/*
 * #%L
 * REST
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
package org.sing_group.mtc.rest.resource;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.sing_group.mtc.domain.entities.UsersDataset.THERAPIST_HTTP_BASIC_AUTH;
import static org.sing_group.mtc.domain.entities.session.GamesSessionDataset.sessions;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeader;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasCreatedStatus;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.mtc.rest.resource.entity.GamesSessionDataDataset.newGamesSessionData;
import static org.sing_group.mtc.rest.resource.entity.IsEqualToGamesSessionData.equalToGameSession;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.extension.rest.client.Header;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sing_group.mtc.rest.resource.entity.session.GamesSessionData;

@RunWith(Arquillian.class)
public class GamesSessionResourceIntegrationTest {
  private static final String BASE_PATH = "api/game/session";

  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }

  @Test
  @InSequence(1)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  public void beforeGet() {}

  @Test
  @InSequence(2)
  @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGet(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    sessions().forEach(session -> {
      final Response response = webTarget.path(session.getId().toString())
        .request()
      .get();

      assertThat(response, hasOkStatus());
      
      final GamesSessionData gamesSession = response.readEntity(GamesSessionData.class);
      
      assertThat(session, is(equalToGameSession(gamesSession)));
      
      response.close();
    });
  }

  @Test
  @InSequence(3)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGet() {}

  @Test
  @InSequence(5)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  public void beforeCreate() {}

  @Test
  @InSequence(6)
  @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testCreate(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Response response = webTarget
      .request()
    .post(json(newGamesSessionData()));
    
    assertThat(response, hasCreatedStatus());
    assertThat(response, hasHttpHeader("Location", value -> value.endsWith("game/session/2")));
  }

  @Test
  @InSequence(7)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml", "games-sessions-create.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterCreate() {}
//
//  @Test
//  @InSequence(10)
//  @UsingDataSet("users.xml")
//  public void beforeInvalidEmails() {}
//
//  @Test
//  @InSequence(11)
//  @RunAsClient
//  public void testInvalidEmails(
//    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
//  ) {
//    final Consumer<String> testUnathorized = email -> testUnauthorized(webTarget, email);
//    
//    invalidEmails().forEach(testUnathorized);
//  }
//
//  @Test
//  @InSequence(12)
//  @ShouldMatchDataSet("users.xml")
//  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
//  public void afterInvalidEmails() {}
//  
//  private static void testAuthorized(ResteasyWebTarget webTarget, User expectedUser) {
//    Optional<Response> responseRef = Optional.empty();
//    
//    try {
//      final String password = passwordOfUser(expectedUser);
//      
//      final Response response = webTarget
//        .queryParam("email", expectedUser.getEmail())
//        .queryParam("password", password)
//        .request()
//      .get();
//      
//      assertThat(response, hasOkStatus());
//      
//      final UserData user = response.readEntity(UserData.class);
//      
//      assertThat(user, is(equalToUser(expectedUser)));
//    } finally {
//      responseRef.ifPresent(Response::close);
//    }
//  }
//  
//  private static void testUnauthorized(ResteasyWebTarget webTarget, String email) {
//    testUnauthorized(webTarget, email, false);
//  }
//  
//  private static void testUnauthorized(ResteasyWebTarget webTarget, String email, boolean breakPassword) {
//    Optional<Response> responseRef = Optional.empty();
//    
//    try {
//      final Response response = webTarget.clone()
//        .queryParam("email", email)
//        .queryParam("password", passwordOfUser(email) + (breakPassword ? "break" : ""))
//        .request()
//      .get();
//      
//      responseRef = Optional.of(response);
//      
//      assertThat(response, hasUnauthorizedStatus());
//    } finally {
//      responseRef.ifPresent(Response::close);
//    }
//    
//  }
}
