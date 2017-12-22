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
import static org.sing_group.mtc.domain.entities.UsersDataset.admin;
import static org.sing_group.mtc.domain.entities.UsersDataset.administratorToDelete;
import static org.sing_group.mtc.domain.entities.UsersDataset.admins;
import static org.sing_group.mtc.domain.entities.UsersDataset.countAdmins;
import static org.sing_group.mtc.domain.entities.UsersDataset.modifiedAdministrator;
import static org.sing_group.mtc.domain.entities.UsersDataset.newAdministrator;
import static org.sing_group.mtc.domain.entities.UsersDataset.newPasswordOf;
import static org.sing_group.mtc.domain.entities.UsersDataset.passwordOf;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeader;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeaderContaining;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeaderEndingWith;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasCreatedStatus;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.mtc.rest.entity.GenericTypes.AdministratorDataListType.ADMINISTRATOR_DATA_LIST_TYPE;
import static org.sing_group.mtc.rest.entity.user.IsEqualToAdministrator.containsAdministratorsInAnyOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToAdministrator.containsAdministratorsInOrder;
import static org.sing_group.mtc.rest.entity.user.IsEqualToAdministrator.equalToAdministrator;

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
import org.sing_group.mtc.domain.entities.user.Administrator;
import org.sing_group.mtc.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.mtc.rest.entity.mapper.user.DefaultUserMapper;
import org.sing_group.mtc.rest.entity.user.AdministratorData;
import org.sing_group.mtc.rest.entity.user.AdministratorEditionData;
import org.sing_group.mtc.rest.resource.Deployments;

@RunWith(Arquillian.class)
public class AdministratorResourceIntegrationTest {
  private static final String BASE_PATH = "api/admin/";
  
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
    final Administrator admin = admin();
    
    final Response response = webTarget.path(admin.getLogin())
      .request()
    .get();
    
    assertThat(response, hasOkStatus());
    
    final AdministratorData userData = response.readEntity(AdministratorData.class);
    
    assertThat(userData, is(equalToAdministrator(admin)));
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
    final Stream<Administrator> admins = admins();
    
    final Response response = webTarget
      .request()
      .header("Origin", "localhost")
    .get();
    
    assertThat(response, hasOkStatus());
    assertThat(response, hasHttpHeader("X-Total-Count", countAdmins()));
    assertThat(response, hasHttpHeaderContaining("Access-Control-Expose-Headers", "X-Total-Count"));
    
    final List<AdministratorData> userData = response.readEntity(ADMINISTRATOR_DATA_LIST_TYPE);
    
    assertThat(userData, containsAdministratorsInAnyOrder(admins));
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
    final Function<Administrator, String> getter = Administrator::getLogin;
    final String sortField = "login";
    final SortDirection sortDirection = SortDirection.DESC;
    
    final Stream<Administrator> admins = admins(
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
    assertThat(response, hasHttpHeader("X-Total-Count", countAdmins()));
    
    final List<AdministratorData> adminData = response.readEntity(ADMINISTRATOR_DATA_LIST_TYPE);
    
    assertThat(adminData, containsAdministratorsInOrder(admins));
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
    final Administrator newAdmin = newAdministrator();
    final AdministratorEditionData userData = userMapper.toCreationData(newAdmin, passwordOf(newAdmin));
    
    final Response response = webTarget
      .request()
    .post(json(userData));
    
    assertThat(response, hasCreatedStatus());
    assertThat(response, hasHttpHeaderEndingWith("Location", newAdmin.getLogin()));
  }

  @Test
  @InSequence(22)
  @ShouldMatchDataSet({ "users.xml", "users-create-administrator.xml" })
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
    final Administrator modifiedAdmin = modifiedAdministrator();
    final AdministratorEditionData userData = userMapper.toEditionData(modifiedAdmin, newPasswordOf(modifiedAdmin));
    
    final Response response = webTarget.path(modifiedAdmin.getLogin())
      .request()
    .put(json(userData));
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(32)
  @ShouldMatchDataSet(value = "users-modify-administrator.xml", orderBy = "user.login")
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
    final Administrator admin = administratorToDelete();
    
    final Response response = webTarget.path(admin.getLogin())
      .request()
    .delete();
    
    assertThat(response, hasOkStatus());
  }

  @Test
  @InSequence(42)
  @ShouldMatchDataSet(value = "users-delete-administrator.xml", orderBy = "user.login")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDelete() {}

}
