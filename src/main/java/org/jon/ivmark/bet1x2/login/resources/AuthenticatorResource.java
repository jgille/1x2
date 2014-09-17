package org.jon.ivmark.bet1x2.login.resources;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.awt.image.PixelGrabber;
import java.net.URI;

@Path("authenticate")
public class AuthenticatorResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Application application;

    public AuthenticatorResource(Application application) {
        this.application = application;
    }

    @GET
    public Response authenticate(@Context HttpServletRequest request, @Context UriInfo uriInfo) {
        logger.info("Authenticating...");

        Account account = application.newIdSiteCallbackHandler(request).getAccountResult().getAccount();

        String baseUri = uriInfo.getBaseUri().toString();
        String uri = baseUri.replaceAll("/api", "");
        Cookie jwt = new Cookie("jwt", account.getUsername(), "/", null);
        NewCookie cookie = new NewCookie(jwt);
        return Response.status(302)
                       .location(URI.create(uri))
                       .cookie(cookie)
                       .build();
    }
}
