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
import static javax.persistence.GenerationType.IDENTITY;
import static org.sing_group.fluent.checker.Checks.requireStringSize;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "institution")
public class Institution implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;
  
  @Column(name = "name", length = 255, columnDefinition = "VARCHAR(255)", unique = true, nullable = false)
  private String name;
  
  @Column(name = "description", length = 1023, columnDefinition = "VARCHAR(1023)", nullable = true)
  private String description;
  
  @Column(name = "address", length = 1023, columnDefinition = "VARCHAR(1023)", nullable = true)
  private String address;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
    name = "manager",
    referencedColumnName = "login",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_institution_manager")
  )
  private Manager manager;
  
  @OneToMany(mappedBy = "institution", fetch =  FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Therapist> therapists;

  // For JPA
  Institution() {}
  
  public Institution(Integer id, String name, Manager manager) {
    this(id, name, manager, null, null, null);
  }
  
  public Institution(String name, Manager manager) {
    this(name, manager, null, null, null);
  }
  
  public Institution(String name, Manager manager, String description, String address, Collection<Therapist> therapists) {
    this(null, name, manager, description, address, therapists);
  }

  public Institution(Integer id, String name, Manager manager, String description, String address, Collection<Therapist> therapists) {
    this.id = id;
    this.name = name;
    this.setDescription(description);
    this.setAddress(address);
    this.setManager(manager);
    
    this.therapists = therapists == null ? new HashSet<>() : new HashSet<>(therapists);
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = requireStringSize(name, 1, 255, "'name' should have a length between 1 and 100");
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  public void setDescription(String description) {
    this.description = description == null
      ? null
      : requireStringSize(name, 0, 1023, "'description' should be null or have a length between 0 and 1023");
  }

  public Optional<String> getAddress() {
    return Optional.ofNullable(address);
  }

  public void setAddress(String address) {
    this.address = address == null
      ? null
      : requireStringSize(name, 0, 1023, "'address' should be null or have a length between 0 and 1023");
  }
  
  public Optional<Manager> getManager() {
    return Optional.ofNullable(manager);
  }

  public void setManager(Manager manager) {
    if (this.manager != null) {
      this.manager.directRemoveInstitution(this);
      this.manager = null;
    }
    
    if (manager != null) {
      this.manager = manager;
      this.manager.directAddInstitution(this);
    }
  }
  
  public Stream<Therapist> getTherapists() {
    return therapists.stream();
  }

  public boolean hasTherapist(Therapist therapist) {
    return this.therapists.contains(therapist);
  }
  
  public boolean addTherapist(Therapist therapist) {
    requireNonNull(therapist, "therapist can't be null");
    
    if (this.hasTherapist(therapist)) {
      return false;
    } else {
      therapist.setInstitution(this);
      
      return true;
    }
  }
  
  public boolean removeTherapist(Therapist therapist) {
    requireNonNull(therapist, "therapist can't be null");

    if (this.hasTherapist(therapist)) {
      therapist.setInstitution(this);
      
      return true;
    } else {
      return false;
    }
  }

  protected boolean directAddTherapist(Therapist therapist) {
    return this.therapists.add(therapist);
  }

  protected boolean directRemoveTherapist(Therapist therapist) {
    return this.therapists.remove(therapist);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Institution other = (Institution) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
