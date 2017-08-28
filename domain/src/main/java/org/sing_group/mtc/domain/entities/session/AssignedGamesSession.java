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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.sing_group.mtc.domain.entities.session.AssignedGamesSession.AssignedSessionId;
import org.sing_group.mtc.domain.entities.user.Patient;

@Entity
@Table(name = "assigned_session")
@IdClass(AssignedSessionId.class)
public class AssignedGamesSession implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "sessionId", insertable = false, updatable = false)
  private Integer sessionId;
  
  @Id
  @Column(name = "sessionVersion", insertable = false, updatable = false)
  private int sessionVersion;
  
  @Id
  @Column(name = "patientId", insertable = false, updatable = false)
  private int patientId;
  
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(name = "sessionId", referencedColumnName = "sessionId", nullable = false),
    @JoinColumn(name = "sessionVersion", referencedColumnName = "version", nullable = false)
  })
  private GamesSessionVersion session;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patientId", referencedColumnName = "id", nullable = false)
  private Patient patient;
  
  public GamesSessionVersion getSession() {
    return session;
  }

  public void setSession(GamesSessionVersion session) {
    if (this.session != null) {
      this.session.directRemoveAssigned(this);
      this.session = null;
      this.sessionId = null;
    }
    
    if (session != null) {
      this.session = session;
      this.sessionId = session.getSession().getId();
      this.session.directAddAssigned(this);
    }
  }

  public Patient getPatient() {
    return patient;
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

  public final static class AssignedSessionId implements Serializable {
    private static final long serialVersionUID = 1L;
  
    private int sessionId;
    
    private int sessionVersion;
    
    private int patientId;
    
    private Date assignmentDate;

    public int getPatientId() {
      return patientId;
    }

    public int getSessionId() {
      return sessionId;
    }
    
    public int getSessionVersion() {
      return sessionVersion;
    }
    
    public Date getAssignmentDate() {
      return assignmentDate;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((assignmentDate == null) ? 0 : assignmentDate.hashCode());
      result = prime * result + patientId;
      result = prime * result + sessionId;
      result = prime * result + sessionVersion;
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
      AssignedSessionId other = (AssignedSessionId) obj;
      if (assignmentDate == null) {
        if (other.assignmentDate != null)
          return false;
      } else if (!assignmentDate.equals(other.assignmentDate))
        return false;
      if (patientId != other.patientId)
        return false;
      if (sessionId != other.sessionId)
        return false;
      if (sessionVersion != other.sessionVersion)
        return false;
      return true;
    }
  }
}
