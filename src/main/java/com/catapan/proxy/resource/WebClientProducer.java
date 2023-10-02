package com.catapan.proxy.resource;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.authentication.TokenCredentials;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2Options;
import io.vertx.ext.web.client.OAuth2WebClient;
import io.vertx.ext.web.client.WebClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class WebClientProducer {
    @ConfigProperty(name = "oauth2.clientId")
    String clientId;
    @ConfigProperty(name = "oauth2.clientSecret")
    String clientSecret;
    @ConfigProperty(name = "oauth2.site")
    String site;
    @ConfigProperty(name = "oauth2.tokenPath")
    String tokenPath;
    @ConfigProperty(name = "oauth2.authorizationPath")
    String authorizationPath;
    @Inject
    Vertx vertx;

    @Produces
    @ApplicationScoped
    OAuth2WebClient produceOAuth2WebClient() {
        WebClient webClient = WebClient.create(vertx);
        OAuth2Options oauth2Options = new OAuth2Options();
        oauth2Options.setClientID(clientId)
                .setClientSecret(clientSecret)
                .setSite(site)
                .setTokenPath(tokenPath)
                .setAuthorizationPath(authorizationPath);

        OAuth2Auth authProvider = OAuth2Auth.create(vertx, oauth2Options);
        OAuth2WebClient oauth2Client = OAuth2WebClient.create(webClient, authProvider);

        oauth2Client.withCredentials(new TokenCredentials("xC_FPxv-cNlFMUbGzc94z_WNrQzRyh-egs8hZZRrbQqVga0ML4Vq0Qoicem1eRA9rxrk_S1h"));

        return oauth2Client;
    }

    public void close(@Disposes OAuth2WebClient oauth2WebClient) {
        oauth2WebClient.close();
    }
}
