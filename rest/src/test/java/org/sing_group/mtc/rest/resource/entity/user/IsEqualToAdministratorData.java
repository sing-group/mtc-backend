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
package org.sing_group.mtc.rest.resource.entity.user;

import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntityOfSameType;
import org.sing_group.mtc.rest.resource.entity.user.AdministratorData;

public class IsEqualToAdministratorData extends IsEqualToEntityOfSameType<AdministratorData> {
  public IsEqualToAdministratorData(AdministratorData user) {
    super(user);
  }

  @Override
  protected boolean matchesSafely(AdministratorData actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("login", AdministratorData::getLogin, actual)
        && checkAttribute("email", AdministratorData::getEmail, actual)
        && checkAttribute("name", AdministratorData::getName, actual)
        && checkAttribute("surname", AdministratorData::getSurname, actual)
        && checkAttribute("role", AdministratorData::getRole, actual);
    }
  }
  
  @Factory
  public static IsEqualToAdministratorData equalToUserData(AdministratorData user) {
    return new IsEqualToAdministratorData(user);
  }
  
  @Factory
  public static Matcher<Iterable<? extends AdministratorData>> containsUserDatasInAnyOrder(AdministratorData... users) {
    return containsEntityInAnyOrder(IsEqualToAdministratorData::equalToUserData, users);
  }
  
  @Factory
  public static Matcher<Iterable<? extends AdministratorData>> containsUserDatasInAnyOrder(Iterable<AdministratorData> users) {
    return containsEntityInAnyOrder(IsEqualToAdministratorData::equalToUserData, users);
  }
  
  @Factory
  public static Matcher<Iterable<? extends AdministratorData>> containsUserDatasInAnyOrder(Stream<AdministratorData> users) {
    return containsEntityInAnyOrder(IsEqualToAdministratorData::equalToUserData, users);
  }
}
