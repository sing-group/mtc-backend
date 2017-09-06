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
package org.sing_group.mtc.rest.entity.user;

import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntity;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.rest.entity.user.ManagerData;

public class IsEqualToManager extends IsEqualToEntity<Manager, ManagerData> {
  public IsEqualToManager(Manager user) {
    super(user);
  }

  @Override
  protected boolean matchesSafely(ManagerData actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("login", Manager::getLogin, ManagerData::getLogin, actual)
        && checkAttribute("email", Manager::getEmail, ManagerData::getEmail, actual)
        && checkAttribute("name", unwrapOptionalFuncion(Manager::getName), ManagerData::getName, actual)
        && checkAttribute("surname", unwrapOptionalFuncion(Manager::getSurname), ManagerData::getSurname, actual);
    }
  }
  
  @Factory
  public static IsEqualToManager equalToManager(Manager manager) {
    return new IsEqualToManager(manager);
  }
  
  @Factory
  public static Matcher<Iterable<? extends ManagerData>> containsManagersInAnyOrder(Manager... managers) {
    return containsEntityInAnyOrder(IsEqualToManager::equalToManager, managers);
  }
  
  @Factory
  public static Matcher<Iterable<? extends ManagerData>> containsManagersInAnyOrder(Iterable<Manager> managers) {
    return containsEntityInAnyOrder(IsEqualToManager::equalToManager, managers);
  }
  
  @Factory
  public static Matcher<Iterable<? extends ManagerData>> containsManagersInAnyOrder(Stream<Manager> managers) {
    return containsEntityInAnyOrder(IsEqualToManager::equalToManager, managers);
  }
}
