/*
 * #%L
 * Service
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
package org.sing_group.mtc.service.session;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.spi.session.GamesSessionDAO;
import org.sing_group.mtc.domain.entities.session.GamesSession;
import org.sing_group.mtc.service.security.SecurityGuard;
import org.sing_group.mtc.service.spi.session.GamesSessionService;

@Stateless
@Default
@RolesAllowed("THERAPIST")
public class DefaultGamesSessionService implements GamesSessionService {
  @Inject
  private GamesSessionDAO sessionDao;
  
  @Inject
  private SecurityGuard securityManager;
  
  @Override
  public GamesSession createSession(GamesSession gameSession) {
    gameSession.setTherapist(this.securityManager.getLoggedUser());
    
    return this.sessionDao.persist(gameSession);
  }

  @Override
  public GamesSession get(int sessionId) {
    return this.sessionDao.get(sessionId);
  }
}
