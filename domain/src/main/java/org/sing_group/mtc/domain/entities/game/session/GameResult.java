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

import static javax.persistence.GenerationType.IDENTITY;
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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.sing_group.mtc.domain.entities.game.Game;
import org.sing_group.mtc.domain.entities.user.Patient;

@Entity
@Table(
  name = "game_result",
  uniqueConstraints = @UniqueConstraint(columnNames = { "session", "game", "gameOrder", "assignmentDate", "patient" })
)
public class GameResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;
  
  @Column(name = "session")
  private Integer session;

  @Column(name = "game", length = 255, columnDefinition = "VARCHAR(255)")
  private String game;

  @Column(name = "gameOrder")
  private Integer gameOrder;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "assignmentDate")
  private Date assignmentDate;

  @Column(name = "patient", length = 100, nullable = false, unique = true)
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
  
  public Integer getId() {
    return id;
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
      
      checkSameGameSession(this.assignedSession, gameConfiguration);
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
  
  public Optional<GamesSession> getGamesSession() {
    if (this.gameConfiguration != null) {
      return this.getGameConfiguration().map(GameConfigurationForSession::getSession);
    } else if (this.assignedSession != null) {
      return this.getAssignedSession().map(AssignedGamesSession::getSession).get();
    } else {
      return Optional.empty();
    }
  }
  
  public void setGameConfiguration(GameConfigurationForSession gameConfiguration) {
    if (gameConfiguration != null) {
      if (gameConfiguration.getGame() == null) {
        throw new IllegalArgumentException("gameConfiguration should have an assigned game");
      }
      
      checkSameGameSession(this.assignedSession, gameConfiguration);
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
  
  private static void checkSameGameSession(
    AssignedGamesSession assignedSession,
    GameConfigurationForSession gameConfiguration
  ) {
    if (assignedSession != null && gameConfiguration != null) {
      if (gameConfiguration.getSession() == null) 
        throw new IllegalArgumentException("gameConfiguration should have an assigned session");
      
      if (assignedSession.getSession() == null)
        throw new IllegalArgumentException("assignedSession should have an assigned session");
      
      final int gsIdConfig = gameConfiguration.getSession().getId();
      final int gsIdAssigned = assignedSession.getSession().map(GamesSession::getId).get();
      
      if (gsIdConfig != gsIdAssigned) {
        throw new IllegalArgumentException("gameConfiguration should have the same session as the assignedSession");
      }
    }
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
    GameResult other = (GameResult) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
