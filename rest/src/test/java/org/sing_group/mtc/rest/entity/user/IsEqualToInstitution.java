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
import org.sing_group.mtc.domain.entities.user.Institution;

public class IsEqualToInstitution extends IsEqualToEntity<Institution, InstitutionData> {
  public IsEqualToInstitution(Institution user) {
    super(user);
  }

  @Override
  protected boolean matchesSafely(InstitutionData actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("id", Institution::getId, InstitutionData::getId, actual)
        && checkAttribute("name", Institution::getName, InstitutionData::getName, actual)
        && checkAttribute("description", unwrapOptionalFuncion(Institution::getDescription), InstitutionData::getDescription, actual)
        && checkAttribute("address", unwrapOptionalFuncion(Institution::getAddress), InstitutionData::getAddress, actual);
    }
  }
  
  @Factory
  public static IsEqualToInstitution equalToInstitution(Institution admin) {
    return new IsEqualToInstitution(admin);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InstitutionData>> containsInstitutionsInAnyOrder(Institution... institutions) {
    return containsEntityInAnyOrder(IsEqualToInstitution::equalToInstitution, institutions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InstitutionData>> containsInstitutionsInAnyOrder(Iterable<Institution> institutions) {
    return containsEntityInAnyOrder(IsEqualToInstitution::equalToInstitution, institutions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InstitutionData>> containsInstitutionsInAnyOrder(Stream<Institution> institutions) {
    return containsEntityInAnyOrder(IsEqualToInstitution::equalToInstitution, institutions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InstitutionData>> containsInstitutionsInOrder(Institution... institutions) {
    return containsEntityInOrder(IsEqualToInstitution::equalToInstitution, institutions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InstitutionData>> containsInstitutionsInOrder(Iterable<Institution> institutions) {
    return containsEntityInOrder(IsEqualToInstitution::equalToInstitution, institutions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends InstitutionData>> containsInstitutionsInOrder(Stream<Institution> institutions) {
    return containsEntityInOrder(IsEqualToInstitution::equalToInstitution, institutions);
  }
}
