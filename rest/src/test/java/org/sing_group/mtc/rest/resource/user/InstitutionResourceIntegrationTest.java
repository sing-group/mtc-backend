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
import static org.sing_group.mtc.domain.entities.UsersDataset.ADMIN_HTTP_BASIC_AUTH;
import static org.sing_group.mtc.domain.entities.UsersDataset.MANAGER_HTTP_BASIC_AUTH;
import static org.sing_group.mtc.domain.entities.UsersDataset.countInstitutions;
import static org.sing_group.mtc.domain.entities.UsersDataset.institution;
import static org.sing_group.mtc.domain.entities.UsersDataset.institutionToDelete;
import static org.sing_group.mtc.domain.entities.UsersDataset.institutionToModify;
import static org.sing_group.mtc.domain.entities.UsersDataset.institutions;
import static org.sing_group.mtc.domain.entities.UsersDataset.modifiedInstitution;
import static org.sing_group.mtc.domain.entities.UsersDataset.newInstitution;
import static org.sing_group.mtc.domain.entities.UsersDataset.newInstitutionId;
import static org.sing_group.mtc.domain.entities.UsersDataset.therapists;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeader;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeaderContaining;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeaderEndingWith;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasCreatedStatus;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.mtc.rest.entity.GenericTypes.InstitutionDataListType.INSTITUTION_DATA_LIST_TYPE;
import static org.sing_group.mtc.rest.entity.GenericTypes.TherapistDataListType.THERAPIST_DATA_LIST_TYPE;
import static org.sing_group.mtc.rest.entity.user.IsEqualToInstitution.containsInstitutionsInAnyOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToInstitution.containsInstitutionsInOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToInstitution.equalToInstitution;
import static org.sing_group.mtc.rest.entity.user.IsEqualToManager.equalToManager;
import static org.sing_group.mtc.rest.entity.user.IsEqualToTherapist.containsTherapistsInAnyOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToTherapist.containsTherapistsInOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToTherapist.equalToTherapist;

import java.util.List;
import java.util.function.Function;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sing_group.mtc.domain.dao.SortDirection;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.mapper.spi.user.InstitutionMapper;
import org.sing_group.mtc.rest.entity.mapper.user.DefaultInstitutionMapper;
import org.sing_group.mtc.rest.entity.user.InstitutionData;
import org.sing_group.mtc.rest.entity.user.InstitutionEditionData;
import org.sing_group.mtc.rest.entity.user.ManagerData;
import org.sing_group.mtc.rest.entity.user.TherapistData;
import org.sing_group.mtc.rest.resource.Deployments;

@RunWith(Arquillian.class)
public class InstitutionResourceIntegrationTest {
  private static final String BASE_PATH = "api/institution/";
  
  private InstitutionMapper userMapper;

  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }
  
  @Before
  public void setUp() {
    this.userMapper = new DefaultInstitutionMapper();
  }
  
  private void testGet(ResteasyWebTarget webTarget) {
    final Institution institution = institution();
    
    final Response response = webTarget.path(institution.getId().toString())
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    
    final InstitutionData userData = response.readEntity(InstitutionData.class);
    
    assertThat(userData, is(equalToInstitution(institution)));
  }

  @Test
  @InSequence(0)
  @UsingDataSet("users.xml")
  public void beforeGetAsAdmin() {}

  @Test
  @InSequence(1)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGetAsAdmin(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGet(webTarget);
  }

  @Test
  @InSequence(2)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetAsAdmin() {}

  @Test
  @InSequence(3)
  @UsingDataSet("users.xml")
  public void beforeGetAsManager() {}

  @Test
  @InSequence(4)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGetAsManager(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGet(webTarget);
  }

  @Test
  @InSequence(5)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetAsManager() {}

  @Test
  @InSequence(10)
  @UsingDataSet("users.xml")
  public void beforeList() {}

  @Test
  @InSequence(11)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testList(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Stream<Institution> institutions = institutions();
    
    final Response response = webTarget
      .request()
      .header("Origin", "localhost")
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("X-Total-Count", countInstitutions()));
    assertThat(response, hasHttpHeaderContaining("Access-Control-Expose-Headers", "X-Total-Count"));
    
    final List<InstitutionData> userData = response.readEntity(INSTITUTION_DATA_LIST_TYPE);
    
    assertThat(userData, containsInstitutionsInAnyOrder(institutions));
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
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testListFiltered(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final int start = 0;
    final int end = 2;
    final Function<Institution, String> getter = Institution::getName;
    final String order = "name";
    final SortDirection sortDirection = SortDirection.DESC;
    
    final Stream<Institution> institutions = institutions(
      start, end, getter, sortDirection
    );
    
    final Response response = webTarget
      .queryParam("start", start)
      .queryParam("end", end)
      .queryParam("order", order)
      .queryParam("sort", sortDirection)
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("X-Total-Count", countInstitutions()));
    
    final List<InstitutionData> institutionData = response.readEntity(INSTITUTION_DATA_LIST_TYPE);
    
    assertThat(institutionData, containsInstitutionsInOrder(institutions));
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
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testCreate(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Institution newInstitution = newInstitution();
    final InstitutionEditionData userData = userMapper.toEditionData(newInstitution);
    
    final Response response = webTarget
      .request()
    .post(json(userData));
    
    assertThat(response, hasCreatedStatus());
    assertThat(response, hasHttpHeaderEndingWith("Location", Integer.toString(newInstitutionId())));
  }

  @Test
  @InSequence(22)
  @ShouldMatchDataSet({ "users.xml", "users-create-institution.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterCreate() {}
  
  @Test
  @InSequence(30)
  @UsingDataSet("users.xml")
  public void beforeUpdate() {}

  @Test
  @InSequence(31)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testUpdate(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final InstitutionEditionData institutionData = userMapper.toEditionData(modifiedInstitution());
    
    final Response response = webTarget.path(Integer.toString(institutionToModify()))
      .request()
    .put(json(institutionData));
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(32)
  @ShouldMatchDataSet(value = "users-modify-institution.xml", orderBy = "user.login")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterUpdate() {}

  @Test
  @InSequence(40)
  @UsingDataSet("users.xml")
  public void beforeDelete() {}

  @Test
  @InSequence(41)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testDelete(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Institution institution = institutionToDelete();
    
    final Response response = webTarget.path(institution.getId().toString())
      .request()
    .delete();
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(42)
  @ShouldMatchDataSet(value = "users-delete-institution.xml", orderBy = "user.login")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDelete() {}
  
  private void testGetManager(ResteasyWebTarget webTarget) {
    final Institution institution = institution();
    final Manager expectedManager = institution.getManager().orElseThrow(IllegalStateException::new);
    
    final Response response = webTarget.path(institution.getId().toString()).path("manager")
      .request()
      .header("Origin", "localhost")
    .get();
    
    assertThat(response, hasOkStatus());
    
    final ManagerData userData = response.readEntity(ManagerData.class);
    
    assertThat(userData, is(equalToManager(expectedManager)));
  }

  @Test
  @InSequence(100)
  @UsingDataSet("users.xml")
  public void beforeGetManagerAsAdmin() {}

  @Test
  @InSequence(101)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGetManagerAsAdmin(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGetManager(webTarget);
  }

  @Test
  @InSequence(102)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetManagerAsAdmin() {}

  @Test
  @InSequence(103)
  @UsingDataSet("users.xml")
  public void beforeGetManagerAsManager() {}

  @Test
  @InSequence(104)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGetManagerAsManager(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGetManager(webTarget);
  }

  @Test
  @InSequence(105)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetManagerAsManager() {}
  
  private void testListTherapists(ResteasyWebTarget webTarget) {
    final Institution institution = institution();
    
    final Stream<Therapist> therapists = institution.getTherapists();
    
    final Response response = webTarget.path(institution.getId().toString()).path("therapist")
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("X-Total-Count", institution.getTherapists().count()));
    
    final List<TherapistData> therapistData = response.readEntity(THERAPIST_DATA_LIST_TYPE);
    
    assertThat(therapistData, containsTherapistsInAnyOrder(therapists));
  }

  @Test
  @InSequence(200)
  @UsingDataSet("users.xml")
  public void beforeListTherapistsAsAdmin() {}

  @Test
  @InSequence(201)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testListTherapistsAsAdmin(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testListTherapists(webTarget);
  }

  @Test
  @InSequence(202)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListTherapistsAsAdmin() {}

  @Test
  @InSequence(203)
  @UsingDataSet("users.xml")
  public void beforeListTherapistsAsManager() {}

  @Test
  @InSequence(204)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testListTherapistsAsManager(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testListTherapists(webTarget);
  }

  @Test
  @InSequence(205)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListTherapistsAsManager() {}
  
  private void testListTherapistsFiltered(ResteasyWebTarget webTarget) {
    final int start = 1;
    final int end = 2;
    final Function<Therapist, String> getter = Therapist::getEmail;
    final String order = "email";
    final SortDirection sortDirection = SortDirection.DESC;
    
    final Institution institution = institution(2);
    
    final Stream<Therapist> therapists = therapists(
      institution.getTherapists(), start, end, getter, sortDirection
    );
    
    final Response response = webTarget.path(institution.getId().toString()).path("therapist")
      .queryParam("start", start)
      .queryParam("end", end)
      .queryParam("order", order)
      .queryParam("sort", sortDirection)
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("X-Total-Count", institution.getTherapists().count()));
    
    final List<TherapistData> therapistData = response.readEntity(THERAPIST_DATA_LIST_TYPE);
    
    assertThat(therapistData, containsTherapistsInOrder(therapists));
  }

  @Test
  @InSequence(206)
  @UsingDataSet("users.xml")
  public void beforeListTherapistsFilteredAsAdmin() {}

  @Test
  @InSequence(207)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testListTherapistsFilteredAsAdmin(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testListTherapistsFiltered(webTarget);
  }

  @Test
  @InSequence(208)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListTherapistsFilteredAsAdmin() {}

  @Test
  @InSequence(209)
  @UsingDataSet("users.xml")
  public void beforeListTherapistsFilteredAsManager() {}

  @Test
  @InSequence(210)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testListTherapistsFilteredAsManager(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testListTherapistsFiltered(webTarget);
  }

  @Test
  @InSequence(211)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListTherapistsFilteredAsManager() {}

  
  private void testGetTherapist(ResteasyWebTarget webTarget) {
    final Institution institution = institution();
    
    final Therapist therapist = institution.getTherapists().findFirst().orElseThrow(IllegalStateException::new);
    
    final Response response = webTarget.path(institution.getId().toString()).path("therapist").path(therapist.getLogin())
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    
    final TherapistData therapistData = response.readEntity(TherapistData.class);
    
    assertThat(therapistData, is(equalToTherapist(therapist)));
  }

  @Test
  @InSequence(212)
  @UsingDataSet("users.xml")
  public void beforeGetTherapistAsAdmin() {}

  @Test
  @InSequence(213)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGetTherapistAsAdmin(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGetTherapist(webTarget);
  }

  @Test
  @InSequence(214)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetTherapistAsAdmin() {}

  @Test
  @InSequence(215)
  @UsingDataSet("users.xml")
  public void beforeGetTherapistAsManager() {}

  @Test
  @InSequence(216)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGetTherapistAsManager(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGetTherapist(webTarget);
  }

  @Test
  @InSequence(217)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetTherapistAsManager() {}
}
