package org.jon.ivmark.bet1x2.resources;

import org.jon.ivmark.bet1x2.api.RoundDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("rounds")
public class RoundResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<RoundDto> rounds = new ArrayList<>();

    @GET
    public List<RoundDto> getRounds() {
        logger.info("Getting rounds");
        return rounds;
    }

    @POST
    public Response saveRounds(List<RoundDto> rounds) {
        logger.info("Saving rounds");
        this.rounds = rounds;
        return Response.ok().build();
    }
}
