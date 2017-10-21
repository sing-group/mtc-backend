package org.sing_group.mtc.service.spi.game.session;

import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;

public interface AssignedGamesSessionService {

  public AssignedGamesSession get(int assignedId);

}
