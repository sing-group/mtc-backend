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
package org.sing_group.mtc.rest.resource.entity.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.mtc.domain.entities.user.RoleType;

@XmlRootElement(name = "admin-edition-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministratorEditionData extends IdentifiedUserEditionData {
  private static final long serialVersionUID = 1L;

  AdministratorEditionData() {
    super();
  }
  
  public AdministratorEditionData(String login, String password, String email, String name, String surname) {
    super(login, password, email, name, surname, RoleType.ADMIN);
  }

  @Override
  public String toString() {
    return "AdministratorEditionData [login=" + login + ", email=" + email + ", name=" + name + ", surname=" + surname + ", password=" + password + "]";
  }

  
}
