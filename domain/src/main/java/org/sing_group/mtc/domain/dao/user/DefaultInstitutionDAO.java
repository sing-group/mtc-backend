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

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.sing_group.mtc.domain.dao.DAOHelper;
import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.spi.user.InstitutionDAO;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;

public class DefaultInstitutionDAO implements InstitutionDAO {
  @PersistenceContext
  protected EntityManager em;
  
  private DAOHelper<Long, Institution> dh;

  public DefaultInstitutionDAO() {
    super();
  }

  public DefaultInstitutionDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Long.class, Institution.class, this.em);
  }

  @Override
  public Institution get(long name) {
    return this.dh.get(name)
      .orElseThrow(() -> new IllegalArgumentException("Unknown institution: " + name));
  }

  @Override
  public Stream<Institution> list(ListingOptions listingOptions) {
    return this.dh.list(listingOptions).stream();
  }

  @Override
  public Stream<Institution> list(Manager manager, ListingOptions listingOptions) {
    return this.dh.list(listingOptions, (cb, root) -> new Predicate[] {
      cb.equal(root.get("manager"), manager)
    }).stream();
  }

  @Override
  public long count() {
    return this.dh.count();
  }

  @Override
  public Institution create(Institution institution) {
    return this.dh.persist(institution);
  }

  @Override
  public Institution update(Institution institution) {
    final Institution persistentInstitution = this.get(institution.getId());
    
    persistentInstitution.setName(institution.getName());
    persistentInstitution.setDescription(institution.getDescription().orElse(null));
    persistentInstitution.setAddress(institution.getAddress().orElse(null));
    
    return persistentInstitution;
  }

  @Override
  public void delete(long id) {
    this.dh.remove(this.get(id));
  }

}
