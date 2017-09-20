package com.msa.cqrs.commandservice;

import com.msa.cqrs.commandservice.command.AddProductCommand;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.ConcurrencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class ProductRestController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    CommandGateway commandGateway;


    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    public void add(
            @PathVariable(value = "id") String id,
            @RequestParam(value = "name", required = true) String name,
            HttpServletResponse response) {

        logger.debug("Adding product [{}] '{}'", id, name);

        try {
            AddProductCommand command = new AddProductCommand(id, name);
            commandGateway.sendAndWait(command);
            logger.info("Added product [{}] '{}'", id, name);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return;
        } catch (CommandExecutionException cex) {
            logger.warn("Added command FAILED with Message: {}", cex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            Optional.ofNullable(cex.getCause()).ifPresent(e -> {
                logger.warn("Caused by: {}, {}", e.getClass().getName(), e.getMessage());
                if (e instanceof ConcurrencyException) {
                    logger.warn("A duplicate product with same ID [{}] already exists.", id);
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            });
        }
    }
}
