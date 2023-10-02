package com.catapan.proxy.resource;

import io.quarkus.vertx.web.Route;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.OAuth2WebClient;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ProxyResource {
    private static final Logger logger = LoggerFactory.getLogger(ProxyResource.class);
    @ConfigProperty(name = "hostProxy")
    String hostProxy;
    @ConfigProperty(name = "hostPort")
    int hostPort;
    @Inject
    OAuth2WebClient oauth2Client;

    @PreDestroy
    public void clean() {
        this.oauth2Client.close();
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
                String constructedUrl = String.format("https://%s:%d%s", hostProxy, hostPort, context.request().uri());
        logger.info("Constructed URL: {}", constructedUrl);

        HttpRequest<Buffer> request = oauth2Client.request(HttpMethod.valueOf(context.request().method().name()), hostPort, hostProxy, context.request().uri())
                .ssl(true)
                .putHeaders(context.request().headers())
                .putHeader("Host", hostProxy);

        request.putHeader("Authorization", "Bearer xC_FPxv-cNlFMUbGzc94z_WNrQzRyh-egs8hZZRrbQqVga0ML4Vq0Qoicem1eRA9rxrk_S1h");

        if (context.body() != null && !context.body().isEmpty()) {
            return request.sendBuffer(context.body().buffer());
        }

        return request.send();
    }
}
