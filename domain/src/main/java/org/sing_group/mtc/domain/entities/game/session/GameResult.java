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

import static org.sing_group.fluent.checker.Checks.requireAfter;
import static org.sing_group.fluent.checker.Checks.requirePositive;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.sing_group.mtc.domain.entities.game.Game;
import org.sing_group.mtc.domain.entities.game.session.GameResult.GameResultId;
import org.sing_group.mtc.domain.entities.user.Patient;

@Entity
@Table(name = "game_result")
@IdClass(GameResultId.class)
public class GameResult {
  @Id
  private Integer session;

  @Id
  private String game;

  @Id
  private Integer gameOrder;

  @Id
  private Date assignmentDate;

  @Id
  private String patient;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumns(
    value = {
      @JoinColumn(name = "session", referencedColumnName = "session", insertable = false, updatable = false, nullable = false),
      @JoinColumn(name = "patient", referencedColumnName = "patient", insertable = false, updatable = false, nullable = false),
      @JoinColumn(name = "assignmentDate", referencedColumnName = "assignmentDate", insertable = false, updatable = false, nullable = false)
    },
    foreignKey = @ForeignKey(name = "FK_gameresult_assignedsession")
  )
  private AssignedGamesSession assignedSession;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumns(
    value = {
      @JoinColumn(name = "gameOrder", referencedColumnName = "gameOrder", insertable = false, updatable = false, nullable = false),
      @JoinColumn(name = "session", referencedColumnName = "session", insertable = false, updatable = false, nullable = false),
      @JoinColumn(name = "game", referencedColumnName = "game", insertable = false, updatable = false, nullable = false)
    },
    foreignKey = @ForeignKey(name = "FK_gameresult_sessiongame")
  )
  private GameConfigurationForSession gameConfiguration;

  @Column(name = "attempt", nullable = false, length = 2)
  private int attempt;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "start", nullable = false)
  private Date start;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "end", nullable = true)
  private Date end;

  @ElementCollection
  @MapKeyColumn(name = "result", nullable = false)
  @Column(name = "value", nullable = false)
  @CollectionTable(
    name = "game_result_value", joinColumns = {
      @JoinColumn(name = "session", referencedColumnName = "session", nullable = false, updatable = false, insertable = false),
      @JoinColumn(name = "patient", referencedColumnName = "patient", nullable = false, updatable = false, insertable = false),
      @JoinColumn(name = "assignmentDate", referencedColumnName = "assignmentDate", nullable = false, updatable = false, insertable = false),
      @JoinColumn(name = "gameOrder", referencedColumnName = "gameOrder", nullable = false, updatable = false, insertable = false),
      @JoinColumn(name = "game", referencedColumnName = "game", nullable = false, updatable = false, insertable = false)
    },
    foreignKey = @ForeignKey(name = "FK_gameresult_resultvalues")
  )
  private Map<String, String> resultValues;
  
  // For JPA
  GameResult() {}
  
  public GameResult(int attempt) {
    this(attempt, null, null);
  }
  
  public GameResult(int attempt, AssignedGamesSession assignedSession, GameConfigurationForSession gameConfiguration) {
    this.attempt = requirePositive(attempt, "attempt should be a positive number");
    this.start = new Date();
    this.end = null;
    this.resultValues = new HashMap<>();
    
    this.setAssignedSession(assignedSession);
    this.setGameConfiguration(gameConfiguration);
  }

  public int getAttempt() {
    return attempt;
  }

  public Date getStart() {
    return start;
  }

  public Optional<Date> getEnd() {
    return Optional.ofNullable(end);
  }

  public void setEnd(Date end) {
    this.end = requireAfter(end, this.start, "end should be after start");
  }
  
  public Optional<AssignedGamesSession> getAssignedSession() {
    return Optional.ofNullable(assignedSession);
  }
  
  public void setAssignedSession(AssignedGamesSession assignedSession) {
    if (assignedSession != null) {
      if (assignedSession.getSession() == null) {
        throw new IllegalArgumentException("assignedSession should have an assigned session");
      }
      
      if (assignedSession.getAssignmentDate() == null) {
        throw new IllegalArgumentException("assignedSession should have an assignment date");
      }
      
      if (assignedSession.getPatient() == null) {
        throw new IllegalArgumentException("assignedSession should have a patient");
      }
      
      if (this.gameConfiguration != null && !assignedSession.getSession().map(GamesSession::getId).equals(Optional.of(this.session))) {
        throw new IllegalArgumentException("assignedSession should have the same session as the assigned gameConfiguration");
      }
    }
    
    if (this.assignedSession != null) {
      this.assignedSession.directRemoveGameResult(this);
      this.assignedSession = null;
      
      if (this.gameConfiguration == null)
        this.session = null;
      
      this.assignmentDate = null;
      this.patient = null;
    }
    
    if (assignedSession != null) {
      this.assignedSession = assignedSession;
      this.assignedSession.directAddGameResult(this);
      
      this.session = this.assignedSession.getSession()
        .map(GamesSession::getId)
      .orElseThrow(IllegalStateException::new);
      this.assignmentDate = this.assignedSession.getAssignmentDate();
      this.patient = this.assignedSession.getPatient()
        .map(Patient::getLogin)
      .orElseThrow(IllegalStateException::new);
    }
  }
  
  public Optional<GameConfigurationForSession> getGameConfiguration() {
    return Optional.ofNullable(gameConfiguration);
  }
  
  public void setGameConfiguration(GameConfigurationForSession gameConfiguration) {
    if (gameConfiguration != null) {
      if (gameConfiguration.getSession() == null) {
        throw new IllegalArgumentException("gameConfiguration should have an assigned session");
      }
      
      if (gameConfiguration.getGame() == null) {
        throw new IllegalArgumentException("gameConfiguration should have an assignment game");
      }
      
      if (this.assignedSession != null && !gameConfiguration.getSession().getId().equals(this.session)) {
        throw new IllegalArgumentException("gameConfiguration should have the same session as the assigned assignedSession");
      }
    }
    
    this.gameConfiguration = gameConfiguration;
    
    if (this.gameConfiguration == null) {
      this.gameOrder = null;
      this.game = null;
      
      if (this.assignedSession == null)
        this.session = null;
    } else {
      this.gameOrder = this.gameConfiguration.getGameOrder();
      this.game = this.gameConfiguration.getGame().map(Game::getId).orElseThrow(IllegalStateException::new);
      this.session = this.gameConfiguration.getSession().getId();
    }
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((assignedSession == null) ? 0 : assignedSession.hashCode());
    result = prime * result + ((assignmentDate == null) ? 0 : assignmentDate.hashCode());
    result = prime * result + ((game == null) ? 0 : game.hashCode());
    result = prime * result + ((gameOrder == null) ? 0 : gameOrder.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
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
    GameResult other = (GameResult) obj;
    if (assignedSession == null) {
      if (other.assignedSession != null)
        return false;
    } else if (!assignedSession.equals(other.assignedSession))
      return false;
    if (assignmentDate == null) {
      if (other.assignmentDate != null)
        return false;
    } else if (!assignmentDate.equals(other.assignmentDate))
      return false;
    if (game == null) {
      if (other.game != null)
        return false;
    } else if (!game.equals(other.game))
      return false;
    if (gameOrder == null) {
      if (other.gameOrder != null)
        return false;
    } else if (!gameOrder.equals(other.gameOrder))
      return false;
    if (patient == null) {
      if (other.patient != null)
        return false;
    } else if (!patient.equals(other.patient))
      return false;
    return true;
  }



  public static class GameResultId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int session;
    
    private String game;
    
    private int gameOrder;
    
    private Date assignmentDate;
    
    private String patient;
    
    // For JPA
    GameResultId() {}
    
    
    public int getSession() {
      return this.session;
    }

    public String getGame() {
      return this.game;
    }

    public int getGameOrder() {
      return this.gameOrder;
    }

    public Date getAssignmentDate() {
      return assignmentDate;
    }

    public String getPatient() {
      return patient;
    }


    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((assignmentDate == null) ? 0 : assignmentDate.hashCode());
      result = prime * result + ((game == null) ? 0 : game.hashCode());
      result = prime * result + gameOrder;
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
      GameResultId other = (GameResultId) obj;
      if (assignmentDate == null) {
        if (other.assignmentDate != null)
          return false;
      } else if (!assignmentDate.equals(other.assignmentDate))
        return false;
      if (game == null) {
        if (other.game != null)
          return false;
      } else if (!game.equals(other.game))
        return false;
      if (gameOrder != other.gameOrder)
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
