package org.jon.ivmark.bet1x2.login.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.impl.jwt.signer.JwtSigner;
import org.jon.ivmark.bet1x2.login.JwtService;
import org.jon.ivmark.bet1x2.login.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;

@Path("authenticate")
public class AuthenticatorResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Application application;
    private final JwtService jwtService;

    public AuthenticatorResource(Application application, JwtService jwtService) {
        this.application = application;
        this.jwtService = jwtService;
    }

    @GET
    public Response authenticate(@Context HttpServletRequest request, @Context UriInfo uriInfo)
            throws JsonProcessingException {
        logger.info("Authenticating...");

        Account account = application.newIdSiteCallbackHandler(request).getAccountResult().getAccount();
        User user = userFromAccount(account);

        String baseUri = uriInfo.getBaseUri().toString();
        String uri = baseUri.replaceAll("/api", "");

        NewCookie cookie = jwtService.userCookie(user);
        return Response.status(302)
                       .location(URI.create(uri))
                       .cookie(cookie)
                       .build();
    }

    private User userFromAccount(Account account) {
        User user = new User();
        user.username = account.getUsername();
        user.groups = new ArrayList<>();
        for (Group group : account.getGroups()) {
             user.groups.add(group.getName());
        }
        return user;
    }

}
