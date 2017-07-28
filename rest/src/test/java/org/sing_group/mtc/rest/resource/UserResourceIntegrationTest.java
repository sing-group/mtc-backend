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
import static org.sing_group.mtc.domain.entities.UsersDataset.ADMIN_HTTP_BASIC_AUTH;
import static org.sing_group.mtc.domain.entities.UsersDataset.modifiedUser;
import static org.sing_group.mtc.domain.entities.UsersDataset.newPatient;
import static org.sing_group.mtc.domain.entities.UsersDataset.patientToDelete;
import static org.sing_group.mtc.domain.entities.UsersDataset.users;
import static org.sing_group.mtc.http.util.HasHttpHeader.hasHttpHeader;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasCreatedStatus;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.mtc.rest.resource.GenericTypes.UserDataListType.USER_DATA_LIST_TYPE;
import static org.sing_group.mtc.rest.resource.IsEqualToUserData.containsUsersInAnyOrder;
import static org.sing_group.mtc.rest.resource.IsEqualToUserData.equalToUser;

import java.util.List;

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
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.User;
import org.sing_group.mtc.rest.resource.entity.UserData;
import org.sing_group.mtc.rest.resource.entity.UserEditionData;

@RunWith(Arquillian.class)
public class UserResourceIntegrationTest {
  private static final String BASE_PATH = "api/user/";

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
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testGet(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    users().forEach(expectedUser -> {
      final Response response = webTarget.path(Integer.toString(expectedUser.getId()))
        .request()
      .get();
      
      assertThat(response, hasOkStatus());
      
      final UserData userData = response.readEntity(UserData.class);
      
      assertThat(userData, is(equalToUser(expectedUser)));
      
      response.close();
    });
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
    users().forEach(expectedUser -> {
      final Response response = webTarget
        .request()
      .get();
      
      assertThat(response, hasOkStatus());
      
      final List<UserData> userData = response.readEntity(USER_DATA_LIST_TYPE);
      
      assertThat(userData, containsUsersInAnyOrder(users()));
      
      response.close();
    });
  }

  @Test
  @InSequence(12)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterList() {}

  @Test
  @InSequence(20)
  @UsingDataSet("users.xml")
  public void beforeCreatePatient() {}

  @Test
  @InSequence(21)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testCreatePatient(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Patient newPatient = newPatient();
    final UserEditionData userData = UserEditionData.of(newPatient);
    
    final Response response = webTarget
      .request()
    .post(json(userData));
    
    assertThat(response, hasCreatedStatus());
    assertThat(response, hasHttpHeader("Location"));
    
    response.close();
  }

  @Test
  @InSequence(22)
  @ShouldMatchDataSet({ "users.xml", "user-create-patient.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterCreatePatient() {}

  @Test
  @InSequence(30)
  @UsingDataSet("users.xml")
  public void beforeUpdateUser() {}

  @Test
  @InSequence(31)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testUpdateUser(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final User user = modifiedUser();
    final UserEditionData userData = UserEditionData.of(user);
    
    final Response response = webTarget.path(Integer.toString(user.getId()))
      .request()
    .put(json(userData));
    
    assertThat(response, hasOkStatus());
    
    response.close();
  }

  @Test
  @InSequence(32)
  @ShouldMatchDataSet(value = "user-update-patient.xml", orderBy = "id")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterUpdateUser() {}

  @Test
  @InSequence(40)
  @UsingDataSet("users.xml")
  public void beforeDeleteUser() {}

  @Test
  @InSequence(41)
  @Header(name = "Authorization", value = ADMIN_HTTP_BASIC_AUTH)
  @RunAsClient
  public void testDeleteUser(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final User user = patientToDelete();
    
    final Response response = webTarget.path(Integer.toString(user.getId()))
      .request()
    .delete();
    
    assertThat(response, hasOkStatus());
    
    response.close();
  }

  @Test
  @InSequence(42)
  @ShouldMatchDataSet({ "user-delete-patient.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterDeleteUser() {}

}
