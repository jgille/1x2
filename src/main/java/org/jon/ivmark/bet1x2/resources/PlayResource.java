package org.jon.ivmark.bet1x2.resources;

import io.dropwizard.auth.Auth;
import org.jon.ivmark.bet1x2.PlayRepository;
import org.jon.ivmark.bet1x2.RoundRepository;
import org.jon.ivmark.bet1x2.api.RoundDto;
import org.jon.ivmark.bet1x2.api.RoundPlayDto;
import org.jon.ivmark.bet1x2.login.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.jon.ivmark.bet1x2.Cutoff.isAfterCutOff;

@Path("myplays")
public class PlayResource {

    private static final int MAX_NUM_ROWS = 72;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoundRepository roundRepository;
    private final PlayRepository playRepository;

    public PlayResource(RoundRepository roundRepository, PlayRepository playRepository) {
        this.roundRepository = roundRepository;
        this.playRepository = playRepository;
    }

    @GET
    public List<RoundPlayDto> getPlays(@Auth User user) {
        logger.info("Getting plays for {}", user.username);
        List<RoundDto> rounds = roundRepository.getRounds();
        List<RoundPlayDto> plays = playRepository.getPlays(user);
        return RoundDto.merge(rounds, plays);
    }

    private void savePlays(User user, List<RoundPlayDto> plays) {
        playRepository.savePlays(user, plays);
    }

    @PUT
    public Response savePlay(@Auth User user, RoundPlayDto play) {
        if (play.numPlayedRows() > MAX_NUM_ROWS) {
            return Response.status(403).build();
        }

        if (isAfterCutOff(play.cut_off)) {
            return Response.status(403).build();
        }

        logger.info("Saving play for {}", user.username);
        List<RoundPlayDto> plays = getPlays(user);

        List<RoundPlayDto> toSave = new ArrayList<>(plays.size());
        for (RoundPlayDto playDto : plays) {
            if (playDto.round_id.equals(play.round_id)) {
                toSave.add(play);
            } else {
                toSave.add(playDto);
            }
        }

        savePlays(user, toSave);
        return Response.ok().build();
    }

}


