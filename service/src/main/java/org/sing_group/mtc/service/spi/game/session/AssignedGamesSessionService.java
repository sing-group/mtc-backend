/*-
 * #%L
 * Service
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

package org.sing_group.mtc.service.spi.game.session;

import java.util.stream.Stream;

import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameResult;

public interface AssignedGamesSessionService {

  public AssignedGamesSession get(long assignedId);

  public AssignedGamesSession modify(AssignedGamesSession mapToAssignedGamesSession);

  public void delete(int sessionId);

  public Stream<AssignedGamesSession> list(ListingOptions listingOptions);

  public long count();

  public Stream<GameResult> listResultsOf(int assignedId, ListingOptions options);

  public long countResultsOf(int assignedId);

}
