package org.jon.ivmark.bet1x2;

import org.jon.ivmark.bet1x2.api.RoundDto;

import java.util.List;

public interface RoundRepository {

    void saveRounds(List<RoundDto> rounds);

    List<RoundDto> getRounds();
}
