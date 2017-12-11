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
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.RoleType;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.service.security.SecurityGuard;
import org.sing_group.mtc.service.security.check.SecurityCheck;
import org.sing_group.mtc.service.security.check.SecurityCheckBuilder;
import org.sing_group.mtc.service.spi.user.TherapistService;

@Stateless
@RolesAllowed("MANAGER")
public class DefaultTherapistService implements TherapistService {
  @Inject
  private TherapistDAO dao;
  
  @Inject
  private GamesSessionDAO gamesSessionDao;
  
  @Inject
  private SecurityGuard securityGuard;
  
  @Inject
  private SecurityCheckBuilder checkThat;
  
  private SecurityCheck checkThatIsManagerOf(Therapist therapist) {
    final String managerLogin = therapist.getManager()
      .map(Manager::getLogin)
    .orElse(null);
    
    return checkThat.hasLoginAndRole(managerLogin, RoleType.MANAGER);
  }
  
  private SecurityCheck checkThatIsManagerOf(String therapistLogin) {
    return this.checkThatIsManagerOf(this.dao.get(therapistLogin));
  }
  
  @Override
  @RolesAllowed({ "MANAGER", "THERAPIST" })
  public Therapist get(String login) {
    return this.securityGuard.ifAuthorized(
      checkThat.hasLogin(login),
      checkThatIsManagerOf(login)
    )
    .call(() -> dao.get(login));
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
    return this.securityGuard.ifAuthorized(
      checkThatIsManagerOf(therapist)
    ).call(() -> dao.create(therapist));
  }
  
  @Override
  public Therapist update(Therapist therapist) {
    return this.securityGuard.ifAuthorized(
      checkThatIsManagerOf(therapist.getLogin())
    ).call(() -> dao.update(therapist));
  }

  @Override
  public void delete(String login) {
    this.securityGuard.ifAuthorized(
      checkThatIsManagerOf(login)
    ).run(() -> dao.delete(login));
  }

  @RolesAllowed("THERAPIST")
  @Override
  public GamesSession createGamesSession(String login, GamesSession gamesSession) {
    return this.securityGuard.ifAuthorized(
        checkThat.hasRole(RoleType.MANAGER),
        checkThat.hasLogin(login)
      )
    .call(() -> {
      gamesSession.setTherapist(this.get(login));
      
      return this.gamesSessionDao.persist(gamesSession);
    });
  }

  @RolesAllowed({ "MANAGER", "THERAPIST" })
  @Override
  public Stream<GamesSession> listGameSessions(String login) {
    return this.securityGuard.ifAuthorized(
        checkThat.hasRole(RoleType.MANAGER),
        checkThat.hasLoginAndRole(login, RoleType.THERAPIST)
      )
    .call(() -> this.get(login).getSessions());
  }
}
