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

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * An institution manager.
 * 
 * @author Miguel Reboiro-Jato
 */
@Entity
@Table(name = "manager")
@DiscriminatorValue("MANAGER")
public class Manager extends IdentifiedUser {
  private static final long serialVersionUID = 1L;
  
  @OneToMany(mappedBy = "manager", fetch =  FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Institution> institutions;

  // Required for JPA
  Manager() {}

  public Manager(String login, String email, String password) {
    this(login, email, password, null, null, null);
  }

  public Manager(String login, String email, String password, boolean encodedPassword) {
    this(login, email, password, null, null, null, encodedPassword);
  }

  public Manager(String login, String email, String password, String name, String surname, Collection<Institution> institutions) {
    super(login, email, password, name, surname);
    
    this.institutions = institutions == null
      ? new HashSet<>()
      : new HashSet<>(institutions);
  }

  public Manager(String login, String email, String password, String name, String surname, Collection<Institution> institutions, boolean encodedPassword) {
    super(login, email, password, name, surname, encodedPassword);
    
    this.institutions = institutions == null ? new HashSet<>() : new HashSet<>(institutions);
  }
  
  public Stream<Institution> getInstitutions() {
    return institutions.stream();
  }

  public boolean hasInstitution(Institution institution) {
    return this.institutions.contains(institution);
  }
  
  public boolean addInstitution(Institution institution) {
    requireNonNull(institution, "institution can't be null");
    
    if (this.hasInstitution(institution)) {
      return false;
    } else {
      institution.setManager(this);
      
      return true;
    }
  }
  
  public boolean removeInstitution(Institution institution) {
    requireNonNull(institution, "institution can't be null");

    if (this.hasInstitution(institution)) {
      institution.setManager(this);
      
      return true;
    } else {
      return false;
    }
  }

  protected boolean directRemoveInstitution(Institution institution) {
    return this.institutions.remove(institution);
  }

  protected boolean directAddInstitution(Institution institution) {
    return this.institutions.remove(institution);
  }
}
