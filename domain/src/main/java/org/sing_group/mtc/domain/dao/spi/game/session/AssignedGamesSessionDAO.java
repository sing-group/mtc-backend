/*
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 Miguel Reboiro-Jato and Adolfo Piñón Blanco
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.mtc.domain.dao.spi.game.session;

import java.util.Date;
import java.util.stream.Stream;

import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Patient;

public interface AssignedGamesSessionDAO {
  public AssignedGamesSession get(int assignedId);

  public Stream<AssignedGamesSession> listByPatient(Patient patient, ListingOptions options);

  public AssignedGamesSession assignSession(Patient patient, GamesSession gamesSession, Date startDate, Date endDate);

  public AssignedGamesSession modify(AssignedGamesSession assigned);

  public void delete(int sessionId);
}
