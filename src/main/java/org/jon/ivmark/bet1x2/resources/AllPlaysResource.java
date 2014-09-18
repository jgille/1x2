package org.jon.ivmark.bet1x2.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jon.ivmark.bet1x2.PlayRepository;
import org.jon.ivmark.bet1x2.RoundRepository;
import org.jon.ivmark.bet1x2.api.RoundDto;
import org.jon.ivmark.bet1x2.api.RoundPlayDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.*;

@Path("allplays")
public class AllPlaysResource {

    private final RoundRepository roundRepository;
    private final PlayRepository playRepository;
    private final ObjectMapper objectMapper;

    public AllPlaysResource(RoundRepository roundRepository, PlayRepository playRepository, ObjectMapper objectMapper) {
        this.roundRepository = roundRepository;
        this.playRepository = playRepository;
        this.objectMapper = objectMapper;
    }

    @GET
    public String getAllCompletedPlays() throws JsonProcessingException {
        Map<String, List<RoundPlayDto>> allPlays = playRepository.getAllPlays();

        Map<String, List<RoundPlayDto>> completedPlays = new TreeMap<>();
        List<RoundDto> rounds = roundRepository.getRounds();

        for (Map.Entry<String, List<RoundPlayDto>> e : allPlays.entrySet()) {
            String username = e.getKey();
            List<RoundPlayDto> plays = PlayResource.merge(rounds, e.getValue());
            List<RoundPlayDto> completed = new ArrayList<>();
            for (RoundPlayDto playDto : plays) {
                if (playDto.may_submit_play) {
                    continue;
                }
                completed.add(playDto);
            }
            completedPlays.put(username, completed);
        }
        return objectMapper.writeValueAsString(completedPlays);
    }
}
