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

import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntityOfSameType;
import org.sing_group.mtc.domain.entities.user.User;
import org.sing_group.mtc.rest.resource.entity.UserData;

public class IsEqualToUserData extends IsEqualToEntityOfSameType<UserData> {
  public IsEqualToUserData(UserData owner) {
    super(owner);
  }

  @Override
  protected boolean matchesSafely(UserData actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("id", UserData::getId, actual)
        && checkAttribute("email", UserData::getEmail, actual)
        && checkAttribute("name", UserData::getName, actual)
        && checkAttribute("surname", UserData::getSurname, actual)
        && checkAttribute("role", UserData::getRole, actual);
    }
  }

  @Factory
  public static IsEqualToUserData equalToUser(User user) {
    return equalToUserData(UserData.of(user));
  }
  
  @Factory
  public static IsEqualToUserData equalToUserData(UserData user) {
    return new IsEqualToUserData(user);
  }

  @Factory
  public static Matcher<Iterable<? extends UserData>> containsUsersInAnyOrder(User... users) {
    return containsEntityInAnyOrder(IsEqualToUserData::equalToUser, users);
  }

  @Factory
  public static Matcher<Iterable<? extends UserData>> containsUsersInAnyOrder(Iterable<User> users) {
    return containsEntityInAnyOrder(IsEqualToUserData::equalToUser, users);
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserData>> containsUsersInAnyOrder(Stream<User> users) {
    return containsEntityInAnyOrder(IsEqualToUserData::equalToUser, users);
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserData>> containsUserDatasInAnyOrder(UserData... users) {
    return containsEntityInAnyOrder(IsEqualToUserData::equalToUserData, users);
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserData>> containsUserDatasInAnyOrder(Iterable<UserData> users) {
    return containsEntityInAnyOrder(IsEqualToUserData::equalToUserData, users);
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserData>> containsUserDatasInAnyOrder(Stream<UserData> users) {
    return containsEntityInAnyOrder(IsEqualToUserData::equalToUserData, users);
  }
}
