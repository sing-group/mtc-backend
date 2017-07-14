/*
 * #%L
 * Tests
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
package org.sing_group.mtc.domain.entities;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.Patient;

public class IsEqualToPatient extends IsEqualToEntity<Patient> {
  public IsEqualToPatient(Patient owner) {
    super(owner);
  }

  @Override
  protected boolean matchesSafely(Patient actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("email", Patient::getEmail, actual)
        && checkAttribute("name", Patient::getName, actual)
        && checkAttribute("surname", Patient::getSurname, actual)
        && checkAttribute("password", user -> user.getPassword().toUpperCase(), actual);
    }
  }

  @Factory
  public static IsEqualToPatient equalToPatient(Patient owner) {
    return new IsEqualToPatient(owner);
  }

  @Factory
  public static Matcher<Iterable<? extends Patient>> containsPatientsInAnyOrder(Patient... owners) {
    return containsEntityInAnyOrder(IsEqualToPatient::equalToPatient, owners);
  }

  @Factory
  public static Matcher<Iterable<? extends Patient>> containsPatientsInAnyOrder(Iterable<Patient> owners) {
    return containsEntityInAnyOrder(IsEqualToPatient::equalToPatient, owners);
  }
}
