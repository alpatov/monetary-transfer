package com.monetary.transfer.services.api;

import com.monetary.transfer.exceptions.IntegrityException;
import com.monetary.transfer.exceptions.NotFoundException;
import com.monetary.transfer.models.Account;

import javax.inject.Singleton;
import java.util.function.Supplier;

/**
 * Service for storing and retrieving bank accounts.
 */
@Singleton
public interface AccountService {
    /**
     * Looking up for bank account in memory storage.
     *
     * @param number account number
     * @return bank account instance
     * @throws NotFoundException in case when account is not present in the
     * storage
     */
    Account lookup(String number);

    /**
     * Looking up for bank account in memory storage.
     *
     * @param accountNumberSupplier supplier of an account number
     * @return bank account instance
     * @throws NotFoundException in case when account is not present in the
     * storage
     */
    default Account lookup(Supplier<String> accountNumberSupplier) {
        return lookup(accountNumberSupplier.get());
    }

    /**
     * Stores account in memory storage.
     *
     * @param account bank account
     * @throws IntegrityException if account of the same number already  exists
     * in the storage
     */
    void store(Account account);
}
