package com.monetary.transfer;

import com.monetary.transfer.config.ApplicationTestConfig;
import com.monetary.transfer.data.TestAccounts;
import com.monetary.transfer.jersey.AbstractJerseyTest;
import com.monetary.transfer.models.Account;
import com.monetary.transfer.models.ElectronicTransfer;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Suite for testing monetary transfer REST API.
 */
public class TransferTestSuite extends AbstractJerseyTest {

    /**
     * Test data source.
     */
    private final TestAccounts testAccounts = new TestAccounts();

    /**
     * {@inheritDoc}
     */
    @Override
    protected Application configure() {
        return new ApplicationTestConfig();
    }

    /**
     * Propagate test data using a special REST interface designed specifically
     * for testing purposes.
     */
    @Before
    public void installTestData() {
        List<Account> accounts = testAccounts.getAccounts();
        GenericEntity<List<Account>> entity = new GenericEntity<List<Account>>(accounts){};
        Entity<GenericEntity<List<Account>>> json = Entity.json(entity);

        assertEquals(
            errors.lookup("test.error.data.installation"),
            Status.OK,
            target("/account")
                .request()
                .post(json)
                .getStatusInfo()
                .toEnum()
        );
    }

    /**
     * Valid transfer execution test.
     *
     * <p>Checks whether an electronic transfer that is deemed valid will
     * succeed.
     */
    @Test
    public void validTransferTest() {
        ElectronicTransfer transfer = testAccounts.getValidElectronicTransfer();
        Entity<ElectronicTransfer> json = Entity.json(transfer);

        assertEquals(
            errors.lookup("test.error.transfer.valid"),
            Status.OK,
            target("/transfer")
                .request()
                .post(json)
                .getStatusInfo()
                .toEnum()
        );
    }

    /**
     * Invalid transfer execution test.
     *
     * <p>Verifies that the transfer with invalid data will not be executed.
     */
    @Test
    public void invalidTransferTest() {
        ElectronicTransfer transfer = testAccounts.getTransferWithEqualPayerAndPayee();
        /*
         * Check that accounts are differ from each other
         */
        assertConstraintViolation(
                "/transfer",
                "POST",
                transfer,
                Status.BAD_REQUEST,
                "error.transfer.accounts.equals"
        );

        /*
         * Check that accounts are differ from each other
         */
        transfer.setPayee(testAccounts.getRandomNonExistingAccount());
        assertConstraintViolation(
                "/transfer",
                "POST",
                transfer,
                Status.NOT_FOUND,
                "error.account.not-found"
        );

        /*
         * Check that impossible debit will be rejected
         */
        transfer = testAccounts.createTransferWithImpossibleDebit();
        assertConstraintViolation(
                "/transfer",
                "POST",
                transfer,
                Status.BAD_REQUEST,
                "error.account.amount"
        );
    }

    /**
     * Integrity validation test.
     *
     * <p>Verifies that data will be validated before processing.
     *
     * <p>Main goal of the verification is to make sure that all known
     * deviations from normal data representation will be controlled by the
     * server properly and user will receive human readable response.
     */
    @Test
    public void transferIntegrityValidationTest() throws IOException {
        /*
         * Check response on empty or malformed request
         */
        assertConstraintViolation(
                "/transfer",
                "POST",
                "test-data/empty-json.json",
                Status.BAD_REQUEST,
                "error.transfer.debit.required",
                "error.transfer.payer.required",
                "error.transfer.payee.required"
        );
        assertConstraintViolation(
                "/transfer",
                "POST",
                "test-data/malformed-json.json",
                Status.BAD_REQUEST
        );
        assertConstraintViolation(
                "/transfer",
                "POST",
                "test-data/malformed-account-number.json",
                Status.BAD_REQUEST,
                "error.account.number.invalid"
        );
        /*
         * Check response to request with missing recipient information
         */
        assertConstraintViolation(
                "/transfer",
                "POST",
                "test-data/missing-recipient.json",
                Status.BAD_REQUEST,
                "error.transfer.payee.required"
        );
        /*
         * Check response to request with missing payer information
         */
        assertConstraintViolation(
                "/transfer",
                "POST",
                "test-data/missing-payer.json",
                Status.BAD_REQUEST,
                "error.transfer.payer.required"
        );
        /*
         * Check response to requests with inappropriate debit specification
         */
        assertConstraintViolation(
                "/transfer",
                "POST",
                "test-data/negative-debit.json",
                Status.BAD_REQUEST,
                "error.transfer.debit.positiveness"
        );
        assertConstraintViolation(
                "/transfer",
                "POST",
                "test-data/zero-debit.json",
                Status.BAD_REQUEST,
                "error.transfer.debit.positiveness"
        );
        assertConstraintViolation(
                "/transfer",
                "POST",
                "test-data/missing-debit.json",
                Status.BAD_REQUEST,
                "error.transfer.debit.required"
        );
        assertConstraintViolation(
                "/transfer",
                "POST",
                "test-data/malformed-debit.json",
                Status.BAD_REQUEST
        );
        assertConstraintViolation(
                "/transfer",
                "POST",
                "test-data/impossible-debit.json",
                Status.BAD_REQUEST,
                "error.transfer.debit.min"
        );
    }
}
