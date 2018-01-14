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

package org.sing_group.mtc.rest.resource.game;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.mtc.rest.entity.game.GameResultData;
import org.sing_group.mtc.rest.entity.mapper.spi.game.GamesMapper;
import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.spi.game.GameResource;
import org.sing_group.mtc.service.spi.game.GameResultService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Path("game")
@Api(
  value = "game",
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
public class DefaultGameResource implements GameResource {
  @Inject
  private GameResultService gameResultService;

  @Inject
  private GamesMapper gamesMapper;
  
  @Context
  private UriInfo uriInfo;
  
  @GET
  @Path("result/{id: \\d+}")
  @ApiOperation(
    value = "Finds a game result by identifier.",
    response = GameResultData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown game result: {id}")
  )
  @Override
  public Response getResult(
    @PathParam("id") long id
  ) {
    final GameResultData result = this.gamesMapper.mapToGameResultData(
      this.gameResultService.getResult(id),
      this.uriInfo.getAbsolutePathBuilder()
    );
    
    return Response.ok(result).build();
  }
}
