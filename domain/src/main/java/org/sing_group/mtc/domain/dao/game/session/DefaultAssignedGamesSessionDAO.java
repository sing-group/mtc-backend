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
package org.sing_group.mtc.domain.dao.game.session;

import java.util.Date;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.sing_group.mtc.domain.dao.DAOHelper;
import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.spi.game.session.AssignedGamesSessionDAO;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;

public class DefaultAssignedGamesSessionDAO implements AssignedGamesSessionDAO {
  @PersistenceContext
  private EntityManager em;

  private DAOHelper<Integer, AssignedGamesSession> dh;

  DefaultAssignedGamesSessionDAO() {}

  public DefaultAssignedGamesSessionDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, AssignedGamesSession.class, this.em);
  }

  @Override
  public AssignedGamesSession get(int assignedId) {
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
      cb.equal(root.join("session").get("therapist"), therapist)
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
  public void delete(int sessionId) {
    this.dh.removeByKey(sessionId);
  }
}
