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
package org.sing_group.mtc.service.user;

import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.spi.game.session.GamesSessionDAO;
import org.sing_group.mtc.domain.dao.spi.user.TherapistDAO;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.RoleType;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.service.security.SecurityGuard;
import org.sing_group.mtc.service.spi.user.TherapistService;
import org.sing_group.mtc.service.spi.user.UserService;

@Stateless
@RolesAllowed("MANAGER")
public class DefaultTherapistService implements TherapistService {
  @Inject
  private TherapistDAO dao;
  
  @Inject
  private GamesSessionDAO gamesSessionDao;
  
  @Inject
  private UserService userService;

  @Inject
  private SecurityGuard securityGuard;
  
  @Override
  @RolesAllowed({ "MANAGER", "THERAPIST" })
  public Therapist get(String login) {
    return this.securityGuard.ifAuthorized(RoleType.MANAGER, () -> login, () -> dao.get(login));
  }

  @Override
  public Stream<Therapist> list(ListingOptions listingOptions) {
    return dao.list(listingOptions);
  }

  @Override
  public long count() {
    return this.dao.count();
  }
  
  @Override
  public Therapist create(Therapist therapist) {
    return dao.create(therapist);
  }
  
  @Override
  public Therapist update(Therapist therapist) {
    return dao.update(therapist);
  }

  @Override
  public void delete(String login) {
    dao.delete(login);
  }

  @RolesAllowed("THERAPIST")
  @Override
  public GamesSession createGamesSession(String login, GamesSession gamesSession) {
    return this.securityGuard.ifAuthorized(RoleType.MANAGER, () -> login, () -> {
      gamesSession.setTherapist(this.get(login));
      
      return this.gamesSessionDao.persist(gamesSession);
    });
  }

  @RolesAllowed("THERAPIST")
  @Override
  public Stream<GamesSession> listGameSessions(String login) {
    return this.securityGuard.ifAuthorized(RoleType.MANAGER, () -> login, () -> {
      return ((Therapist) this.userService.getCurrentUser()).getSessions();
    });
  }
}
