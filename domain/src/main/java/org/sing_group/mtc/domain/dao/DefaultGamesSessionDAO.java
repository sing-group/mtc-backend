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
package org.sing_group.mtc.domain.dao;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.mtc.domain.dao.spi.GamesSessionDAO;
import org.sing_group.mtc.domain.entities.session.GamesSession;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultGamesSessionDAO implements GamesSessionDAO {
  @PersistenceContext
  private EntityManager em;

  private DAOHelper<Integer, GamesSession> dh;

  DefaultGamesSessionDAO() {}

  public DefaultGamesSessionDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, GamesSession.class, this.em);
  }

  @Override
  public GamesSession persist(GamesSession session) {
    final GamesSession gamesSession = this.dh.persist(session);
    
    session.getVersions().forEach(version -> {
      version.setSession(gamesSession);
      this.em.persist(version);
    });
    this.em.flush();
    
    session.getVersions().forEach(version -> {
      version.getGameConfigurations().forEach(config -> {
        config.setSession(version);
        this.em.persist(config);
      });
    });
    this.em.flush();
    
    return gamesSession;
  }
  
  @Override
  public GamesSession get(int sessionId) {
    return this.dh.get(sessionId);
  }
}
