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

import java.net.URI;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.SortDirection;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionData;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionEditionData;
import org.sing_group.mtc.rest.entity.game.session.GamesSessionData;
import org.sing_group.mtc.rest.entity.game.session.GamesSessionEditionData;
import org.sing_group.mtc.rest.entity.mapper.spi.game.GamesMapper;
import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.mtc.rest.resource.spi.game.session.GamesSessionResource;
import org.sing_group.mtc.service.spi.game.session.AssignedGamesSessionService;
import org.sing_group.mtc.service.spi.game.session.GamesSessionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;

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
@CrossDomain(allowedHeaders = { "X-Total-Count", "Location" }, allowRequestHeaders = true)
public class DefaultGamesSessionResource implements GamesSessionResource {
  @Inject
  private GamesSessionService service;
  
  @Inject
  private AssignedGamesSessionService assignedService;

  @Inject
  private GamesMapper gamesMapper;
  
  @Context
  private UriInfo uriInfo;
  
  private BaseRestPathBuilder pathBuilder;
  
  @PostConstruct
  private void createPathBuilder() {
    this.pathBuilder = new BaseRestPathBuilder(this.uriInfo.getBaseUriBuilder());
  }
  
  @GET
  @Path("{id: \\d+}")
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
      this.gamesMapper.mapToGameSessionData(session, this.uriInfo.getAbsolutePathBuilder());
    
    return Response.ok(sessionData).build();
  }
  
  @PUT
  @Path("{id: \\d+}")
  @ApiOperation(
    value = "Modifies a games session.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the games session modified."),
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
    
    final URI sessionUri = this.pathBuilder.gamesSession(session).build();
    
    return Response.ok()
      .header("Location", sessionUri)
    .build();
  }
  
  @DELETE
  @Path("{id: \\d+}")
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
  @Path("assigned")
  @ApiOperation(
    value = "Returns a list of assigned sessions. Depending on the user that makes the request this "
      + "method can return the list of sessions assigned to patients (therapist) or the list of "
      + "sessions assigned to a patient (patient).",
    response = AssignedGamesSessionData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response listAssigned(
    @QueryParam("start") @DefaultValue("-1") int start,
    @QueryParam("end") @DefaultValue("-1") int end,
    @QueryParam("sort") String sortField,
    @QueryParam("order") @DefaultValue("NONE") SortDirection order
  ) {
    final Stream<AssignedGamesSession> assignedSessions = this.assignedService.list(
      new ListingOptions(start, end, sortField, order)
    );
    
    final AssignedGamesSessionData[] assignedSessionDatas = assignedSessions
      .map(session -> gamesMapper.mapToAssignedGamesSession(session, this.uriInfo.getAbsolutePathBuilder()))
    .toArray(AssignedGamesSessionData[]::new);
    
    return Response.ok(assignedSessionDatas).build();
  }

  @GET
  @Path("assigned/{id: \\d+}")
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
      assignedSession, this.uriInfo.getAbsolutePathBuilder()
    );
    
    return Response.ok(assignedSessionData).build();
  }
  
  @PUT
  @Path("assigned/{id: \\d+}")
  @ApiOperation(
    value = "Modifies an assigned games sessions.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the assigned games session modified."),
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
    
    final URI sessionUri = this.pathBuilder.gamesSessionAssigned(session).build();
    
    return Response.ok()
      .header("Location", sessionUri)
    .build();
  }
  
  @DELETE
  @Path("assigned/{id: \\d+}")
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
