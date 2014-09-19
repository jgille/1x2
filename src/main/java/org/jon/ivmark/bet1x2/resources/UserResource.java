package org.jon.ivmark.bet1x2.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.auth.Auth;
import org.apache.commons.io.FileUtils;
import org.jon.ivmark.bet1x2.login.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

@Path("team")
public class UserResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final File baseDir;

    public UserResource(String dataDir) {
        this.baseDir = new File(dataDir, "teams");
    }

    @GET
    public String getTeamName(@Auth User user) throws IOException {
        logger.info("Getting team name for {}", user.username);
        File file = new File(baseDir, user.username);
        String team = FileUtils.readFileToString(file);
        return team == null || team.isEmpty() ? user.username : team;
    }

    @POST
    public Response setTeamName(@Auth User user, String teamName) throws IOException {
        logger.info("Saving team name for {}, team name {}", user.username, teamName);

        File file = new File(baseDir, user.username);
        FileUtils.writeStringToFile(file, teamName);
        return Response.ok().build();
    }
}
