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
package org.sing_group.mtc.rest.resource.entity;

import org.hamcrest.Factory;
import org.sing_group.mtc.domain.entities.IsEqualToEntity;
import org.sing_group.mtc.domain.entities.session.GamesSession;
import org.sing_group.mtc.rest.resource.entity.session.GamesSessionData;

public class IsEqualToGamesSessionData extends IsEqualToEntity<GamesSessionData, GamesSession> {
  public IsEqualToGamesSessionData(GamesSessionData owner) {
    super(owner);
  }

  @Override
  protected boolean matchesSafely(GamesSession actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("id", GamesSessionData::getId, GamesSession::getId, actual)
        && checkAttribute("therapist", GamesSessionData::getTherapist, gs -> Integer.toString(gs.getTherapist().getId()), actual, String::endsWith)
        && matchIterableAttribute("version",
          wrapArrayToIterableFunction(GamesSessionData::getGameVersions),
          wrapStreamToIterableFunction(GamesSession::getVersions),
          actual,
          IsEqualToGamesSessionVersionData::containsGamesSessionVersionInAnyOrder
        );
        //&& checkArrayAttribute("name", GamesSessionData::getGameVersions, gs -> gs.getVersions().toArray(GamesSessionVersion[]::new), actual);
    }
  }

  @Factory
  public static IsEqualToGamesSessionData equalToGameSession(GamesSessionData session) {
    return new IsEqualToGamesSessionData(session);
  }
  
//  
//  @Factory
//  public static IsEqualToGamesSessionData equalToUserData(GamesSessionData user) {
//    return new IsEqualToGamesSessionData(user);
//  }
//
//  @Factory
//  public static Matcher<Iterable<? extends UserData>> containsUsersInAnyOrder(User... users) {
//    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToUser, users);
//  }
//
//  @Factory
//  public static Matcher<Iterable<? extends UserData>> containsUsersInAnyOrder(Iterable<User> users) {
//    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToUser, users);
//  }
//  
//  @Factory
//  public static Matcher<Iterable<? extends UserData>> containsUsersInAnyOrder(Stream<User> users) {
//    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToUser, users);
//  }
//  
//  @Factory
//  public static Matcher<Iterable<? extends UserData>> containsUserDatasInAnyOrder(GamesSessionData... users) {
//    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToUserData, users);
//  }
//  
//  @Factory
//  public static Matcher<Iterable<? extends UserData>> containsUserDatasInAnyOrder(Iterable<GamesSessionData> users) {
//    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToUserData, users);
//  }
//  
//  @Factory
//  public static Matcher<Iterable<? extends UserData>> containsUserDatasInAnyOrder(Stream<GamesSessionData> users) {
//    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToUserData, users);
//  }
}
