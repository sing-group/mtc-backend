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

import java.net.URI;
import java.util.function.Function;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.mtc.domain.entities.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.resource.entity.mapper.GamesMapper;
import org.sing_group.mtc.rest.resource.entity.session.GamesSessionCreationData;
import org.sing_group.mtc.rest.resource.entity.session.GamesSessionData;
import org.sing_group.mtc.service.GamesSessionService;

@Path("game/session")
@Produces({
  MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
})
@Consumes({
  MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
})
@Stateless
public class GamesSessionResource {
  @Inject
  private GamesSessionService service;

  @Inject
  private GamesMapper gamesMapper;
  
  @Context
  private UriInfo uriInfo;

  @POST
  public Response create(GamesSessionCreationData sessionData) {
    final GamesSession session = this.service.createSession(
      this.gamesMapper.mapToGameSession(sessionData)
    );

    final URI url = this.uriInfo.getAbsolutePathBuilder()
      .path(Integer.toString(session.getId()))
    .build();
    
    return Response.created(url).build();
  }

  @GET
  @Path("{id}")
  public Response get(@PathParam("id") int sessionId) {
    final GamesSession session = this.service.get(sessionId);
    
    final Function<Therapist, String> therapistUriBuilder = therapist -> 
      uriInfo.getBaseUriBuilder()
        .path("user")
        .path(Integer.toString(therapist.getId()))
      .build().toString();
    
    final GamesSessionData sessionData = this.gamesMapper.mapToGameSessionData(session, therapistUriBuilder);
    
    return Response.ok(sessionData).build();
  }
}
