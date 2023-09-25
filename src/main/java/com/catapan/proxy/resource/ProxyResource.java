package com.catapan.proxy.resource;

import io.quarkus.vertx.web.Route;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

@ApplicationScoped
public class ProxyResource {

    private static final Logger logger = LoggerFactory.getLogger(ProxyResource.class);

    @ConfigProperty(name = "hostProxy")
    String hostProxy;

    @ConfigProperty(name = "hostPort")
    int hostPort;

    @Inject
    WebClient client;

    @PreDestroy
    public void clean() {
        this.client.close();
    }

    @Route(path = "/*", order = 2)
    public void proxy(RoutingContext rc) {

        Future<HttpResponse<Buffer>> result = getUpstreamResponse(rc);
        if (result != null) {
            result.onSuccess(upstreamResponse -> {
                rc.response().headers().addAll(upstreamResponse.headers());
                rc.response().setStatusCode(upstreamResponse.statusCode()).end(upstreamResponse.bodyAsBuffer());
                logger.info("Successfully ***");
            }).onFailure(error -> {
                logger.error("Error in call with partner", error);
                rc.response().setStatusCode(Response.Status.BAD_GATEWAY.getStatusCode()).end(error.getMessage());
            });
        }
    }

    private Future<HttpResponse<Buffer>> getUpstreamResponse(RoutingContext context) {
        URI uri = URI.create(context.request().uri());

        String queryString = uri.getQuery();
        final var constructedUrl = String.format("%s:%d%s%s", hostProxy, hostPort, uri.getPath(),
                queryString == null ? "" : "?" + queryString);
        logger.info("Constructed URL: {}", constructedUrl);

        HttpRequest<Buffer> request = client.request(
                        HttpMethod.valueOf(context.request().method().name()),
                        hostPort,
                        hostProxy,
                        String.format("%s?%s", uri.getPath(), uri.getQuery()))
                .ssl(true)
                .putHeaders(context.request().headers())
                .putHeader("Host", hostProxy);

        request.putHeaders(context.request().headers())
                .putHeader("Host", hostProxy);

        if (context.body() != null && !context.body().isEmpty()) {
            return request.sendBuffer(context.body().buffer());
        }

        return request.send();
    }
}
