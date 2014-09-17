package org.jon.ivmark.bet1x2;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jon.ivmark.bet1x2.login.resources.LoginResource;

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

        environment.jersey().register(new LoginResource());
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
