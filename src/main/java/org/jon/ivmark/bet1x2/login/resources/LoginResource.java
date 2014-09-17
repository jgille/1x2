package org.jon.ivmark.bet1x2.login.resources;

import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.idsite.IdSiteUrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

@Path("login")
public class LoginResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Application application;

    public LoginResource(Application application) {
        this.application = application;
    }

    @GET
    public Response login(@Context UriInfo uriInfo) throws URISyntaxException {
        IdSiteUrlBuilder idSiteBuilder = application.newIdSiteUrlBuilder();

        String baseUri = uriInfo.getBaseUri().toString();
        String url = baseUri + "authenticate";
        logger.info("Callback url: {}", url);
        idSiteBuilder.setCallbackUri(url);

        return Response.status(302).location(URI.create(idSiteBuilder.build())).build();
    }
}
