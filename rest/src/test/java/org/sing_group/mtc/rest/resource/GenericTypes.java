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

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.sing_group.mtc.rest.resource.entity.UserData;

/**
 * This class have methods to read list of entities
 * 
 * @author Miguel Reboiro-Jato
 *
 */
public final class GenericTypes {
  private GenericTypes() {}

  public static class UserDataListType extends GenericType<List<UserData>> {
    public static UserDataListType USER_DATA_LIST_TYPE = new UserDataListType();

    /**
     * Get a list of events from a response
     * 
     * @param response
     *          response received by rest
     * @return list of events
     */
    public static List<UserData> readEntity(Response response) {
      return response.readEntity(USER_DATA_LIST_TYPE);
    }
  }
}