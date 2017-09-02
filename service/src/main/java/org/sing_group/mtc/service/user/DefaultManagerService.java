/*
 * #%L
 * Service
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
package org.sing_group.mtc.service.user;

import static org.sing_group.fluent.checker.Checks.requireStringSize;

import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.spi.user.ManagerDAO;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.service.spi.user.ManagerService;

@Stateless
@RolesAllowed("ADMIN")
public class DefaultManagerService implements ManagerService {
  @Inject
  private ManagerDAO dao;

  @Override
  public Manager get(String login) {
    requireStringSize(login, 1, 100, "'login' should have a length between 1 and 100");
    
    return dao.get(login);
  }

  @Override
  public Stream<Manager> list() {
    return dao.list();
  }
  
  @Override
  public Manager create(Manager manager) {
    return dao.create(manager);
  }
  
  @Override
  public Manager update(Manager manager) {
    return dao.update(manager);
  }

  @Override
  public void delete(String login) {
    dao.delete(login);
  }
}
