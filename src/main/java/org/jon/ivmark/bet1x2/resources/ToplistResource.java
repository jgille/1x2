package org.jon.ivmark.bet1x2.resources;

import io.dropwizard.auth.Auth;
import org.jon.ivmark.bet1x2.PlayRepository;
import org.jon.ivmark.bet1x2.RoundRepository;
import org.jon.ivmark.bet1x2.api.*;
import org.jon.ivmark.bet1x2.login.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.*;

@Path("toplist")
public class ToplistResource {

    private final RoundRepository roundRepository;
    private final PlayRepository playRepository;

    public ToplistResource(RoundRepository roundRepository, PlayRepository playRepository) {
        this.roundRepository = roundRepository;
        this.playRepository = playRepository;
    }

    @GET
    public ToplistDto getToplist(@Auth User user) {
        Map<String, ToplistDto.Entry> entries = new HashMap<>();

        List<RoundDto> rounds = roundRepository.getRounds();
        Map<String, List<RoundPlayDto>> allPlays = playRepository.getAllPlays();
        int i = 0;
        for (RoundDto round : rounds) {
            for (Map.Entry<String, List<RoundPlayDto>> e : allPlays.entrySet()) {
                List<RoundPlayDto> plays = e.getValue();
                int correct = 0;
                if (i < plays.size()) {
                    RoundPlayDto playDto = plays.get(i);
                    correct = countCorrect(round, playDto);
                }
                ToplistDto.Entry entry = entries.get(e.getKey());
                if (entry == null) {
                    entry = new ToplistDto.Entry();
                    entry.username = e.getKey();
                    entry.total = new ToplistDto.Result();
                    entry.rounds = new ArrayList<>();

                    entries.put(entry.username, entry);
                }

                entry.total.correct += correct;
                ToplistDto.Result thisRound = new ToplistDto.Result();
                thisRound.correct = correct;
                entry.rounds.add(thisRound);

            }
            i++;
        }

        ArrayList<ToplistDto.Entry> entryList = new ArrayList<>(entries.values());

        // TODO: Set position
        Collections.sort(entryList, new Comparator<ToplistDto.Entry>() {
            @Override
            public int compare(ToplistDto.Entry o1, ToplistDto.Entry o2) {
                return o2.total.correct - o1.total.correct;
            }
        });

        int cnt = 0;
        int previosPos = 1;
        int previousCorrect = -1;

        for (ToplistDto.Entry entry : entryList) {
            cnt++;
            if (entry.total.correct < previousCorrect) {
                entry.position = cnt + "";
                previosPos = cnt;
            } else {
                entry.position = previosPos + "";
            }
            previousCorrect = entry.total.correct;
        }


        ToplistDto toplistDto = new ToplistDto();

        toplistDto.entries = entryList;

        ToplistDto.Metadata metadata = new ToplistDto.Metadata();
        metadata.rounds = new ArrayList<>();
        for (RoundDto roundDto : rounds) {
            metadata.rounds.add(roundDto.name);
        }
        toplistDto.metadata = metadata;

        return toplistDto;
    }

    private int countCorrect(RoundDto round, RoundPlayDto plays) {
        int i = 0;
        int correct = 0;
        for (PlayDto playDto : plays.plays) {
            GameDto gameDto = round.games.get(i++);
            String result = gameDto.result;
            if (result == null) {
                result = "";
            }

            if ((playDto.one && result.equals("1"))
                    || (playDto.x && result.equalsIgnoreCase("x"))
                    || (playDto.two && result.equals("2"))
                    ) {
                correct++;
            }
        }
        return correct;
    }

}