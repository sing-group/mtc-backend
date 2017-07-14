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
package org.sing_group.mtc.rest.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
  @Override
  public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext cres)
    throws IOException {

    final MultivaluedMap<String, Object> headers = cres.getHeaders();

    headers.remove("Access-Control-Allow-Origin");
    headers.remove("Access-Control-Allow-Headers");
    headers.remove("Access-Control-Allow-Credentials");
    headers.remove("Access-Control-Allow-Methods");
    headers.remove("Access-Control-Max-Age");

    headers.add("Access-Control-Allow-Origin", "*");
    headers.add("Access-Control-Allow-Headers", "Accept, Authorization, Connection, Content-Length, Content-Type, Origin");
    headers.add("Access-Control-Allow-Credentials", "true");
    headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    headers.add("Access-Control-Max-Age", "1209600");
    headers.add("Access-Control-Expose-Headers", "Content-Type, Connection, Content-Length, Date, Location");
  }
}