/*-
 * #%L
 * REST
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

package org.sing_group.mtc.rest.resource.game.session;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.sing_group.mtc.domain.entities.UsersDataset.PATIENT_HTTP_BASIC_AUTH;
import static org.sing_group.mtc.domain.entities.UsersDataset.THERAPIST_HTTP_BASIC_AUTH;
import static org.sing_group.mtc.domain.entities.UsersDataset.patient;
import static org.sing_group.mtc.domain.entities.UsersDataset.therapist;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.assignedGamesSession;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.assignedGamesSessionToDelete;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.assignedGamesSessionsOfPatient;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.assignedGamesSessionsOfTherapist;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.gamesSessionToDelete;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.modifiedAssignedGamesSessionId;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.modifiedGamesSession;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.sessions;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeader;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeaderContaining;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeaderEndingWith;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.mtc.rest.entity.GenericTypes.AssignedGamesSessionDataListType.ASSIGNED_GAMES_SESSION_DATA_LIST_TYPE;
import static org.sing_group.mtc.rest.entity.game.session.GamesSessionDataDataset.modifiedAssignedGamesSessionData;
import static org.sing_group.mtc.rest.entity.game.session.IsEqualToAssignedGamesSession.containsAssignedGamesSessionsInAnyOrder;
import static org.sing_group.mtc.rest.entity.game.session.IsEqualToAssignedGamesSession.equalToAssignedGamesSession;
import static org.sing_group.mtc.rest.entity.game.session.IsEqualToGamesSessionData.equalToGamesSession;

import java.util.List;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.extension.rest.client.Header;
import org.jboss.arquillian.extension.rest.client.Headers;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionData;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionEditionData;
import org.sing_group.mtc.rest.entity.game.session.GamesSessionData;
import org.sing_group.mtc.rest.entity.game.session.GamesSessionEditionData;
import org.sing_group.mtc.rest.entity.mapper.game.DefaultGamesMapper;
import org.sing_group.mtc.rest.entity.mapper.spi.game.GamesMapper;
import org.sing_group.mtc.rest.resource.Deployments;

@RunWith(Arquillian.class)
public class GamesSessionResourceIntegrationTest {
  private static final String BASE_PATH = "api/games-session";
  
  private GamesMapper gamesMapper;

  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }
  
  @Before
  public void setUp() {
    this.gamesMapper = new DefaultGamesMapper();
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
      
      assertThat(session, is(equalToGamesSession(gamesSession)));
      
      response.close();
    });
  }

  @Test
  @InSequence(3)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGet() {}

  @Test
  @InSequence(4)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  public void beforeModify() {}

  @Test
  @InSequence(5)
  @Headers({
    @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testModify(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final GamesSession modifiedSession = modifiedGamesSession();
    
    final GamesSessionEditionData expected = this.gamesMapper.mapToGameSessionEditionData(modifiedSession);
    
    final Response response = webTarget.path(modifiedSession.getId().toString())
      .request()
    .put(json(expected));
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeaderEndingWith("Location", Long.toString(modifiedSession.getId())));
    assertThat(response, hasHttpHeaderContaining("Access-Control-Expose-Headers", "Location"));
  }

  @Test
  @InSequence(6)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions-modify.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterModify() {}

  @Test
  @InSequence(7)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  public void beforeDelete() {}

  @Test
  @InSequence(8)
  @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testDelete(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Response response = webTarget.path(Integer.toString(gamesSessionToDelete()))
      .request()
    .delete();
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(9)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions-delete.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDelete() {}
  
  private void testGetAssigned(ResteasyWebTarget webTarget) {
    final AssignedGamesSession expected = assignedGamesSession();
    
    final Response response = webTarget.path("assigned").path(expected.getId().toString())
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    
    final AssignedGamesSessionData assignedData = response.readEntity(AssignedGamesSessionData.class);
    
    assertThat(assignedData, is(equalToAssignedGamesSession(expected)));
  }

  @Test
  @InSequence(100)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  public void beforeGetAssignedAsTherapist() {}

  @Test
  @InSequence(101)
  @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGetAssignedAsTherapist(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGetAssigned(webTarget);
  }

  @Test
  @InSequence(102)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetAssignedAsTherapist() {}

  @Test
  @InSequence(103)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  public void beforeGetAssignedAsPatient() {}

  @Test
  @InSequence(104)
  @Header(name = "Authorization", value = PATIENT_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGetAssignedAsPatient(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGetAssigned(webTarget);
  }

  @Test
  @InSequence(105)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetAssignedAsPatient() {}

  @Test
  @InSequence(106)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  public void beforeModifyAssigned() {}

  @Test
  @InSequence(107)
  @Headers({
    @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testModifyAssigned(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final AssignedGamesSessionEditionData data = modifiedAssignedGamesSessionData();
    final String sessionId = Integer.toString(modifiedAssignedGamesSessionId());
    
    final Response response = webTarget.path("assigned").path(sessionId)
      .request()
    .put(json(data));
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeaderEndingWith("Location", sessionId));
    assertThat(response, hasHttpHeaderContaining("Access-Control-Expose-Headers", "Location"));
  }

  @Test
  @InSequence(108)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions-modify.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterModifyAssigned() {}

  @Test
  @InSequence(109)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  public void beforeDeleteAssigned() {}

  @Test
  @InSequence(110)
  @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testDeleteAssigned(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Response response = webTarget.path("assigned").path(Integer.toString(assignedGamesSessionToDelete()))
      .request()
    .delete();
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(111)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions-delete.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDeleteAssigned() {}
  
  private void testListAssigned(ResteasyWebTarget webTarget, AssignedGamesSession[] assignedGamesSessions) {
    final Response response = webTarget.path("assigned")
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("X-Total-Count", assignedGamesSessions.length));
    assertThat(response, hasHttpHeaderContaining("Access-Control-Expose-Headers", "X-Total-Count"));
    
    final List<AssignedGamesSessionData> assignedData = response.readEntity(ASSIGNED_GAMES_SESSION_DATA_LIST_TYPE);
    
    assertThat(assignedData, containsAssignedGamesSessionsInAnyOrder(assignedGamesSessions));
  }

  @Test
  @InSequence(120)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  public void beforeListAssignedAsTherapist() {}

  @Test
  @InSequence(121)
  @Headers({
    @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testListAssignedAsTherapist(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testListAssigned(webTarget, assignedGamesSessionsOfTherapist(therapist().getLogin()));
  }

  @Test
  @InSequence(122)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListAssignedAsTherapist() {}

  @Test
  @InSequence(123)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  public void beforeListAssignedAsPatient() {}

  @Test
  @InSequence(124)
  @Headers({
    @Header(name = "Authorization", value = PATIENT_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testListAssignedAsPatient(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testListAssigned(webTarget, assignedGamesSessionsOfPatient(patient().getLogin()));
  }

  @Test
  @InSequence(125)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListAssignedAsPatient() {}
}
