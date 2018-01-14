/*-
 * #%L
 * Domain
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

package org.sing_group.mtc.domain.entities.user;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
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

import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;

/**
 * A patient.
 * 
 * @author Miguel Reboiro-Jato
 */
@Entity
@Table(name = "patient")
@DiscriminatorValue("PATIENT")
public class Patient extends User implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "therapist",
    referencedColumnName = "login",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_patient_therapist")
  )
  private Therapist therapist;

  @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AssignedGamesSession> assignedGamesSessions;

  // Required for JPA
  Patient() {}

  public Patient(String login, String password, Therapist therapist) {
    this(login, password, therapist, null);
  }

  public Patient(String login, String password, Therapist therapist, boolean encodedPassword) {
    this(login, password, therapist, null, encodedPassword);
  }

  public Patient(String login, String password, Therapist therapist, Collection<AssignedGamesSession> sessions) {
    super(login, password);
    
    this.setTherapist(therapist);
    
    this.assignedGamesSessions = sessions == null ? new HashSet<>() : new HashSet<>(sessions);
  }

  public Patient(String login, String password, Therapist therapist, Collection<AssignedGamesSession> sessions, boolean encodedPassword) {
    super(login, password, encodedPassword);
    
    this.setTherapist(therapist);
    
    this.assignedGamesSessions = sessions == null ? new HashSet<>() : new HashSet<>(sessions);
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
  
  public Stream<AssignedGamesSession> getAssignedGameSessions() {
    return this.assignedGamesSessions.stream();
  }
  
  public boolean hasAssignedGameSession(AssignedGamesSession session) {
    return this.assignedGamesSessions.contains(session);
  }
  
  public boolean addAssignedGameSession(AssignedGamesSession session) {
    requireNonNull(session, "'session' can't be null");
    
    if (this.assignedGamesSessions.add(session)) {
      if (!session.getPatient().equals(Optional.of(this)))
        session.setPatient(this);
      
      return true;
    } else {
      return false;
    }
  }

  public boolean removeAssignedGameSession(AssignedGamesSession session) {
    requireNonNull(session, "'session' can't be null");

    if (this.assignedGamesSessions.remove(session)) {
      if (session.getPatient().equals(Optional.of(this)))
        session.setPatient(null);
      
      return true;
    } else {
      return false;
    }
  }
}
