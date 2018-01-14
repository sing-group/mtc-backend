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

package org.sing_group.mtc.rest.resource.spi.user;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.mtc.domain.dao.SortDirection;
import org.sing_group.mtc.rest.entity.user.ManagerCreationData;
import org.sing_group.mtc.rest.entity.user.ManagerEditionData;

@Local
public interface ManagerResource {

  public Response get(String login);

  public Response list(int start, int end, String sortField, SortDirection order);

  public Response create(ManagerCreationData data);

  public Response update(String login, ManagerEditionData data);

  public Response delete(String login);
  
  public Response getInstitutions(String login, int start, int end, String sortField, SortDirection order);

  public Response getInstitution(String login, long id);

  public Response getTherapists(String login, int start, int end, String sortField, SortDirection order);

}
