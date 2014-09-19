package org.jon.ivmark.bet1x2.api;

import java.util.ArrayList;
import java.util.List;

import static org.jon.ivmark.bet1x2.Cutoff.isAfterCutOff;

public class RoundDto {

    public String round_id;
    public String name;
    public String cut_off;
    public List<GameDto> games;
    public boolean completed;

    public static List<RoundPlayDto> merge(List<RoundDto> rounds, List<RoundPlayDto> plays) {
        if (plays.isEmpty()) {
            return playsFromRounds(rounds);
        }
        int i = 0;
        for (RoundDto roundDto : rounds) {
            if (plays.size() <= i) {
                RoundPlayDto playDto = new RoundPlayDto();
                playDto.plays = new ArrayList<>(roundDto.games.size());
                for (GameDto gameDto : roundDto.games) {
                    playDto.plays.add(new PlayDto());
                }
                plays.add(playDto);
            }
            RoundPlayDto roundPlayDto = plays.get(i++);
            roundPlayDto.round_id = roundDto.round_id;
            roundPlayDto.name = roundDto.name;
            roundPlayDto.cut_off = roundDto.cut_off;
            roundPlayDto.may_submit_play = !isAfterCutOff(roundPlayDto.cut_off);

            int j = 0;

            for (PlayDto playDto : roundPlayDto.plays) {
                playDto.game = roundDto.games.get(j++);
            }
        }
        return plays;
    }

    private static List<RoundPlayDto> playsFromRounds(List<RoundDto> rounds) {
        List<RoundPlayDto> plays = new ArrayList<>(rounds.size());
        for (RoundDto roundDto : rounds) {
            plays.add(playsFromRound(roundDto));
        }
        return plays;
    }

    private static RoundPlayDto playsFromRound(RoundDto roundDto) {
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
