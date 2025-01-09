package com.akhil.linkedin.api_gateway.filters;

import com.akhil.linkedin.api_gateway.services.JwtService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    public static class Config {
        // Put configuration properties here
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            log.info("Login Request Path: {}", exchange.getRequest().getPath());

            final String tokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if(tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                log.error("Authorization header is missing in the request");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            final String token = tokenHeader.substring(7);

            try{
                String userId = jwtService.getUserIdFromToken(token);
                log.info("Getting userID from token: {}", userId);
                ServerWebExchange modifiedExchange = exchange
                        .mutate()
                        .request(r -> r.header("X-User-Id", userId))
                        .build();

                return chain.filter(modifiedExchange);
            } catch (Exception e) {
                log.error("Error while parsing token: {}", e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }
}
