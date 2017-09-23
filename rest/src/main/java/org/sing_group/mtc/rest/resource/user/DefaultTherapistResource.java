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
package org.sing_group.mtc.rest.resource.user;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.net.URI;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.SortDirection;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.mapper.spi.game.GamesMapper;
import org.sing_group.mtc.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.mtc.rest.entity.session.GamesSessionCreationData;
import org.sing_group.mtc.rest.entity.session.GamesSessionData;
import org.sing_group.mtc.rest.entity.user.TherapistData;
import org.sing_group.mtc.rest.entity.user.TherapistEditionData;
import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.spi.user.TherapistResource;
import org.sing_group.mtc.service.spi.user.InstitutionService;
import org.sing_group.mtc.service.spi.user.TherapistService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;

@Path("therapist")
@Api(
  value = "therapist",
  authorizations = @Authorization("basicAuth")
)
@ApiResponses({
  @ApiResponse(code = 401, message = SecurityExceptionMapper.UNAUTHORIZED_MESSAGE),
  @ApiResponse(code = 403, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
})
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain(allowedHeaders = { "X-Total-Count", "Location" }, allowRequestHeaders = true)
public class DefaultTherapistResource implements TherapistResource {
  @Inject
  private TherapistService service;
  
  @Inject
  private InstitutionService institutionService;
  
  @Inject
  private UserMapper userMapper;
  
  @Inject
  private GamesMapper gamesMapper;
  
  @Context
  private UriInfo uriInfo;
  
  @GET
  @Path("{login}")
  @ApiOperation(
    value = "Finds therapists by login.",
    response = TherapistData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login} | 'login' should have a length between 1 and 100")
  )
  @Override
  public Response get(@PathParam("login") String login) {
    final Therapist user = this.service.get(login);

    return Response.ok(toData(user)).build();
  }
  
  @GET
  @ApiOperation(
    value = "Returns all the therapists in the database.",
    response = TherapistData.class,
    responseContainer = "List",
    code = 200,
    responseHeaders = @ResponseHeader(name = "X-Total-Count", description = "Total number of therapists in the database.")
  )
  @Override
  public Response list(
    @QueryParam("start") @DefaultValue("-1") int start,
    @QueryParam("end") @DefaultValue("-1") int end,
    @QueryParam("order") String order,
    @QueryParam("sort") @DefaultValue("NONE") SortDirection sort
  ) {
    final ListingOptions options = new ListingOptions(start, end, order, sort);
    
    final TherapistData[] therapists = this.service.list(options)
      .map(this::toData)
    .toArray(TherapistData[]::new);
    
    return Response.ok(therapists)
      .header("X-Total-Count", this.service.count())
    .build();
  }
  
  @POST
  @ApiOperation(
    value = "Creates a new therapist.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the new therapist created."),
    code = 201
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Entity already exists")
  )
  @Override
  public Response create(TherapistEditionData data) {
    final Therapist therapist = this.service.create(userMapper.toTherapist(data, this.institutionService.get(data.getInstitution())));
    
    final URI userUri = this.buildUriFor(therapist);
    
    return Response.created(userUri).build();
  }
  
  @PUT
  @ApiOperation(
    value = "Modifies an existing therapist.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  @Override
  public Response update(
    TherapistEditionData data
  ) {
    this.service.update(userMapper.toTherapist(data, this.institutionService.get(data.getInstitution())));
    
    return Response.ok().build();
  }
  
  @DELETE
  @Path("{login}")
  @ApiOperation(
    value = "Deletes an existing therapist.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  @Override
  public Response delete(@PathParam("login") String login) {
    this.service.delete(login);
    
    return Response.ok().build();
  }
  
  @POST
  @Path("{login}/gamesession")
  @Override
  @ApiOperation(
    value = "Creates a new games session associated to the therapist.",
    responseHeaders = @ResponseHeader(
      name = "Location",
      description = "Location of the new games session created relative to the therapist."
    ),
    code = 201
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Entity already exists")
  )
  public Response createGamesSession(
    @PathParam("login") String therapist,
    GamesSessionCreationData data
  ) {
    final GamesSession gamesSession = this.service.createGamesSession(
      therapist, gamesMapper.mapToGameSession(data)
    );
    
    return Response
      .created(this.buildUriForSession(gamesSession))
    .build();
  }

  @GET
  @Path("{login}/gamesession")
  @Override
  @ApiOperation(
    value = "List the games sessions of the therapist.",
    code = 200,
    responseContainer = "List",
    response = GamesSessionData.class
  )
  public Response listGamesSessions(@PathParam("login") String therapist) {
    final GamesSessionData[] sessions = this.service.listGameSessions(therapist)
      .map(session -> gamesMapper.mapToGameSessionData(session, this::buildUriFor))
    .toArray(GamesSessionData[]::new);
    
    return Response.ok(sessions).build();
  }
  
  private URI buildUriFor(Therapist therapist) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(therapist.getLogin())
    .build();
  }
  
  private URI buildUriForInstitution(Therapist therapist) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(therapist.getLogin())
      .path("institution")
    .build();
  }
  
  private URI buildUriForPatient(Patient patient) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(patient.getTherapist().getLogin())
      .path("patient")
      .path(patient.getLogin())
    .build();
  }
  
  private URI buildUriForSession(GamesSession session) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(session.getTherapist().map(Therapist::getLogin).orElseThrow(IllegalStateException::new))
      .path("gamesession")
      .path(session.getId().toString())
    .build();
  }
  
  private TherapistData toData(Therapist therapist) {
    return userMapper.toData(
      therapist,
      institution -> this.buildUriForInstitution(therapist),
      this::buildUriForPatient,
      this::buildUriForSession
    );
  }
}
