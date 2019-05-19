package com.monetary.transfer.data;

import com.monetary.transfer.models.Account;
import com.monetary.transfer.models.ElectronicTransfer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Data repository for testing purposes.
 */
@Data
public class TestAccounts {

    /**
     * Random numbers generator.
     */
    private transient Random random = new Random();

    /**
     * The set of bank accounts for test purposes.
     */
    private final List<Account> accounts;

    /**
     * Generates the set of bank accounts with randomly selected account numbers
     * and balances.
     */
    public TestAccounts() {
        accounts = Stream
                .generate(AccountUtils::getRandomAccount)
                .parallel()
                .limit(1000)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Returns randomly selected account from the of accounts set created
     * during instantiation of the current object.
     *
     * @return new instance of {@link Account}
     */
    private Account getRandomExistingAccount() {
        int index = random.nextInt(accounts.size());
        return accounts.get(index);
    }

    /**
     * Generates a bank account with randomly selected number, so it is not
     * presented in test data generated for test purposes.
     *
     * @return new instance of {@link Account}
     */
    public Account getRandomNonExistingAccount() {
        Account account;
        do account = AccountUtils.getRandomAccount();
        while (accounts.contains(account));
        account.setBalance(null);
        return account;
    }

    private BigDecimal getRandomValidTransferDebit(Account account) {
        return AccountUtils.randomAccountBalance(account.getBalance()::intValue);
    }

    /**
     * Returns valid random electronic transfer.
     *
     * <p>It is guaranteed that the payer and the payee will represent different bank
     * accounts.
     *
     * <p>It is also guaranteed that debit value of constructed electronic
     * funds transfer will be less or equals to payees account balance.
     *
     * @return new instance of {@link ElectronicTransfer}
     */
    public ElectronicTransfer getValidElectronicTransfer() {
        /*
         * Peek random payee
         */
        Account payee = getRandomExistingAccount();
        /*
         * Peek such a payer who does not coincide with the recipient of funds
         * and has funds available for transfer.
         */
        Account payer;
        do payer = getRandomExistingAccount();
        while (
            payer.equals(payee) ||
            payer.getBalance().compareTo(BigDecimal.ZERO) <= 0
        );
        /*
         * Peek random debit
         */
        BigDecimal debit = getRandomValidTransferDebit(payer);
        /*
         * Collect transfer together
         */
        return ElectronicTransfer
                .builder()
                .payee(payee)
                .payer(payer)
                .debit(debit)
                .build();
    }

    /**
     * Returns transfer with equal payer and payee accounts.
     *
     * @return new instance of {@link ElectronicTransfer}
     */
    public ElectronicTransfer getTransferWithEqualPayerAndPayee() {
        Account account = getRandomExistingAccount();
        BigDecimal debit = getRandomValidTransferDebit(account);
        return ElectronicTransfer
                .builder()
                .payer(account)
                .payee(account)
                .debit(debit)
                .build();
    }

    public ElectronicTransfer createTransferWithImpossibleDebit() {
        ElectronicTransfer transfer = getValidElectronicTransfer();
        BigDecimal impossibleDebit = transfer.getPayer().getBalance().multiply(BigDecimal.TEN);
        transfer.setDebit(impossibleDebit);
        return transfer;
    }
}
