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
import static org.sing_group.mtc.domain.entities.UsersDataset.countManagers;
import static org.sing_group.mtc.domain.entities.UsersDataset.institutions;
import static org.sing_group.mtc.domain.entities.UsersDataset.manager;
import static org.sing_group.mtc.domain.entities.UsersDataset.managerToDelete;
import static org.sing_group.mtc.domain.entities.UsersDataset.managers;
import static org.sing_group.mtc.domain.entities.UsersDataset.modifiedManager;
import static org.sing_group.mtc.domain.entities.UsersDataset.newManager;
import static org.sing_group.mtc.domain.entities.UsersDataset.newPasswordOf;
import static org.sing_group.mtc.domain.entities.UsersDataset.passwordOf;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeader;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasCreatedStatus;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.mtc.rest.entity.GenericTypes.InstitutionDataListType.INSTITUTION_DATA_LIST_TYPE;
import static org.sing_group.mtc.rest.entity.GenericTypes.ManagerDataListType.MANAGER_DATA_LIST_TYPE;
import static org.sing_group.mtc.rest.entity.user.IsEqualToInstitution.containsInstitutionsInAnyOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToInstitution.equalToInstitution;
import static org.sing_group.mtc.rest.entity.user.IsEqualToManager.containsManagersInAnyOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToManager.containsManagersInOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToManager.equalToManager;

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
import org.sing_group.mtc.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.mtc.rest.entity.mapper.user.DefaultUserMapper;
import org.sing_group.mtc.rest.entity.user.InstitutionData;
import org.sing_group.mtc.rest.entity.user.ManagerData;
import org.sing_group.mtc.rest.entity.user.ManagerEditionData;
import org.sing_group.mtc.rest.resource.Deployments;

@RunWith(Arquillian.class)
public class ManagerResourceIntegrationTest {
  private static final String BASE_PATH = "api/manager/";
  
  private UserMapper userMapper;

  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }
  
  @Before
  public void setUp() {
    this.userMapper = new DefaultUserMapper();
  }

  @Test
  @InSequence(0)
  @UsingDataSet("users.xml")
  public void beforeGet() {}

  @Test
  @InSequence(1)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGet(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Manager manager = manager();
    
    final Response response = webTarget.path(manager.getLogin())
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    
    final ManagerData userData = response.readEntity(ManagerData.class);
    
    assertThat(userData, is(equalToManager(manager)));
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
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testList(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Stream<Manager> managers = managers();
    
    final Response response = webTarget
      .request()
      .header("Origin", "localhost")
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("Access-Control-Allow-Headers", header -> header.contains("X-Total-Count")));
    assertThat(response, hasHttpHeader("X-Total-Count", countManagers()));
    
    final List<ManagerData> userData = response.readEntity(MANAGER_DATA_LIST_TYPE);
    
    assertThat(userData, containsManagersInAnyOrder(managers));
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
    final int start = 1;
    final int end = 3;
    final Function<Manager, String> getter = Manager::getLogin;
    final String order = "login";
    final SortDirection sortDirection = SortDirection.ASC;
    
    final Stream<Manager> managers = managers(
      start, end, getter, sortDirection
    );
    
    final Response response = webTarget
      .queryParam("start", start)
      .queryParam("end", end)
      .queryParam("order", order)
      .queryParam("sort", sortDirection)
      .request()
      .header("Origin", "localhost")
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("Access-Control-Allow-Headers", header -> header.contains("X-Total-Count")));
    assertThat(response, hasHttpHeader("X-Total-Count", countManagers()));
    
    final List<ManagerData> managerData = response.readEntity(MANAGER_DATA_LIST_TYPE);
    
    assertThat(managerData, containsManagersInOrder(managers));
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
    final Manager newAdmin = newManager();
    final ManagerEditionData userData = userMapper.toEditionData(newAdmin, passwordOf(newAdmin));
    
    final Response response = webTarget
      .request()
    .post(json(userData));
    
    assertThat(response, hasCreatedStatus());
    assertThat(response, hasHttpHeader("Location", value -> value.endsWith(newAdmin.getLogin())));
  }

  @Test
  @InSequence(22)
  @ShouldMatchDataSet({ "users.xml", "users-create-manager.xml" })
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
    final Manager modifiedAdmin = modifiedManager();
    final ManagerEditionData userData = userMapper.toEditionData(modifiedAdmin, newPasswordOf(modifiedAdmin));
    
    final Response response = webTarget
      .request()
    .put(json(userData));
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(32)
  @ShouldMatchDataSet(value = "users-modify-manager.xml", orderBy = "user.login")
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
    final Manager manager = managerToDelete();
    
    final Response response = webTarget.path(manager.getLogin())
      .request()
    .delete();
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(42)
  @ShouldMatchDataSet(value = "users-delete-manager.xml", orderBy = "user.login")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDelete() {}

  private void testListInstitution(ResteasyWebTarget webTarget) {
    final Manager manager = manager();
    
    final Response response = webTarget.path(manager.getLogin()).path("institution")
      .request()
      .header("Origin", "localhost")
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("Access-Control-Allow-Headers", header -> header.contains("X-Total-Count")));
    assertThat(response, hasHttpHeader("X-Total-Count", manager.getInstitutions().count()));
    
    final List<InstitutionData> institutionData = response.readEntity(INSTITUTION_DATA_LIST_TYPE);
    
    assertThat(institutionData, containsInstitutionsInAnyOrder(manager.getInstitutions()));
  }
  
  @Test
  @InSequence(100)
  @UsingDataSet("users.xml")
  public void beforeListInstitutionAsAdmin() {}

  @Test
  @InSequence(101)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testListInstitutionAsAdmin(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testListInstitution(webTarget);
  }

  @Test
  @InSequence(102)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListInstitutionAsAdmin() {}
  
  @Test
  @InSequence(103)
  @UsingDataSet("users.xml")
  public void beforeListInstitutionAsManager() {}

  @Test
  @InSequence(104)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testListInstitutionAsManager(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testListInstitution(webTarget);
  }

  @Test
  @InSequence(105)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListInstitutionAsManager() {}

  @Test
  @InSequence(106)
  @UsingDataSet("users.xml")
  public void beforeListFilteredInstitution() {}

  @Test
  @InSequence(107)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testListFilteredInstitution(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Manager manager = manager();
    
    final int start = 1;
    final int end = 2;
    final String order = "name";
    final SortDirection sortDirection = SortDirection.DESC;
    
    final Response response = webTarget.path(manager.getLogin()).path("institution")
      .queryParam("start", start)
      .queryParam("end", end)
      .queryParam("order", order)
      .queryParam("sort", sortDirection)
      .request()
      .header("Origin", "localhost")
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("Access-Control-Allow-Headers", header -> header.contains("X-Total-Count")));
    assertThat(response, hasHttpHeader("X-Total-Count", manager.getInstitutions().count()));
    
    final List<InstitutionData> institutionData = response.readEntity(INSTITUTION_DATA_LIST_TYPE);
    
    assertThat(institutionData, containsInstitutionsInAnyOrder(institutions(manager.getInstitutions(), start, end, Institution::getName, sortDirection)));
  }

  @Test
  @InSequence(108)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterListFilteredInstitution() {}
  
  private void testGetInstitution(ResteasyWebTarget webTarget) {
    final Manager manager = manager();
    final Institution institution = manager.getInstitutions().findFirst().orElseThrow(IllegalStateException::new);
    
    final Response response = webTarget.path(manager.getLogin()).path("institution").path(institution.getId().toString())
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    
    final InstitutionData userData = response.readEntity(InstitutionData.class);
    
    assertThat(userData, is(equalToInstitution(institution)));
  }

  @Test
  @InSequence(109)
  @UsingDataSet("users.xml")
  public void beforeGetInstitutionAsAdmin() {}

  @Test
  @InSequence(110)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGetInstitutionAsAdmin(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGetInstitution(webTarget);
  }

  @Test
  @InSequence(111)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetInstitutionAsAdmin() {}

  @Test
  @InSequence(112)
  @UsingDataSet("users.xml")
  public void beforeGetInstitutionAsManager() {}

  @Test
  @InSequence(113)
  @Header(name = "Authorization", value = MANAGER_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGetInstitutionAsManager(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGetInstitution(webTarget);
  }

  @Test
  @InSequence(114)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetInstitutionAsManager() {}

}
