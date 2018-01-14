/*-
 * #%L
 * Domain
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

package org.sing_group.mtc.domain.dao.game;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.mtc.domain.dao.DAOHelper;
import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.spi.game.GameResultDAO;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameResult;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultGameResultDAO implements GameResultDAO {
  @PersistenceContext
  private EntityManager em;

  private DAOHelper<Long, GameResult> dh;

  DefaultGameResultDAO() {}

  public DefaultGameResultDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(Long.class, GameResult.class, this.em);
  }

  @Override
  public GameResult get(long id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown game result: " + id));
  }
  
  @Override
  public Stream<GameResult> listByAssignedGamesSession(
    AssignedGamesSession assignedGamesSession, ListingOptions options
  ) {
    return this.dh.list(options, (CriteriaBuilder cb, Root<GameResult> root) -> new Predicate[] {
      cb.equal(root.get("assignedGamesSession"), assignedGamesSession)
    }).stream();
  }
}
