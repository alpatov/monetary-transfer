package com.monetary.transfer.controllers;

import com.monetary.transfer.models.Account;
import com.monetary.transfer.services.api.AccountService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * Test controller for creating test dummy data.
 */
@Path("/account")
@Consumes("application/json")
public class AccountApiExtension {

    /**
     * TestAccounts operational API.
     */
    private final AccountService accountService;

    /**
     * Satisfies dependencies.
     *
     * @param accountService accounts operational API
     */
    @Inject
    public AccountApiExtension(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Stores account if it is valid and the account with the same number is not
     * in the storage.
     *
     * @param account bank account
     * @return server response
     */
    @PUT
    public Response create(@Valid Account account) {
        accountService.store(account);
        return Response.ok().build();
    }

    /**
     * Stores all of provided accounts if they are valid and are not in the
     * storage.
     *
     * @param accounts a set of bank accounts
     * @return server response
     */
    @POST
    public Response create(@Valid Set<Account> accounts) {
        accounts.parallelStream().forEach(accountService::store);
        return Response.ok().build();
    }
}
