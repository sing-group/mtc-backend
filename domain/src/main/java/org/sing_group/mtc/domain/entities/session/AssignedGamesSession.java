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
package org.sing_group.mtc.domain.entities.session;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.sing_group.mtc.domain.entities.session.AssignedGamesSession.AssignedGamesSessionId;
import org.sing_group.mtc.domain.entities.user.Patient;

@Entity
@Table(name = "assigned_session")
@IdClass(AssignedGamesSessionId.class)
public class AssignedGamesSession implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @GeneratedValue(strategy = SEQUENCE)
  @Column(name = "simpleId", unique = true, updatable = false, insertable = false, nullable = false)
  private int simpleId;
  
  @Id
  @Column(name = "assignmentDate")
  @Temporal(TemporalType.TIMESTAMP)
  private Date assignmentDate;
  
  @Column(name = "startDate", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date startDate;
  
  @Column(name = "endDate", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "session",
    referencedColumnName = "id",
    nullable = false, insertable = false, updatable = false,
    foreignKey = @ForeignKey(name = "FK_assignedsession_gamesession")
  )
  private GamesSession session;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "patient",
    referencedColumnName = "login",
    nullable = false, insertable = false, updatable = false,
    foreignKey = @ForeignKey(name = "FK_assignedsession_patient")
  )
  private Patient patient;
  
  public int getSimpleId() {
    return simpleId;
  }
  
  public GamesSession getSession() {
    return session;
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

  public Patient getPatient() {
    return patient;
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

  public Date getEndDate() {
    return endDate;
  }

  public final static class AssignedGamesSessionId implements Serializable {
    private static final long serialVersionUID = 1L;
  
    private int session;
    private String patient;
    private Date assignmentDate;
    
    public AssignedGamesSessionId(int sessionId, String patient, Date assignmentDate) {
      super();
      this.session = sessionId;
      this.patient = patient;
      this.assignmentDate = assignmentDate;
    }

    public String getPatient() {
      return patient;
    }

    public int getSession() {
      return session;
    }
    
    public Date getAssignmentDate() {
      return assignmentDate;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((assignmentDate == null) ? 0 : assignmentDate.hashCode());
      result = prime * result + ((patient == null) ? 0 : patient.hashCode());
      result = prime * result + session;
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
      AssignedGamesSessionId other = (AssignedGamesSessionId) obj;
      if (assignmentDate == null) {
        if (other.assignmentDate != null)
          return false;
      } else if (!assignmentDate.equals(other.assignmentDate))
        return false;
      if (patient == null) {
        if (other.patient != null)
          return false;
      } else if (!patient.equals(other.patient))
        return false;
      if (session != other.session)
        return false;
      return true;
    }
  }
}
