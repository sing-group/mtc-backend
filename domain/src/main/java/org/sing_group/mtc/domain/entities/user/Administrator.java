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
package org.sing_group.mtc.domain.entities.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An administrator of the application.
 * 
 * @author Miguel Reboiro-Jato
 */
@Entity
@Table(name = "administrator")
@DiscriminatorValue("ADMIN")
public class Administrator extends IdentifiedUser {
  private static final long serialVersionUID = 1L;

  // Required for JPA
  Administrator() {}

  public Administrator(String login, String email, String password) {
    super(login, email, password);
  }

  public Administrator(String login, String email, String password, boolean encodedPassword) {
    super(login, email, password, encodedPassword);
  }

  public Administrator(String login, String email, String password, String name, String surname, boolean encodedPassword) {
    super(login, email, password, name, surname, encodedPassword);
  }

  public Administrator(String login, String email, String password, String name, String surname) {
    super(login, email, password, name, surname);
  }

}
