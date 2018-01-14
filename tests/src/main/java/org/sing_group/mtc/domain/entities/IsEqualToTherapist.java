/*-
 * #%L
 * Tests
 * %%
 * Copyright (C) 2017 - 2018 Miguel Reboiro-Jato, Adolfo Piñón Blanco,
 *     Hugo López-Fernández, Rosalía Laza Fidalgo, Reyes Pavón Rial,
 *     Francisco Otero Lamas, Adrián Varela Pomar, Carlos Spuch Calvar,
 *     and Tania Rivera Baltanás
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
import org.sing_group.mtc.domain.entities.user.Therapist;

public class IsEqualToTherapist extends IsEqualToEntityOfSameType<Therapist> {
  public IsEqualToTherapist(Therapist owner) {
    super(owner);
  }

  @Override
  protected boolean matchesSafely(Therapist actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("login", Therapist::getLogin, actual)
        && checkAttribute("password", user -> user.getPassword().toUpperCase(), actual);
    }
  }

  @Factory
  public static IsEqualToTherapist equalToTherapist(Therapist owner) {
    return new IsEqualToTherapist(owner);
  }

  @Factory
  public static Matcher<Iterable<? extends Therapist>> containsTherapistsInAnyOrder(Therapist... owners) {
    return containsEntityInAnyOrder(IsEqualToTherapist::equalToTherapist, owners);
  }

  @Factory
  public static Matcher<Iterable<? extends Therapist>> containsTherapistsInAnyOrder(Iterable<Therapist> owners) {
    return containsEntityInAnyOrder(IsEqualToTherapist::equalToTherapist, owners);
  }
}
