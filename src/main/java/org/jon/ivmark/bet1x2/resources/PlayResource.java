package org.jon.ivmark.bet1x2.resources;

import io.dropwizard.auth.Auth;
import org.jon.ivmark.bet1x2.PlayRepository;
import org.jon.ivmark.bet1x2.RoundRepository;
import org.jon.ivmark.bet1x2.api.GameDto;
import org.jon.ivmark.bet1x2.api.PlayDto;
import org.jon.ivmark.bet1x2.api.RoundDto;
import org.jon.ivmark.bet1x2.api.RoundPlayDto;
import org.jon.ivmark.bet1x2.login.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("myplays")
public class PlayResource {

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
        return merge(rounds, plays);
    }

    @POST
    public Response savePlays(@Auth User user, List<RoundPlayDto> plays) {
        logger.info("Saving plays for {}", user.username);
        playRepository.savePlays(user, plays);
        return Response.ok().build();
    }

    @PUT
    public Response savePlay(@Auth User user, RoundPlayDto play) {
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

    private List<RoundPlayDto> merge(List<RoundDto> rounds, List<RoundPlayDto> plays) {
        if (plays.isEmpty()) {
            return playsFromRounds(rounds);
        }
        int i = 0;
        for (RoundPlayDto roundPlayDto : plays) {
            RoundDto roundDto = rounds.get(i++);
            roundPlayDto.round_id = roundDto.round_id;
            roundPlayDto.name = roundDto.name;
            roundPlayDto.cut_off = roundDto.cut_off;

            int j = 0;

            for (PlayDto playDto : roundPlayDto.plays) {
                playDto.game = roundDto.games.get(j++);
            }
        }
        return plays;
    }

    private List<RoundPlayDto> playsFromRounds(List<RoundDto> rounds) {
        List<RoundPlayDto> plays = new ArrayList<>(rounds.size());
        for (RoundDto roundDto : rounds) {
            plays.add(playsFromRound(roundDto));
        }
        return plays;
    }

    private RoundPlayDto playsFromRound(RoundDto roundDto) {
        RoundPlayDto roundPlayDto = new RoundPlayDto();
        roundPlayDto.cut_off = roundDto.cut_off;
        roundPlayDto.name = roundDto.name;
        roundPlayDto.round_id = roundDto.round_id;

        roundPlayDto.plays = new ArrayList<>(roundDto.games.size());

        for (GameDto gameDto : roundDto.games) {
            PlayDto play = new PlayDto();
            play.game = gameDto;
            roundPlayDto.plays.add(play);
        }
        return roundPlayDto;
    }
}
