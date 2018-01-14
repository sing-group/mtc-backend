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

import static org.junit.Assert.assertThat;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasForbiddenStatus;
import static org.sing_group.mtc.http.util.HasHttpStatus.hasUnauthorizedStatus;

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
import org.sing_group.mtc.domain.entities.UsersDataset;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset;
import org.sing_group.mtc.http.util.HasHttpStatus;
import org.sing_group.mtc.rest.resource.Deployments;


@RunWith(Arquillian.class)
public class GamesSessionResourceSecurityTest {
  private static final String BASE_PATH = "api/games-session";
  
  @Deployment
  public static Archive<?> createDeployment() {
    return Deployments.createDeployment();
  }

  @Test
  @InSequence(11)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  public void beforeGetAsAnonymous() {}
  
  @Test
  @InSequence(12)
  @RunAsClient
  public void testGetAsAnonymous(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGet(webTarget, hasUnauthorizedStatus());
  }

  @Test
  @InSequence(13)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetAsAnonymous() {}

  @Test
  @InSequence(21)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  public void beforeGetAsAdmin() {}
  
  @Test
  @InSequence(22)
  @RunAsClient
  @Header(name = "Authorization", value = UsersDataset.ADMIN_HTTP_BASIC_AUTH)
  public void testGetAsAdmin(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGet(webTarget, hasForbiddenStatus());
  }

  @Test
  @InSequence(23)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetAsAdmin() {}

  @Test
  @InSequence(31)
  @UsingDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  public void beforeGetAsPatient() {}
  
  @Test
  @InSequence(32)
  @RunAsClient
  @Header(name = "Authorization", value = UsersDataset.PATIENT_HTTP_BASIC_AUTH)
  public void testGetAsPatient(
    @ArquillianResteasyResource(BASE_PATH) ResteasyWebTarget webTarget
  ) {
    this.testGet(webTarget, hasForbiddenStatus());
  }

  @Test
  @InSequence(33)
  @ShouldMatchDataSet({ "users.xml", "games.xml", "games-sessions.xml" })
  @CleanupUsingScript({ "cleanup.sql", "cleanup-autoincrement.sql" })
  public void afterGetAsPatient() {}
  
  private void testGet(ResteasyWebTarget webTarget, HasHttpStatus statusMatcher) {
    final GamesSession session = GamesSessionDataset.session1();
    
    final Response response = webTarget.path(session.getId().toString())
      .request()
    .get();
    
    assertThat(response, statusMatcher);
  }
}
