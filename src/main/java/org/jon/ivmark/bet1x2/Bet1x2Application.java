package org.jon.ivmark.bet1x2;

import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.impl.jwt.signer.DefaultJwtSigner;
import com.stormpath.sdk.impl.jwt.signer.JwtSigner;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jon.ivmark.bet1x2.login.JwtService;
import org.jon.ivmark.bet1x2.login.resources.AuthenticatorResource;
import org.jon.ivmark.bet1x2.login.resources.LoginResource;
import org.jon.ivmark.bet1x2.resources.RoundResource;

public class Bet1x2Application extends Application<Bet1x2Config> {

    public static void main(String[] args) throws Exception {
        new Bet1x2Application().run(args);
    }

    @Override
    public void initialize(Bootstrap<Bet1x2Config> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
    }

    @Override
    public void run(Bet1x2Config config, Environment environment) throws Exception {
        environment.jersey().setUrlPattern("/api/*");

        ApiKey apiKey = ApiKeys.builder().setFileLocation("apiKey.properties").build();
        Client client = Clients.builder().setApiKey(apiKey).build();

        String applicationRestUrl = "https://api.stormpath.com/v1/applications/1lAAjIdXrAVGINdy3AoqYl";
        com.stormpath.sdk.application.Application application =
                client.getResource(applicationRestUrl, com.stormpath.sdk.application.Application.class);

        JwtSigner jwtSigner =  new DefaultJwtSigner(config.jwtSecret);
        JwtService jwtService = new JwtService(jwtSigner, environment.getObjectMapper());

        environment.jersey().register(new LoginResource(application, jwtService));
        environment.jersey().register(new AuthenticatorResource(application, jwtService));
        RoundRepository repository = new FileRoundRepository(config.dataDir, environment.getObjectMapper());
        environment.jersey().register(new RoundResource(repository));
    }
}
