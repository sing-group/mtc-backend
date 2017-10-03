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

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sing_group.mtc.domain.entities.game.session.GamesSession;

/**
 * A therapist.
 * 
 * @author Miguel Reboiro-Jato
 */
@Entity
@Table(name = "therapist")
@DiscriminatorValue("THERAPIST")
public class Therapist extends IdentifiedUser {
  private static final long serialVersionUID = 1L;

  @OneToMany(mappedBy = "therapist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<Patient> patients;
  
  @OneToMany(mappedBy = "therapist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<GamesSession> sessions;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
    name = "institution",
    referencedColumnName = "id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_institution_therapist")
  )
  private Institution institution;
  
  // For JPA
  Therapist() {}

  public Therapist(String login, String email, String password, Institution institution) {
    this(login, email, password, institution, null, null, null, null);
  }

  public Therapist(String login, String email, String password, Institution institution, boolean encodedPassword) {
    this(login, email, password, institution, null, null, null, null, encodedPassword);
  }

  public Therapist(
    String login, String email, String password,
    Institution institution,
    String name,
    String surname,
    Collection<Patient> patients,
    Collection<GamesSession> sessions
  ) {
    super(login, email, password, name, surname);
    
    this.patients = patients == null ? new HashSet<>() : new HashSet<>(patients);
    this.sessions = sessions == null ? new HashSet<>() : new HashSet<>(sessions);
    
    this.setInstitution(institution);
  }

  public Therapist(
    String login, String email, String password,
    Institution institution,
    String name,
    String surname,
    Collection<Patient> patients,
    Collection<GamesSession> sessions,
    boolean encodedPassword
  ) {
    super(login, email, password, name, surname, encodedPassword);
    
    this.patients = patients == null ? new HashSet<>() : new HashSet<>(patients);
    this.sessions = sessions == null ? new HashSet<>() : new HashSet<>(sessions);
    
    this.setInstitution(institution);
  }
  
  public void setInstitution(Institution institution) {
    if (this.institution != null) {
      this.institution.directRemoveTherapist(this);
      this.institution = null;
    }
    
    if (institution != null) {
      this.institution = institution;
      this.institution.directAddTherapist(this);
    }
  }
  
  public Institution getInstitution() {
    return institution;
  }

  public Stream<Patient> getPatients() {
    return patients.stream();
  }

  public boolean hasPatient(Patient patient) {
    return this.patients.contains(patient);
  }
  
  public boolean addPatient(Patient patient) {
    if (this.hasPatient(patient)) {
      return false;
    } else {
      patient.setTherapist(this);
      return true;
    }
  }

  public boolean removePatient(Patient patient) {
    if (this.hasPatient(patient)) {
      patient.setTherapist(null);
      return true;
    } else {
      return false;
    }
  }

  protected boolean directRemovePatient(Patient patient) {
    return this.patients.remove(patient);
  }

  protected boolean directAddPatient(Patient patient) {
    return this.patients.add(patient);
  }
  
  public Stream<GamesSession> getSessions() {
    return sessions.stream();
  }
  
  public boolean hasSession(GamesSession session) {
    return this.sessions.contains(session);
  }
  
  public boolean addSession(GamesSession session) {
    requireNonNull(session, "session can't be null");
    
    if (this.sessions.add(session)) {
      if (!session.getTherapist().equals(Optional.of(this)))
        session.setTherapist(this);
      
      return true;
    } else {
      return false;
    }
  }
  
  public boolean removeSession(GamesSession session) {
    requireNonNull(session, "session can't be null");
    
    if (this.sessions.remove(session)) {
      if (session.getTherapist() != null)
        session.setTherapist(null);
      
      return true;
    } else {
      return false;
    }
  }
}
