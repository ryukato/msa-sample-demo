package com.msa.cqrs.commandservice;

import org.apache.http.util.Asserts;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductRestControllerParamValidityTest {
    ProductRestController productRestController;
    MockHttpServletResponse mockHttpServletResponse;

    @Mock
    CommandGateway gateway;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productRestController = new ProductRestController();
        mockHttpServletResponse = new MockHttpServletResponse();
    }

    @Test
    public void testAddWithGoodRequestParams() {
        // Arrange
        productRestController.commandGateway = gateway;
        when(gateway.sendAndWait(any())).thenReturn(null);

        // Act
        productRestController.add(UUID.randomUUID().toString(), "Test Add Product", mockHttpServletResponse);

        // Assert
        verify(gateway).sendAndWait(any());
        assertTrue(mockHttpServletResponse.getStatus() == HttpServletResponse.SC_CREATED);

    }

    @Test
    public void testFailedAddWithCommandExecutionException() {
        // Arrange
        productRestController.commandGateway = gateway;
        when(gateway.sendAndWait(any())).thenThrow(CommandExecutionException.class);

        // Act
        productRestController.add(UUID.randomUUID().toString(), "Test Add product", mockHttpServletResponse);

        // Assert
//        verify(gateway).sendAndWait(any())
    }


}