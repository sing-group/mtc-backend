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

package org.sing_group.mtc.rest.resource.user;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.sing_group.mtc.domain.entities.UsersDataset.PATIENT_HTTP_BASIC_AUTH;
import static org.sing_group.mtc.domain.entities.UsersDataset.THERAPIST_HTTP_BASIC_AUTH;
import static org.sing_group.mtc.domain.entities.UsersDataset.countPatientsOfTherapist;
import static org.sing_group.mtc.domain.entities.UsersDataset.modifiedPatient;
import static org.sing_group.mtc.domain.entities.UsersDataset.newPasswordOf;
import static org.sing_group.mtc.domain.entities.UsersDataset.newPatient;
import static org.sing_group.mtc.domain.entities.UsersDataset.passwordOf;
import static org.sing_group.mtc.domain.entities.UsersDataset.patient;
import static org.sing_group.mtc.domain.entities.UsersDataset.patientToDelete;
import static org.sing_group.mtc.domain.entities.UsersDataset.patientsOfTherapist;
import static org.sing_group.mtc.domain.entities.UsersDataset.therapist;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.assignedGamesSessionsOfPatient;
import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.newAssignedGamesSession;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeader;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeaderContaining;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeaderEndingWith;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasCreatedStatus;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.mtc.rest.entity.GenericTypes.AssignedGamesSessionDataListType.ASSIGNED_GAMES_SESSION_DATA_LIST_TYPE;
import static org.sing_group.mtc.rest.entity.GenericTypes.PatientDataListType.PATIENT_DATA_LIST_TYPE;
import static org.sing_group.mtc.rest.entity.game.session.IsEqualToAssignedGamesSession.containsAssignedGamesSessionsInAnyOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToPatient.containsPatientsInAnyOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToPatient.containsPatientsInOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToPatient.equalToPatient;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

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
import org.sing_group.mtc.domain.dao.SortDirection;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionCreationData;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionData;
import org.sing_group.mtc.rest.entity.mapper.game.DefaultGamesMapper;
import org.sing_group.mtc.rest.entity.mapper.spi.game.GamesMapper;
import org.sing_group.mtc.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.mtc.rest.entity.mapper.user.DefaultUserMapper;
import org.sing_group.mtc.rest.entity.user.PatientData;
import org.sing_group.mtc.rest.entity.user.PatientEditionData;
import org.sing_group.mtc.rest.resource.Deployments;

@RunWith(Arquillian.class)
public class PatientResourceIntegrationTest {
  private static final String BASE_PATH = "api/patient/";
  
  private UserMapper userMapper;
  
  private GamesMapper gamesMapper;

  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }
  
  @Before
  public void setUp() {
    this.userMapper = new DefaultUserMapper();
    this.gamesMapper = new DefaultGamesMapper();
  }

  @Test
  @InSequence(0)
  @UsingDataSet("users.xml")
  public void beforeGet() {}

  @Test
  @InSequence(1)
  @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGet(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Patient patient = patient();
    
    final Response response = webTarget.path(patient.getLogin())
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    
    final PatientData userData = response.readEntity(PatientData.class);
    
    assertThat(userData, is(equalToPatient(patient)));
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
  @Headers({
    @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testList(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final String therapist = therapist().getLogin();
    final Stream<Patient> patients = patientsOfTherapist(therapist);
    
    final Response response = webTarget
      .request()
      .header("Origin", "localhost")
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("X-Total-Count", countPatientsOfTherapist(therapist)));
    assertThat(response, hasHttpHeaderContaining("Access-Control-Expose-Headers", "X-Total-Count"));
    
    final List<PatientData> userData = response.readEntity(PATIENT_DATA_LIST_TYPE);
    
    assertThat(userData, containsPatientsInAnyOrder(patients));
  }

  @Test
  @InSequence(12)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterList() {}

  @Test
  @InSequence(13)
  @UsingDataSet("users.xml")
  public void beforeListFiltered() {}

  @Test
  @InSequence(14)
  @Headers({
    @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testListFiltered(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final int start = 1;
    final int end = 2;
    final Function<Patient, String> getter = Patient::getLogin;
    final String sortField = "login";
    final SortDirection sortDirection = SortDirection.DESC;
    
    final String therapist = therapist().getLogin();
    final Stream<Patient> patients = patientsOfTherapist(
      therapist,
      start, end, getter, sortDirection
    );
    
    final Response response = webTarget
      .queryParam("start", start)
      .queryParam("end", end)
      .queryParam("sort", sortField)
      .queryParam("order", sortDirection)
      .request()
    .get();
    
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("X-Total-Count", countPatientsOfTherapist(therapist)));
    
    final List<PatientData> patientData = response.readEntity(PATIENT_DATA_LIST_TYPE);
    
    assertThat(patientData, containsPatientsInOrder(patients));
  }

  @Test
  @InSequence(15)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListFiltered() {}

  @Test
  @InSequence(20)
  @UsingDataSet("users.xml")
  public void beforeCreate() {}

  @Test
  @InSequence(21)
  @Headers({
    @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testCreate(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Patient newPatient = newPatient();
    final PatientEditionData userData = userMapper.toCreationData(newPatient, passwordOf(newPatient));
    
    final Response response = webTarget
      .request()
    .post(json(userData));
    
    assertThat(response, hasCreatedStatus());
    assertThat(response, hasHttpHeaderEndingWith("Location", newPatient.getLogin()));
    assertThat(response, hasHttpHeaderContaining("Access-Control-Expose-Headers", "Location"));
  }

  @Test
  @InSequence(22)
  @ShouldMatchDataSet({ "users.xml", "users-create-patient.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterCreate() {}
  
  @Test
  @InSequence(30)
  @UsingDataSet("users.xml")
  public void beforeUpdate() {}

  @Test
  @InSequence(31)
  @Headers({
    @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testUpdate(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Patient modifiedPatient = modifiedPatient();
    final PatientEditionData userData = userMapper.toEditionData(modifiedPatient, newPasswordOf(modifiedPatient));
    
    final Response response = webTarget.path(modifiedPatient.getLogin())
      .request()
    .put(json(userData));
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeaderEndingWith("Location", modifiedPatient.getLogin()));
    assertThat(response, hasHttpHeaderContaining("Access-Control-Expose-Headers", "Location"));
  }

  @Test
  @InSequence(32)
  @ShouldMatchDataSet(value = "users-modify-patient.xml", orderBy = "user.login")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterUpdate() {}

  @Test
  @InSequence(40)
  @UsingDataSet("users.xml")
  public void beforeDelete() {}

  @Test
  @InSequence(41)
  @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testDelete(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Patient patient = patientToDelete();
    
    final Response response = webTarget.path(patient.getLogin())
      .request()
    .delete();
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(42)
  @ShouldMatchDataSet(value = "users-delete-patient.xml", orderBy = "user.login")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDelete() {}
  
  private void testGetAssignedSessions(ResteasyWebTarget webTarget) {
    final Patient patient = patient();
    
    final Response response = webTarget.path(patient.getLogin()).path("games-session").path("assigned")
      .request()
      .header("Origin", "localhost")
    .get();
    
    final AssignedGamesSession[] expected = assignedGamesSessionsOfPatient(patient.getLogin());
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("X-Total-Count", expected.length));
    assertThat(response, hasHttpHeaderContaining("Access-Control-Expose-Headers", "X-Total-Count"));
    
    final List<AssignedGamesSessionData> assignedData = response.readEntity(ASSIGNED_GAMES_SESSION_DATA_LIST_TYPE);
    
    assertThat(assignedData, containsAssignedGamesSessionsInAnyOrder(expected));
  }

  @Test
  @InSequence(100)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  public void beforeGetAssignedSessionsAsTherapist() {}

  @Test
  @InSequence(101)
  @Headers({
    @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testGetAssignedSessionsAsTherapist(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGetAssignedSessions(webTarget);
  }

  @Test
  @InSequence(102)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetAssignedSessionsAsTherapist() {}

  @Test
  @InSequence(103)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  public void beforeGetAssignedSessionsAsPatient() {}

  @Test
  @InSequence(104)
  @Headers({
    @Header(name = "Authorization", value = PATIENT_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testGetAssignedSessionsAsPatient(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGetAssignedSessions(webTarget);
  }

  @Test
  @InSequence(105)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetAssignedSessionsAsPatient() {}

  @Test
  @InSequence(106)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml" })
  public void beforeAssignSession() {}

  @Test
  @InSequence(107)
  @Headers({
    @Header(name = "Authorization", value = THERAPIST_HTTP_BASIC_AUTH),
    @Header(name = "Origin", value = "remote-host")
  })
  @RunAsClient
  public void testAssignSession(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Patient patient = patient();
    
    final AssignedGamesSessionCreationData data = this.gamesMapper.mapToAssignedGamesSessionCreation(newAssignedGamesSession());
    
    final Response response = webTarget.path(patient.getLogin()).path("games-session").path("assigned")
      .request()
    .post(json(data));
    
    assertThat(response, hasCreatedStatus());
    assertThat(response, hasHttpHeader("Location"));
    assertThat(response, hasHttpHeaderContaining("Access-Control-Expose-Headers", "Location"));
  }

  @Test
  @InSequence(108)
  @ShouldMatchDataSet(
    value = { "users.xml", "games.xml", "games-sessions.xml", "assigned-games-sessions.xml", "assigned-games-sessions-create.xml" },
    excludeColumns = "assigned_games_session.assignmentDate"
  )
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterAssignSession() {}
}
