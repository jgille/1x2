package org.jon.ivmark.bet1x2.login.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.idsite.IdSiteUrlBuilder;
import org.jon.ivmark.bet1x2.login.JwtService;
import org.jon.ivmark.bet1x2.login.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;

@Path("login")
public class LoginResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Application application;

    private final JwtService jwtService;

    public LoginResource(Application application, JwtService jwtService) {
        this.application = application;
        this.jwtService = jwtService;
    }

    @GET
    public Response login(@Context UriInfo uriInfo) throws URISyntaxException, JsonProcessingException {
        String baseUri = uriInfo.getBaseUri().toString();

        if (isRunningOnLocalhost(baseUri)) {
            String uri = baseUri.replaceAll("/api", "");
            logger.info("Dev mode on localhost");

            User user = new User();
            user.username = "Developer 2";

            NewCookie cookie = jwtService.userCookie(user);

            return Response.status(302).location(URI.create(uri)).cookie(cookie).build();
        }

        IdSiteUrlBuilder idSiteBuilder = application.newIdSiteUrlBuilder();
        String url = baseUri + "authenticate";
        logger.info("Callback url: {}", url);
        idSiteBuilder.setCallbackUri(url);

        return Response.status(302).location(URI.create(idSiteBuilder.build())).build();
    }

    private boolean isRunningOnLocalhost(String baseUri) {
        return baseUri.contains("localhost");
    }
}
