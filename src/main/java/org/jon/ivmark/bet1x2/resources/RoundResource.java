package org.jon.ivmark.bet1x2.resources;

import io.dropwizard.auth.Auth;
import org.jon.ivmark.bet1x2.RoundRepository;
import org.jon.ivmark.bet1x2.api.RoundDto;
import org.jon.ivmark.bet1x2.login.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("rounds")
public class RoundResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoundRepository repository;

    public RoundResource(RoundRepository repository) {
        this.repository = repository;
    }

    @GET
    public List<RoundDto> getRounds() {
        logger.info("Getting rounds");
        return repository.getRounds();
    }

    @POST
    public Response saveRounds(@Auth User user, List<RoundDto> rounds) {
        logger.info("Saving rounds");
        repository.saveRounds(rounds);
        return Response.ok().build();
    }
}
