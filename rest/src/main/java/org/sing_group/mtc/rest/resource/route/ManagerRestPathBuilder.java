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

package org.sing_group.mtc.rest.resource.route;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class ManagerRestPathBuilder implements RestPathBuilder {
  protected UriBuilder builder;

  public ManagerRestPathBuilder(UriBuilder builder) {
    this.builder = builder.clone().path("manager");
  }
  
  public ManagerRestPathBuilder(UriBuilder builder, String login) {
    this.builder = builder.clone().path("manager").path(login);
  }

  @Override
  public URI build() {
    return this.builder.clone().build();
  }
}
