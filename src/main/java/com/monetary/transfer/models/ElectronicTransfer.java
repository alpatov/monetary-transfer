package com.monetary.transfer.models;

import com.monetary.transfer.exceptions.IntegrityException;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Electronic funds transfer (EFT).
 *
 * <p>Electronic transfer of money from one bank account to another.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicTransfer {

    /**
     * Account to which funds will be credited.
     */
    @NotNull(message = "error.transfer.payee.required")
    @Valid
    private Account payee;

    /**
     * Account from which funds will be charged.
     */
    @NotNull(message = "error.transfer.payer.required")
    @Valid
    private Account payer;

    /**
     * Funds to transfer.
     *
     * <p>May be {@code null} for DTO pattern to work properly when receiving
     * data from client.
     */
    @NotNull(message = "error.transfer.debit.required")
    @Positive(message = "error.transfer.debit.positiveness")
    @DecimalMin(
        message = "error.transfer.debit.min",
        value = "0.01"
    )
    private volatile BigDecimal debit;

    /**
     * Checks whether payer and beneficiary accounts are different.
     *
     * @throws IntegrityException when payer and payee account are identical
     */
    private void requireNonEqualAccounts() {
        if (payer.equals(payee)) {
            throw new IntegrityException("error.transfer.accounts.equals");
        }
    }

    /**
     * Execute financial transaction.
     *
     * @throws IntegrityException in case of integrity problems.
     */
    @Synchronized
    public final void execute() {
        /*
         * Checking for data integrity
         */
        requireNonEqualAccounts();
        payer.requireBalance(debit);
        /*
         * Transferring
         */
        payer.withdraw(debit);
        payee.deposit(debit);
    }
}
