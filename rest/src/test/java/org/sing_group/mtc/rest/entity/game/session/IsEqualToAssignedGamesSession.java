/*
 * #%L
 * REST
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
package org.sing_group.mtc.rest.entity.game.session;

import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntity;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.rest.entity.user.IsEqualToIdAndUri;

public class IsEqualToAssignedGamesSession extends IsEqualToEntity<AssignedGamesSession, AssignedGamesSessionData> {
  public IsEqualToAssignedGamesSession(AssignedGamesSession expected) {
    super(expected);
  }

  @Override
  protected boolean matchesSafely(AssignedGamesSessionData actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("id", AssignedGamesSession::getId, AssignedGamesSessionData::getId, actual)
        && checkAttribute("assignmentDate", AssignedGamesSession::getAssignmentDate, AssignedGamesSessionData::getAssignmentDate, actual)
        && checkAttribute("startDate", AssignedGamesSession::getStartDate, AssignedGamesSessionData::getStartDate, actual)
        && checkAttribute("endDate", AssignedGamesSession::getEndDate, AssignedGamesSessionData::getEndDate, actual)
        && matchAttribute("gamesSession", 
          unwrapOptionalFuncion(AssignedGamesSession::getGamesSession),
          AssignedGamesSessionData::getGamesSession,
          actual,
          IsEqualToIdAndUri::equalToGamesSessionIdAndUri
        )
        && matchIterableAttribute("gameResults",
          wrapStreamToIterableFunction(AssignedGamesSession::getGameResults),
          wrapArrayToIterableFunction(AssignedGamesSessionData::getGameResults),
          actual,
          IsEqualToIdAndUri::containsGameResultIdAndUrisInAnyOrder
        );
    }
  }

  @Factory
  public static IsEqualToAssignedGamesSession equalToAssignedGamesSession(AssignedGamesSession session) {
    return new IsEqualToAssignedGamesSession(session);
  }
  
  @Factory
  public static Matcher<Iterable<? extends AssignedGamesSessionData>> containsAssignedGamesSessionsInAnyOrder(AssignedGamesSession... gameSessions) {
    return containsEntityInAnyOrder(IsEqualToAssignedGamesSession::equalToAssignedGamesSession, gameSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends AssignedGamesSessionData>> containsAssignedGamesSessionsInAnyOrder(Iterable<AssignedGamesSession> gameSessions) {
    return containsEntityInAnyOrder(IsEqualToAssignedGamesSession::equalToAssignedGamesSession, gameSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends AssignedGamesSessionData>> containsAssignedGamesSessionsInAnyOrder(Stream<AssignedGamesSession> gameSessions) {
    return containsEntityInAnyOrder(IsEqualToAssignedGamesSession::equalToAssignedGamesSession, gameSessions);
  }
}
