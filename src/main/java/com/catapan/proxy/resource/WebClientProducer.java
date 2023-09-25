package com.catapan.proxy.resource;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class WebClientProducer {

    @Inject
    Vertx vertx;

    @Produces
    @ApplicationScoped
    WebClient produceWebClient() {
        return WebClient.create(vertx);
    }

    public void close(@Disposes WebClient webClient) {
        webClient.close();
    }
}
