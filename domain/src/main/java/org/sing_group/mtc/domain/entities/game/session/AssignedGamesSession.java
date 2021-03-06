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

package org.sing_group.mtc.domain.entities.game.session;

import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;
import static javax.persistence.GenerationType.IDENTITY;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;
import static org.sing_group.fluent.checker.Checks.requireSameTimeOrAfter;
import static org.sing_group.fluent.checker.Checks.requireSameTimeOrBefore;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
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

import org.sing_group.mtc.domain.entities.user.Patient;

@Entity
@Table(name = "assigned_games_session")
public class AssignedGamesSession implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  
  @Column(name = "assignmentDate", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date assignmentDate;
  
  @Column(name = "startDate", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date startDate;
  
  @Column(name = "endDate", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date endDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "gamesSession",
    referencedColumnName = "id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_assignedsession_gamesession")
  )
  private GamesSession gamesSession;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "patient",
    referencedColumnName = "login",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_assignedsession_patient")
  )
  private Patient patient;
  
  @OneToMany(mappedBy = "assignedGamesSession", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GameResult> gameResults;
  
  AssignedGamesSession() {}
  
  AssignedGamesSession(
    Long id, Date assignmentDate, Date startDate, Date endDate, GamesSession session, Patient patient,
    Set<GameResult> gameResults
  ) {
    this.id = id;
    this.assignmentDate = removeTime(assignmentDate);
    this.startDate = removeTime(startDate);
    this.endDate = removeTime(endDate);
    this.setGamesSession(session);
    this.setPatient(patient);
    this.gameResults = new HashSet<>();
    gameResults.forEach(this::addGameResult);
  }
  
  public AssignedGamesSession(long id, Date startDate, Date endDate) {
    this.id = id;
    this.startDate = removeTime(startDate);
    this.endDate = removeTime(endDate);
  }

  public AssignedGamesSession(Date startDate, Date endDate, GamesSession session, Patient patient) {
    this(null, startDate, endDate, session, patient);
  }

  public AssignedGamesSession(Long id, Date startDate, Date endDate, GamesSession session, Patient patient) {
    this(id, new Date(), startDate, endDate, session, patient, emptySet());
  }

  public Long getId() {
    return id;
  }

  public String getTherapistLogin() {
    return this.getGamesSession()
      .map(GamesSession::getTherapistLogin)
    .orElseThrow(() -> new IllegalStateException("games session is not assigned"));
  }
  
  public String getPatientLogin() {
    return this.patient.getLogin();
  }
  
  public Optional<GamesSession> getGamesSession() {
    return Optional.ofNullable(gamesSession);
  }

  public void setGamesSession(GamesSession session) {
    if (this.gamesSession != null) {
      this.gamesSession.directRemoveAssignedGamesSession(this);
      this.gamesSession = null;
    }
    
    if (session != null) {
      this.gamesSession = session;
      this.gamesSession.directAddAssignedGamesSession(this);
    }
  }

  public GameInGamesSession getGame(int gameIndex) {
    return this.gamesSession.getGameConfiguration(gameIndex);
  }

  public int getGameIndex(GameInGamesSession orElse) {
    return this.gamesSession.getGameConfigurationIndex(orElse);
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
    startDate = removeTime(startDate);
    
    requireSameTimeOrAfter(startDate, this.assignmentDate, "startDate should be at the same time or after assignmentDate");
    
    if (this.endDate != null)
      requireSameTimeOrBefore(startDate, this.endDate, "startDate should be at the same time or before endDate");
    
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }
  
  public void setEndDate(Date endDate) {
    endDate = removeTime(endDate);
    
    requireSameTimeOrAfter(endDate, this.startDate, "endDate should be at the same time or after startDate");
    
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
      gameResult.setAssignedGamesSession(this);
      return true;
    }
  }
  
  public boolean removeGameResult(GameResult gameResult) {
    requireNonNull(gameResult, "gameResult can't be null");
    
    if (this.hasGameResult(gameResult)) {
      gameResult.setAssignedGamesSession(null);
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
  
  private Date removeTime(Date date) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    
    
    return calendar.getTime();
  }

  public GameResult addGameResult(
    GameInGamesSession game, Date startDate, Date endDate, Map<String, String> results
  ) {
    if (!this.hasGame(game)) {
      throw new IllegalArgumentException("game does not belong to this session");
    }
    requireNonEmpty(results);
    
    final int countAttempts = (int) this.getResultsForGame(game).count();
    
    return new GameResult(countAttempts + 1, startDate, endDate, this, game, results);
  }
  
  private Stream<GameResult> getResultsForGame(GameInGamesSession game) {
    if (!this.hasGame(game)) {
      throw new IllegalArgumentException("game does not belong to this session");
    }
    
    return this.gameResults.stream()
      .filter(result ->  result.getGameConfiguration().map(game::equals).orElse(false));
  }
  
  private boolean hasGame(GameInGamesSession game) {
    return this.gamesSession.hasGameConfiguration(game);
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
