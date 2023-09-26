package com.catapan.proxy.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ProxyResourceTest0 {

    private static WebClient webClient;

    @BeforeAll
    public static void setup() {
        webClient = WebClient.create(Vertx.vertx());
    }

    @AfterAll
    public static void tearDown() {
        webClient.close();
    }

    @Test
    void testaProxyResource() throws InterruptedException {

        Thread.sleep(2000);

        webClient.get(8081, "localhost", "/endpointTeste")
                .send(ar -> {
                    if (ar.succeeded()) {
                        assertEquals(200, ar.result().statusCode());

                        // responseBody conforme necessário
                        JsonObject responseBody = ar.result().bodyAsJsonObject();
                    } else {
                        ar.cause().printStackTrace();
                    }
                });
    }
}
