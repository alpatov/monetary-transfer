package com.monetary.transfer.services;

import com.monetary.transfer.exceptions.IntegrityException;
import com.monetary.transfer.exceptions.NotFoundException;
import com.monetary.transfer.models.Account;
import com.monetary.transfer.services.api.AccountService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Account Service implementation.
 */
public class AccountServiceImpl implements AccountService {
    /**
     * Bank account memory storage.
     */
    private final Map<String, Account> accounts;

    public AccountServiceImpl() {
        accounts = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * {@link NotFoundException} provider method.
     *
     * @return new instance of {@link NotFoundException}.
     */
    private NotFoundException NotFoundExceptionSupplier() {
        return new NotFoundException("error.account.not-found");
    }

    /**
     * Check account for novelty.
     *
     * @param account bank account
     */
    private void requireNovel(Account account) {
        if (accounts.containsKey(account.getNumber())) {
            throw new IntegrityException("error.account.duplicated");
        }
    }

    @Override
    public final Account lookup(String number) {
        Account account = accounts.get(number);
        return Optional
                .ofNullable(account)
                .orElseThrow(this::NotFoundExceptionSupplier);
    }

    @Override
    public void store(Account account) {
        requireNovel(account);
        accounts.put(account.getNumber(), account);
    }
}
