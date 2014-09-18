package org.jon.ivmark.bet1x2;

import org.jon.ivmark.bet1x2.api.RoundPlayDto;
import org.jon.ivmark.bet1x2.login.User;

import java.util.List;

public interface PlayRepository {

    void savePlays(User user, List<RoundPlayDto> plays);

    List<RoundPlayDto> getPlays(User user);
}
