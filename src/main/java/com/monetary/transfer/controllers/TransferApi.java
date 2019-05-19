package com.monetary.transfer.controllers;

import com.monetary.transfer.models.ElectronicTransfer;
import com.monetary.transfer.services.api.TransferService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * REST API for operations with bank accounts.
 */
@Path("/transfer")
@Produces("application/json")
@Consumes("application/json")
public class TransferApi {
    /**
     * Accounts operational API.
     */
    private final TransferService transferService;

    /**
     * Satisfies dependencies.
     *
     * @param transferService accounts operational API
     */
    @Inject
    public TransferApi(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * REST endpoint for bank account transfers.
     *
     * <p>Accepts electronic transfer. Looks up for accounts provided and
     * executes the transfer in thread-safe manner.
     *
     * @param transfer transfer model
     * @return server response
     */
    @POST
    public Response transfer(@Valid ElectronicTransfer transfer) {
        transferService.lookup(transfer);
        transfer.execute();
        return Response.ok().build();
    }
}
