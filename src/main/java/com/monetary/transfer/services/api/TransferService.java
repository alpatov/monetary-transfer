package com.monetary.transfer.services.api;

import com.monetary.transfer.exceptions.NotFoundException;
import com.monetary.transfer.models.ElectronicTransfer;

/**
 * Bank account transfer API.
 */
public interface TransferService {
    /**
     * Looking up for accounts mentioned in electronic funds transfer.
     *
     * @param transfer electronic transfer model
     * @throws NotFoundException if at least one of the transaction accounts is
     * unknown
     */
    void lookup(ElectronicTransfer transfer);
}
