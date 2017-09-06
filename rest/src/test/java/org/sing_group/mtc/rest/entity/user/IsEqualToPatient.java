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
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.rest.entity.user.PatientData;

public class IsEqualToPatient extends IsEqualToEntity<Patient, PatientData> {
  public IsEqualToPatient(Patient user) {
    super(user);
  }

  @Override
  protected boolean matchesSafely(PatientData actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("login", Patient::getLogin, PatientData::getLogin, actual);
    }
  }
  
  @Factory
  public static IsEqualToPatient equalToPatient(Patient patient) {
    return new IsEqualToPatient(patient);
  }
  
  @Factory
  public static Matcher<Iterable<? extends PatientData>> containsPatientsInAnyOrder(Patient... patients) {
    return containsEntityInAnyOrder(IsEqualToPatient::equalToPatient, patients);
  }
  
  @Factory
  public static Matcher<Iterable<? extends PatientData>> containsPatientsInAnyOrder(Iterable<Patient> patients) {
    return containsEntityInAnyOrder(IsEqualToPatient::equalToPatient, patients);
  }
  
  @Factory
  public static Matcher<Iterable<? extends PatientData>> containsPatientsInAnyOrder(Stream<Patient> patients) {
    return containsEntityInAnyOrder(IsEqualToPatient::equalToPatient, patients);
  }
}
