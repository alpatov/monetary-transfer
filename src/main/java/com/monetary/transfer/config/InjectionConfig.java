package com.monetary.transfer.config;

import com.monetary.transfer.resources.ErrorMessagesRepository;
import com.monetary.transfer.services.AccountServiceImpl;
import com.monetary.transfer.services.TransferServiceImpl;
import com.monetary.transfer.services.api.AccountService;
import com.monetary.transfer.services.api.TransferService;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.inject.Singleton;

/**
 * Dependency Injection configuration class.
 */
class InjectionConfig extends AbstractBinder {

    /**
     * Provides binding definitions using the exposed binding methods.
     */
    @Override
    protected void configure() {
        bind(AccountServiceImpl.class).to(AccountService.class).in(Singleton.class);
        bind(TransferServiceImpl.class).to(TransferService.class).in(RequestScoped.class);
        bindAsContract(ErrorMessagesRepository.class);
    }
}
