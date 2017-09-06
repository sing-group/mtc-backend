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
package org.sing_group.mtc.domain.entities.game.session;

import static java.util.Objects.requireNonNull;
import static javax.persistence.GenerationType.IDENTITY;
import static org.sing_group.fluent.checker.Checks.requireAfter;
import static org.sing_group.fluent.checker.Checks.requireBefore;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.sing_group.mtc.domain.entities.user.Patient;

@Entity
@Table(
  name = "assigned_session",
  uniqueConstraints = @UniqueConstraint(columnNames = { "session", "patient", "assignmentDate" })
)
public class AssignedGamesSession implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;
  
  @Column(name = "assignmentDate", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date assignmentDate;
  
  @Column(name = "startDate", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date startDate;
  
  @Column(name = "endDate", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "session",
    referencedColumnName = "id",
    nullable = false, insertable = false, updatable = false,
    foreignKey = @ForeignKey(name = "FK_assignedsession_gamesession")
  )
  private GamesSession session;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "patient",
    referencedColumnName = "login",
    nullable = false, insertable = false, updatable = false,
    foreignKey = @ForeignKey(name = "FK_assignedsession_patient")
  )
  private Patient patient;
  
  @OneToMany(mappedBy = "assignedSession", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GameResult> gameResults;
  

  public AssignedGamesSession(Date startDate, Date endDate, GamesSession session, Patient patient) {
    this(null, startDate, endDate, session, patient);
  }

  public AssignedGamesSession(Integer id, Date startDate, Date endDate, GamesSession session, Patient patient) {
    this.id = id;
    
    this.assignmentDate = new Date();
    this.setStartDate(startDate);
    this.setEndDate(endDate);
    this.setSession(session);
    this.setPatient(patient);
  }

  public Integer getId() {
    return id;
  }
  
  public Optional<GamesSession> getSession() {
    return Optional.ofNullable(session);
  }

  public void setSession(GamesSession session) {
    if (this.session != null) {
      this.session.directRemoveAssigned(this);
      this.session = null;
    }
    
    if (session != null) {
      this.session = session;
      this.session.directAddAssigned(this);
    }
  }

  public Optional<Patient> getPatient() {
    return Optional.ofNullable(patient);
  }

  public void setPatient(Patient patient) {
    if (this.patient != null) {
      this.patient.removeAssignedGameSession(this);
      this.patient = null;
    }
    
    if (patient != null) {
      this.patient = patient;
      this.patient.addAssignedGameSession(this);
    }
  }

  public Date getAssignmentDate() {
    return assignmentDate;
  }

  public Date getStartDate() {
    return startDate;
  }
  
  public void setStartDate(Date startDate) {
    requireAfter(startDate, this.assignmentDate, "startDate should be after assignmentDate");
    
    if (this.endDate != null)
      requireBefore(startDate, this.endDate, "startDate should be before endDate");
    
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }
  
  public void setEndDate(Date endDate) {
    requireAfter(endDate, this.startDate, "endDate should be after startDate");
    
    this.endDate = endDate;
  }
  
  public Stream<GameResult> getGameResults() {
    return gameResults.stream();
  }
  
  public boolean hasGameResult(GameResult gameResult) {
    return this.gameResults.contains(gameResult);
  }
  
  public boolean addGameResult(GameResult gameResult) {
    requireNonNull(gameResult, "gameResult can't be null");
    
    if (this.hasGameResult(gameResult)) {
      return false;
    } else {
      gameResult.setAssignedSession(this);
      return true;
    }
  }
  
  public boolean removeGameResult(GameResult gameResult) {
    requireNonNull(gameResult, "gameResult can't be null");
    
    if (this.hasGameResult(gameResult)) {
      gameResult.setAssignedSession(null);
      return true;
    } else {
      return true;
    }
  }

  protected boolean directRemoveGameResult(GameResult gameResult) {
    return this.gameResults.remove(gameResult);
  }

  protected boolean directAddGameResult(GameResult gameResult) {
    return this.gameResults.remove(gameResult);
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AssignedGamesSession other = (AssignedGamesSession) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
