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
package org.sing_group.mtc.rest.resource.game.session;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionData;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionEditionData;
import org.sing_group.mtc.rest.entity.game.session.GamesSessionData;
import org.sing_group.mtc.rest.entity.game.session.GamesSessionEditionData;
import org.sing_group.mtc.rest.entity.mapper.spi.game.GamesMapper;
import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.spi.game.session.GamesSessionResource;
import org.sing_group.mtc.service.spi.game.session.AssignedGamesSessionService;
import org.sing_group.mtc.service.spi.game.session.GamesSessionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Path("games-session")
@Api(
  value = "games-session",
  authorizations = @Authorization("basicAuth")
)
@ApiResponses({
  @ApiResponse(code = 401, message = SecurityExceptionMapper.UNAUTHORIZED_MESSAGE),
  @ApiResponse(code = 403, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
})
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Stateless
@Default
@CrossDomain
public class DefaultGamesSessionResource implements GamesSessionResource {
  @Inject
  private GamesSessionService service;
  
  @Inject
  private AssignedGamesSessionService assignedService;

  @Inject
  private GamesMapper gamesMapper;
  
  @Context
  private UriInfo uriInfo;
  
  @GET
  @Path("{id}")
  @ApiOperation(
    value = "Finds a games sessions by identifier.",
    response = GamesSessionData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown session: {id}")
  )
  @Override
  public Response get(@PathParam("id") int sessionId) {
    final GamesSession session = this.service.get(sessionId);
    
    final GamesSessionData sessionData =
      this.gamesMapper.mapToGameSessionData(session, this.uriInfo.getBaseUriBuilder());
    
    return Response.ok(sessionData).build();
  }
  
  @PUT
  @Path("{id}")
  @ApiOperation(
    value = "Modifies a games session.",
    response = GamesSessionData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown session: {id}")
  )
  @Override
  public Response modify(
    @PathParam("id") int sessionId,
    GamesSessionEditionData data
  ) {
    final GamesSession session = this.service.modify(
      this.gamesMapper.mapToGameSession(sessionId, data)
    );
    
    final GamesSessionData sessionData =
      this.gamesMapper.mapToGameSessionData(session, this.uriInfo.getBaseUriBuilder());
    
    return Response.ok(sessionData).build();
  }
  
  @DELETE
  @Path("{id}")
  @ApiOperation(
    value = "Deletes a games sessions.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown session: {id}")
  )
  @Override
  public Response delete(@PathParam("id") int sessionId) {
    this.service.delete(sessionId);
    
    return Response.ok().build();
  }

  @GET
  @Path("assigned/{id}")
  @ApiOperation(
    value = "Finds an assigned games sessions by identifier.",
    response = AssignedGamesSessionData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown session: {id}")
  )
  @Override
  public Response getAssigned(@PathParam("id") int assignedId) {
    final AssignedGamesSession assignedSession = this.assignedService.get(assignedId);
    
    final AssignedGamesSessionData assignedSessionData = this.gamesMapper.mapToAssignedGamesSession(
      assignedSession, this.uriInfo.getBaseUriBuilder()
    );
    
    return Response.ok(assignedSessionData).build();
  }
  
  @PUT
  @Path("assigned/{id}")
  @ApiOperation(
    value = "Modifies an assigned games sessions.",
    response = AssignedGamesSessionData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown assigned games session: {id}")
  )
  @Override
  public Response modifyAssigned(
    @PathParam("id") int sessionId,
    AssignedGamesSessionEditionData data
  ) {
    final AssignedGamesSession session = this.assignedService.modify(
      this.gamesMapper.mapToAssignedGamesSession(sessionId, data)
    );
    
    final AssignedGamesSessionData sessionData =
      this.gamesMapper.mapToAssignedGamesSession(session, this.uriInfo.getBaseUriBuilder());
    
    return Response.ok(sessionData).build();
  }
  
  @DELETE
  @Path("assigned/{id}")
  @ApiOperation(
    value = "Deletes an assigned games sessions.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown session: {id}")
  )
  @Override
  public Response deleteAssigned(@PathParam("id") int sessionId) {
    this.assignedService.delete(sessionId);
    
    return Response.ok().build();
  }
}
