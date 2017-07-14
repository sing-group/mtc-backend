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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.sing_group.mtc.domain.entities.UsersDataset.invalidEmails;
import static org.sing_group.mtc.domain.entities.UsersDataset.passwordOfUser;
import static org.sing_group.mtc.domain.entities.UsersDataset.users;
import static org.sing_group.mtc.domain.entities.UsersDataset.validEmails;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasOkStatus;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasUnauthorizedStatus;
import static org.sing_group.mtc.rest.resource.IsEqualToUserData.equalToUser;

import java.util.Optional;
import java.util.function.Consumer;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sing_group.mtc.domain.entities.User;
import org.sing_group.mtc.rest.resource.entity.UserData;

@RunWith(Arquillian.class)
public class SessionResourceIntegrationTest {
  private static final String BASE_PATH = "api/session/";

  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }

  @Test
  @InSequence(1)
  @UsingDataSet("users.xml")
  public void beforeValidCredentials() {}

  @Test
  @InSequence(2)
  @RunAsClient
  public void testValidCredentials(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Consumer<User> testAuthorized = user -> testAuthorized(webTarget, user);
    
    users().forEach(testAuthorized);
  }

  @Test
  @InSequence(3)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterValidCredentials() {}

  @Test
  @InSequence(5)
  @UsingDataSet("users.xml")
  public void beforeInvalidPassword() {}

  @Test
  @InSequence(6)
  @RunAsClient
  public void testInvalidPassword(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Consumer<String> testUnathorized = email -> testUnauthorized(webTarget, email, true);
    
    validEmails().forEach(testUnathorized);
  }

  @Test
  @InSequence(7)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterInvalidPassword() {}

  @Test
  @InSequence(10)
  @UsingDataSet("users.xml")
  public void beforeInvalidEmails() {}

  @Test
  @InSequence(11)
  @RunAsClient
  public void testInvalidEmails(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    final Consumer<String> testUnathorized = email -> testUnauthorized(webTarget, email);
    
    invalidEmails().forEach(testUnathorized);
  }

  @Test
  @InSequence(12)
  @ShouldMatchDataSet("users.xml")
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterInvalidEmails() {}
  
  private static void testAuthorized(ResteasyWebTarget webTarget, User expectedUser) {
    Optional<Response> responseRef = Optional.empty();
    
    try {
      final String password = passwordOfUser(expectedUser);
      
      final Response response = webTarget
        .queryParam("email", expectedUser.getEmail())
        .queryParam("password", password)
        .request()
      .get();
      
      assertThat(response, hasOkStatus());
      
      final UserData user = response.readEntity(UserData.class);
      
      assertThat(user, is(equalToUser(expectedUser)));
    } finally {
      responseRef.ifPresent(Response::close);
    }
  }
  
  private static void testUnauthorized(ResteasyWebTarget webTarget, String email) {
    testUnauthorized(webTarget, email, false);
  }
  
  private static void testUnauthorized(ResteasyWebTarget webTarget, String email, boolean breakPassword) {
    Optional<Response> responseRef = Optional.empty();
    
    try {
      final Response response = webTarget.clone()
        .queryParam("email", email)
        .queryParam("password", passwordOfUser(email) + (breakPassword ? "break" : ""))
        .request()
      .get();
      
      responseRef = Optional.of(response);
      
      assertThat(response, hasUnauthorizedStatus());
    } finally {
      responseRef.ifPresent(Response::close);
    }
    
  }
}
