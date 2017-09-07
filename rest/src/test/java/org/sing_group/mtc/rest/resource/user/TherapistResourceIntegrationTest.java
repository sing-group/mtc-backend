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
package org.sing_group.mtc.rest.resource.user;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.sing_group.mtc.domain.entities.UsersDataset.MANAGER_HTTP_BASIC_AUTH;
import static org.sing_group.mtc.domain.entities.UsersDataset.THERAPIST_HTTP_BASIC_AUTH;
import static org.sing_group.mtc.domain.entities.UsersDataset.modifiedTherapist;
import static org.sing_group.mtc.domain.entities.UsersDataset.newPasswordOf;
import static org.sing_group.mtc.domain.entities.UsersDataset.newTherapist;
import static org.sing_group.mtc.domain.entities.UsersDataset.passwordOf;
import static org.sing_group.mtc.domain.entities.UsersDataset.therapist;
import static org.sing_group.mtc.domain.entities.UsersDataset.therapistToDelete;
import static org.sing_group.mtc.domain.entities.UsersDataset.therapists;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.newGamesSession;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeader;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasCreatedStatus;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.mtc.rest.entity.GenericTypes.TherapistDataListType.THERAPIST_DATA_LIST_TYPE;
import static org.sing_group.mtc.rest.entity.game.session.GamesSessionDataDataset.newGamesSessionData;
import static org.sing_group.mtc.rest.entity.mapper.UserMapper.toEditionData;
import static org.sing_group.mtc.rest.entity.user.IsEqualToTherapist.containsTherapistsInAnyOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToTherapist.equalToTherapist;

import java.util.List;
import java.util.stream.Stream;

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
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.session.GamesSessionCreationData;
import org.sing_group.mtc.rest.entity.user.TherapistData;
import org.sing_group.mtc.rest.entity.user.TherapistEditionData;
import org.sing_group.mtc.rest.resource.Deployments;

@RunWith(Arquillian.class)
public class TherapistResourceIntegrationTest {
  private static final String BASE_PATH = "api/therapist/";

  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }

  @Test
  @InSequence(0)
  @UsingDataSet("users.xml")
  public void beforeGet() {}

  @Test
  @InSequence(1)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGet(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Therapist therapist = therapist();
    
    final Response response = webTarget.path(therapist.getLogin())
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    
    final TherapistData userData = response.readEntity(TherapistData.class);
    
    assertThat(userData, is(equalToTherapist(therapist)));
  }

  @Test
  @InSequence(2)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGet() {}

  @Test
  @InSequence(10)
  @UsingDataSet("users.xml")
  public void beforeList() {}

  @Test
  @InSequence(11)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testList(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Stream<Therapist> therapists = therapists();
    
    final Response response = webTarget
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    
    final List<TherapistData> userData = response.readEntity(THERAPIST_DATA_LIST_TYPE);
    
    assertThat(userData, containsTherapistsInAnyOrder(therapists));
  }

  @Test
  @InSequence(12)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterList() {}

  @Test
  @InSequence(20)
  @UsingDataSet("users.xml")
  public void beforeCreate() {}

  @Test
  @InSequence(21)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testCreate(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Therapist newAdmin = newTherapist();
    final TherapistEditionData userData = toEditionData(newAdmin, passwordOf(newAdmin));
    
    final Response response = webTarget
      .request()
    .post(json(userData));
    
    assertThat(response, hasCreatedStatus());
    assertThat(response, hasHttpHeader("Location", value -> value.endsWith(newAdmin.getLogin())));
  }

  @Test
  @InSequence(22)
  @ShouldMatchDataSet({ "users.xml", "users-create-therapist.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterCreate() {}
  
  @Test
  @InSequence(30)
  @UsingDataSet("users.xml")
  public void beforeUpdate() {}

  @Test
  @InSequence(31)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testUpdate(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Therapist modifiedAdmin = modifiedTherapist();
    final TherapistEditionData userData = toEditionData(modifiedAdmin, newPasswordOf(modifiedAdmin));
    
    final Response response = webTarget
      .request()
    .put(json(userData));
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(32)
  @ShouldMatchDataSet(value = "users-modify-therapist.xml", orderBy = "user.login")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterUpdate() {}

  @Test
  @InSequence(40)
  @UsingDataSet("users.xml")
  public void beforeDelete() {}

  @Test
  @InSequence(41)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testDelete(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Therapist therapist = therapistToDelete();
    
    final Response response = webTarget.path(therapist.getLogin())
      .request()
    .delete();
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(42)
  @ShouldMatchDataSet(value = "users-delete-therapist.xml", orderBy = "user.login")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDelete() {}

  @Test
  @InSequence(100)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  public void beforeCreateGamesSession() {}

  @Test
  @InSequence(101)
  @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testCreateGamesSession(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final GamesSessionCreationData newSessionData = newGamesSessionData();
    final GamesSession expectedSession = newGamesSession();
    
    final Response response = webTarget.path("gamesession")
      .request()
    .post(json(newSessionData));
    
    assertThat(response, hasCreatedStatus());
    assertThat(response, hasHttpHeader("Location", value -> value.endsWith(expectedSession.getId().toString())));
  }

  @Test
  @InSequence(102)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml", "games-sessions-create.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterCreateGamesSession() {}

}
