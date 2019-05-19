package com.monetary.transfer.services;

import com.monetary.transfer.models.Account;
import com.monetary.transfer.models.ElectronicTransfer;
import com.monetary.transfer.services.api.AccountService;
import com.monetary.transfer.services.api.TransferService;
import lombok.Synchronized;

import javax.inject.Inject;

/**
 * Transfer service implementation.
 */
public class TransferServiceImpl implements TransferService {

    /**
     * Account storage.
     */
    private final AccountService accountService;

    /**
     * Satisfies dependencies.
     *
     * @param accountService account storage service
     */
    @Inject
    public TransferServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @Synchronized
    public final void lookup(ElectronicTransfer transfer) {
        Account payeeAccount = accountService.lookup(transfer.getPayee()::getNumber);
        Account payerAccount = accountService.lookup(transfer.getPayer()::getNumber);
        transfer.setPayee(payeeAccount);
        transfer.setPayer(payerAccount);
    }
}
