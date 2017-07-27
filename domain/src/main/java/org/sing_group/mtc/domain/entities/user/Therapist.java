/*
 * #%L
 * Domain
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
package org.sing_group.mtc.domain.entities.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.sing_group.mtc.domain.entities.session.Session;

/**
 * A therapist.
 * 
 * @author Miguel Reboiro-Jato
 */
@Entity
@DiscriminatorValue("THERAPIST")
public class Therapist extends User implements Serializable {
  private static final long serialVersionUID = 1L;

  @OneToMany(mappedBy = "therapist", fetch = FetchType.LAZY)
  private Set<Patient> patients;
  
  @OneToMany(mappedBy = "therapist", fetch = FetchType.LAZY)
  private Set<Session> sessions;
  
  // Required for JPA
  Therapist() {}

  /**
   * Creates a new instance of {@code Therapist}.
   * 
   * @param email
   *          the email that identifies the user. This parameter must be a non
   *          empty and non {@code null} string with a maximum length of 100
   *          chars.
   * @param password
   *          the raw password of the user. This parameter must be a non
   *          {@code null} string with a minimum length of 6 chars.
   * 
   * @throws NullPointerException
   *           if a {@code null} value is passed as the value for any parameter.
   * @throws IllegalArgumentException
   *           if value provided for any parameter is not valid according to its
   *           description.
   */
  public Therapist(String email, String password, String name, String surname, boolean encodedPassword) {
    super(email, password, name, surname, encodedPassword);
    
    this.patients = new HashSet<>();
    this.sessions = new HashSet<>();
  }

  public Therapist(Long id, String email, String password, String name, String surname, boolean encodedPassword) {
    super(id, email, password, name, surname, encodedPassword);
    
    this.patients = new HashSet<>();
    this.sessions = new HashSet<>();
  }

  public Therapist(Long id, String email, String password, String name, String surname) {
    super(id, email, password, name, surname);
    
    this.patients = new HashSet<>();
    this.sessions = new HashSet<>();
  }

  public Therapist(String email, String password, String name, String surname) {
    super(email, password, name, surname);
    
    this.patients = new HashSet<>();
    this.sessions = new HashSet<>();
  }
  
  public Stream<Patient> getPatients() {
    return patients.stream();
  }
  
  public Stream<Session> getSessions() {
    return sessions.stream();
  }
}
