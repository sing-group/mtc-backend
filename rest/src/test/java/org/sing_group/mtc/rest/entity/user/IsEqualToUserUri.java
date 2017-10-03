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

import java.net.URI;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.ws.rs.core.UriBuilder;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntity;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.domain.entities.user.User;
import org.sing_group.mtc.rest.resource.route.BaseRestPathBuilder;

public class IsEqualToUserUri<T extends User> extends IsEqualToEntity<T, UserUri> {
  private final BiFunction<BaseRestPathBuilder, T, URI> uriBuilder;
  
  public IsEqualToUserUri(T user, BiFunction<BaseRestPathBuilder, T, URI> uriBuilder) {
    super(user);
    
    this.uriBuilder = uriBuilder;
  }

  @Override
  protected boolean matchesSafely(UserUri actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      if (!this.expected.getLogin().equals(actual.getLogin())) {
        this.addDescription(String.format("actual login '%s' is different from expected login '%s'",
          actual.getLogin(), this.expected.getLogin()));
        
        return false;
      } else if (!this.checkUri(actual.getUri())) {
        this.addDescription(String.format("actual URI '%s' does not ends in '%s'",
          actual.getUri().getPath(), this.getExpectedUri().getPath()));
        
        return false;
      } else {
        return true;
      }
    }
  }
  
  private URI getExpectedUri() {
    final UriBuilder uriBuilder = UriBuilder.fromPath("http://localhost");
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    return this.uriBuilder.apply(pathBuilder, expected);
  }
  
  private boolean checkUri(URI actualUri) {
    final URI expectedUri = this.getExpectedUri();
    
    return actualUri.getPath().endsWith(expectedUri.getPath());
  }
  
  @Factory
  public static <T extends User> IsEqualToUserUri<T> equalToUserUri(T user, BiFunction<BaseRestPathBuilder, T, URI> uriBuilder) {
    return new IsEqualToUserUri<T>(user, uriBuilder);
  }
  
  @Factory
  public static IsEqualToUserUri<Patient> equalToPatientUri(Patient patient) {
    return equalToUserUri(patient, (pathBuilder, t) -> pathBuilder.patient(t).build());
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserUri>> containsPatientUrisInAnyOrder(Patient... patients) {
    return containsEntityInAnyOrder(IsEqualToUserUri::equalToPatientUri, patients);
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserUri>> containsPatientUrisInAnyOrder(Iterable<Patient> patients) {
    return containsEntityInAnyOrder(IsEqualToUserUri::equalToPatientUri, patients);
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserUri>> containsPatientUrisInAnyOrder(Stream<Patient> patients) {
    return containsEntityInAnyOrder(IsEqualToUserUri::equalToPatientUri, patients);
  }
  
  @Factory
  public static IsEqualToUserUri<Therapist> equalToTherapistUri(Therapist therapist) {
    return equalToUserUri(therapist, (pathBuilder, t) -> pathBuilder.therapist(t).build());
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserUri>> containsTherapistUrisInAnyOrder(Therapist... therapists) {
    return containsEntityInAnyOrder(IsEqualToUserUri::equalToTherapistUri, therapists);
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserUri>> containsTherapistUrisInAnyOrder(Iterable<Therapist> therapists) {
    return containsEntityInAnyOrder(IsEqualToUserUri::equalToTherapistUri, therapists);
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserUri>> containsTherapistUrisInAnyOrder(Stream<Therapist> therapists) {
    return containsEntityInAnyOrder(IsEqualToUserUri::equalToTherapistUri, therapists);
  }
  
  @Factory
  public static IsEqualToUserUri<Manager> equalToManagerUri(Manager manager) {
    return equalToUserUri(manager, (pathBuilder, t) -> pathBuilder.manager(t).build());
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserUri>> containsManagerUrisInAnyOrder(Manager... managers) {
    return containsEntityInAnyOrder(IsEqualToUserUri::equalToManagerUri, managers);
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserUri>> containsManagerUrisInAnyOrder(Iterable<Manager> managers) {
    return containsEntityInAnyOrder(IsEqualToUserUri::equalToManagerUri, managers);
  }
  
  @Factory
  public static Matcher<Iterable<? extends UserUri>> containsManagerUrisInAnyOrder(Stream<Manager> managers) {
    return containsEntityInAnyOrder(IsEqualToUserUri::equalToManagerUri, managers);
  }
}
