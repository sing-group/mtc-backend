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
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.sing_group.mtc.domain.entities.session.AssignedGamesSession;

/**
 * A patient.
 * 
 * @author Miguel Reboiro-Jato
 */
@Entity
@DiscriminatorValue("PATIENT")
public class Patient extends User implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "therapistId", referencedColumnName = "id")
  private Therapist therapist;

  @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AssignedGamesSession> assigned;

  // Required for JPA
  Patient() {}

  /**
   * Creates a new instance of {@code Patient}.
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
  public Patient(String email, String password, String name, String surname, Therapist therapist, boolean encodedPassword) {
    super(email, password, name, surname, encodedPassword);
    
    this.setTherapist(therapist);
  }

  public Patient(Integer id, String email, String password, String name, String surname, Therapist therapist, boolean encodedPassword) {
    super(id, email, password, name, surname, encodedPassword);
    
    this.setTherapist(therapist);
  }

  public Patient(Integer id, String email, String password, String name, String surname, Therapist therapist) {
    super(id, email, password, name, surname);
    
    this.setTherapist(therapist);
  }

  public Patient(String email, String password, String name, String surname, Therapist therapist) {
    super(email, password, name, surname);
    
    this.setTherapist(therapist);
  }
  
  public Therapist getTherapist() {
    return therapist;
  }
  
  public void setTherapist(Therapist therapist) {
    if (this.therapist != null) {
      this.therapist.directRemovePatient(this);
      this.therapist = null;
    }
    
    if (therapist != null) {
      this.therapist = therapist;
      this.therapist.directAddPatient(this);
    }
  }
  
  public Stream<AssignedGamesSession> getAssigned() {
    return this.assigned.stream();
  }
}
