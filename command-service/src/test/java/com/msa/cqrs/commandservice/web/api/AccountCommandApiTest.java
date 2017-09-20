package com.msa.cqrs.commandservice.web.api;

import com.msa.cqrs.commandservice.aggregates.account.AccountOwner;
import com.msa.cqrs.commandservice.web.api.impl.AccountApiImpl;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import java.util.concurrent.CompletableFuture;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountCommandApiTest {
    AccountCommandApi accountCommandApi;
    MockHttpServletResponse mockHttpServletResponse;

    @Mock
    CommandGateway gateway;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountCommandApi = new AccountApiImpl(gateway);
        mockHttpServletResponse = new MockHttpServletResponse();
    }

    @Test
    public void testCreateAccount() throws Exception {
        // Arrange
        CompletableFuture<String> completableFuture = CompletableFuture.completedFuture("test account creator");
        when(gateway.sendAndWait(any())).thenReturn(completableFuture);

        // Act
        CompletableFuture<ResponseEntity<String>> responseEntityCompletableFuture = accountCommandApi.createAccount(new AccountOwner("test account creator"));

        // Assert
        verify(gateway).sendAndWait(any());
        assertTrue(responseEntityCompletableFuture.get().getStatusCode() == HttpStatus.CREATED);
    }

    @Test
    public void testCreateAccountWithEmptyAccountCreator() throws Exception {
        // Arrange
        CompletableFuture<String> completableFuture = CompletableFuture.completedFuture("");
        when(gateway.sendAndWait(any())).thenReturn(completableFuture);

        // Act
        CompletableFuture<ResponseEntity<String>> responseEntityCompletableFuture = accountCommandApi.createAccount(new AccountOwner(""));

        // Assert
//        verify(gateway).sendAndWait(any());
        assertTrue(responseEntityCompletableFuture.get().getStatusCode() == HttpStatus.BAD_REQUEST);
    }


}
