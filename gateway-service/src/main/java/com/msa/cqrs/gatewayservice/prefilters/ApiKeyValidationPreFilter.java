package com.msa.cqrs.gatewayservice.prefilters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class ApiKeyValidationPreFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String apiKey = request.getHeader("API_KEY");
        logger.debug("{} request to {} with api key {}", request.getMethod(), request.getRequestURL().toString(), apiKey);
        return null;
    }
}
