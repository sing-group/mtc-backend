/*
 * #%L
 * Domain
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
package org.sing_group.mtc.domain.dao.user;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.sing_group.mtc.domain.dao.DAOHelper;
import org.sing_group.mtc.domain.dao.spi.user.InstitutionDAO;
import org.sing_group.mtc.domain.entities.user.Institution;

public class DefaultInstitutionDAO implements InstitutionDAO {

  @PersistenceContext
  protected EntityManager em;
  private DAOHelper<Integer, Institution> dh;

  public DefaultInstitutionDAO() {
    super();
  }

  public DefaultInstitutionDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Institution.class, this.em);
  }

  @Override
  public Institution get(int name) {
    return this.dh.get(name)
      .orElseThrow(() -> new IllegalArgumentException("Unknown institution: " + name));
  }

}
