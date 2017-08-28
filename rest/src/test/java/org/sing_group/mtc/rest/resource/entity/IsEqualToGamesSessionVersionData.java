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
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntity;
import org.sing_group.mtc.domain.entities.session.GamesSessionVersion;
import org.sing_group.mtc.rest.resource.entity.session.GamesSessionVersionData;

public class IsEqualToGamesSessionVersionData extends IsEqualToEntity<GamesSessionVersionData, GamesSessionVersion> {
  public IsEqualToGamesSessionVersionData(GamesSessionVersionData owner) {
    super(owner);
  }

  @Override
  protected boolean matchesSafely(GamesSessionVersion actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("version", GamesSessionVersionData::getVersion, GamesSessionVersion::getVersion, actual);
        //&& matchArrayAttribute("gameConfiguration", GamesSessionVersionData::getGameConfiguration, GamesSessionVersion::getGameConfigurations, actual, IsEqualToGameConfiguration::isEqualToGameConfiguration);
    }
  }

  @Factory
  public static IsEqualToGamesSessionVersionData equalToGamesSessionVersion(GamesSessionVersionData session) {
    return new IsEqualToGamesSessionVersionData(session);
  }
//  
//  @Factory
//  public static IsEqualToGamesSessionData equalToUserData(GamesSessionData user) {
//    return new IsEqualToGamesSessionData(user);
//  }
//
  @Factory
  public static Matcher<Iterable<? extends GamesSessionVersion>> containsGamesSessionVersionInAnyOrder(GamesSessionVersionData ... users) {
    return containsEntityInAnyOrder(IsEqualToGamesSessionVersionData::equalToGamesSessionVersion, users);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GamesSessionVersion>> containsGamesSessionVersionInAnyOrder(Iterable<GamesSessionVersionData> users) {
    return containsEntityInAnyOrder(IsEqualToGamesSessionVersionData::equalToGamesSessionVersion, users);
  }
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
