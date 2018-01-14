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

package org.sing_group.mtc.domain.dao.game.session;

import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.sing_group.mtc.domain.dao.DAOHelper;
import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.spi.game.session.AssignedGamesSessionDAO;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameInGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameResult;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;

public class DefaultAssignedGamesSessionDAO implements AssignedGamesSessionDAO {
  @PersistenceContext
  private EntityManager em;

  private DAOHelper<Long, AssignedGamesSession> dh;

  DefaultAssignedGamesSessionDAO() {}

  public DefaultAssignedGamesSessionDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(Long.class, AssignedGamesSession.class, this.em);
  }

  @Override
  public AssignedGamesSession get(long assignedId) {
    return this.dh.get(assignedId)
      .orElseThrow(() -> new IllegalArgumentException("Unknown assigned games session: " + assignedId));
  }

  @Override
  public Stream<AssignedGamesSession> listByPatient(Patient patient, ListingOptions options) {
    return this.dh.list(options, (cb, root) -> new Predicate[] {
      cb.equal(root.get("patient"), patient)
    }).stream();
  }
  
  @Override
  public Stream<AssignedGamesSession> listByTherapist(Therapist therapist, ListingOptions options) {
    return this.dh.list(options, (cb, root) -> new Predicate[] {
      cb.equal(root.join("gamesSession").get("therapist"), therapist)
    }).stream();
  }

  @Override
  public AssignedGamesSession assignSession(Patient patient, GamesSession gamesSession, Date startDate, Date endDate) {
    final AssignedGamesSession assigned = new AssignedGamesSession(startDate, endDate, gamesSession, patient);
    
    return this.dh.persist(assigned);
  }

  @Override
  public AssignedGamesSession modify(AssignedGamesSession assigned) {
    final AssignedGamesSession persistent = this.get(assigned.getId());
    
    persistent.setEndDate(assigned.getEndDate());
    persistent.setStartDate(assigned.getStartDate());
    
    return persistent;
  }
  
  @Override
  public void delete(long sessionId) {
    this.dh.removeByKey(sessionId);
  }
  
  @Override
  public GameResult addGameResult(
    long assignedSessionId, int gameIndex, Date startDate, Date endDate, Map<String, String> results
  ) {
    final AssignedGamesSession session = this.get(assignedSessionId);
    final GameInGamesSession game = session.getGame(gameIndex);
    final GameResult result = session.addGameResult(game, startDate, endDate, results);
    
    this.em.persist(result);
    
    return result;
  }
}
