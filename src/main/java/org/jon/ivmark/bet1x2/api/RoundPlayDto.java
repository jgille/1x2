package org.jon.ivmark.bet1x2.api;

import java.util.List;

public class RoundPlayDto {

    public String round_id;
    public String name;
    public String cut_off;
    public List<PlayDto> plays;

    public boolean may_submit_play = true;

    public int numPlayedRows() {
        int total = 1;

        for (PlayDto playDto : plays) {
            int i = 0;
            i += (playDto.one ? 1 : 0);
            i += (playDto.x ? 1 : 0);
            i += (playDto.two ? 1 : 0);

            total *= i;
        }

        return total;
    }
}
