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

package org.sing_group.mtc.rest.resource;

import org.apache.log4j.BasicConfigurator;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.sing_group.fluent.checker.Checks;

public final class Deployments {
  static {
    BasicConfigurator.configure();
  }
  
  private Deployments() {}
  
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "test.war")
      .addClass(Checks.class)
      .addPackages(true, "org.sing_group.mtc")
      .addAsLibraries(
        Maven.resolver()
          .loadPomFromFile("pom.xml")
          .importRuntimeDependencies()
          .resolve().withTransitivity()
        .asFile()
      )
      .addAsResource("arquillian.extension.persistence.properties")
      .addAsResource("arquillian.extension.persistence.dbunit.properties")
      .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
      .addAsWebInfResource("jboss-web.xml")
      .addAsWebInfResource("web.xml")
      .addAsWebInfResource("beans.xml");
  }
}
