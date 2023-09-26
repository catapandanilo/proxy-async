package com.catapan.proxy.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

@QuarkusTest
class ProxyResourceTest {

    @Inject
    ProxyResource proxyResource;

    @Test
    void testaProxyResource(VertxTestContext testContext) {
        RoutingContext mockRoutingContext = Mockito.mock(RoutingContext.class);

        // mock para métodos específicos de mockRoutingContext
        // when(mockRoutingContext.request()).thenReturn(instanciaMockDeRequest);

        proxyResource.proxy(mockRoutingContext);

        // garantir que o teste seja concluído após a chamada assíncrona
        testContext.completeNow();
    }
}

