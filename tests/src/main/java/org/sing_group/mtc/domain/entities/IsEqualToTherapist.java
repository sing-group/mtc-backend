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
import org.sing_group.mtc.domain.entities.user.Patient;

public class IsEqualToTherapist extends IsEqualToEntityOfSameType<Patient> {
  public IsEqualToTherapist(Patient owner) {
    super(owner);
  }

  @Override
  protected boolean matchesSafely(Patient actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("login", Patient::getLogin, actual)
        && checkAttribute("password", user -> user.getPassword().toUpperCase(), actual);
    }
  }

  @Factory
  public static IsEqualToTherapist equalToPatient(Patient owner) {
    return new IsEqualToTherapist(owner);
  }

  @Factory
  public static Matcher<Iterable<? extends Patient>> containsPatientsInAnyOrder(Patient... owners) {
    return containsEntityInAnyOrder(IsEqualToTherapist::equalToPatient, owners);
  }

  @Factory
  public static Matcher<Iterable<? extends Patient>> containsPatientsInAnyOrder(Iterable<Patient> owners) {
    return containsEntityInAnyOrder(IsEqualToTherapist::equalToPatient, owners);
  }
}
