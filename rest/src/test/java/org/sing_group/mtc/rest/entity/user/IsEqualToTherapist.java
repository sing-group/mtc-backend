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
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.user.TherapistData;

public class IsEqualToTherapist extends IsEqualToEntity<Therapist, TherapistData> {
  public IsEqualToTherapist(Therapist user) {
    super(user);
  }

  @Override
  protected boolean matchesSafely(TherapistData actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("login", Therapist::getLogin, TherapistData::getLogin, actual)
        && checkAttribute("email", Therapist::getEmail, TherapistData::getEmail, actual)
        && checkAttribute("name", unwrapOptionalFuncion(Therapist::getName), TherapistData::getName, actual)
        && checkAttribute("surname", unwrapOptionalFuncion(Therapist::getSurname), TherapistData::getSurname, actual);
    }
  }
  
  @Factory
  public static IsEqualToTherapist equalToTherapist(Therapist therapist) {
    return new IsEqualToTherapist(therapist);
  }
  
  @Factory
  public static Matcher<Iterable<? extends TherapistData>> containsTherapistsInAnyOrder(Therapist... therapists) {
    return containsEntityInAnyOrder(IsEqualToTherapist::equalToTherapist, therapists);
  }
  
  @Factory
  public static Matcher<Iterable<? extends TherapistData>> containsTherapistsInAnyOrder(Iterable<Therapist> therapists) {
    return containsEntityInAnyOrder(IsEqualToTherapist::equalToTherapist, therapists);
  }
  
  @Factory
  public static Matcher<Iterable<? extends TherapistData>> containsTherapistsInAnyOrder(Stream<Therapist> therapists) {
    return containsEntityInAnyOrder(IsEqualToTherapist::equalToTherapist, therapists);
  }
  
  @Factory
  public static Matcher<Iterable<? extends TherapistData>> containsTherapistsInOrder(Therapist... therapists) {
    return containsEntityInOrder(IsEqualToTherapist::equalToTherapist, therapists);
  }
  
  @Factory
  public static Matcher<Iterable<? extends TherapistData>> containsTherapistsInOrder(Iterable<Therapist> therapists) {
    return containsEntityInOrder(IsEqualToTherapist::equalToTherapist, therapists);
  }
  
  @Factory
  public static Matcher<Iterable<? extends TherapistData>> containsTherapistsInOrder(Stream<Therapist> therapists) {
    return containsEntityInOrder(IsEqualToTherapist::equalToTherapist, therapists);
  }
}
