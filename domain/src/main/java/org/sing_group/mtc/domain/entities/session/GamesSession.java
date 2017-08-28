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
package org.sing_group.mtc.domain.entities.session;

import static java.util.Objects.requireNonNull;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sing_group.mtc.domain.entities.user.Therapist;

@Entity
@Table(name = "session")
public class GamesSession implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "therapistId", referencedColumnName = "id")
  private Therapist therapist;
  
  @OneToMany(
    mappedBy = "session",
    fetch = FetchType.LAZY,
    cascade = { DETACH, MERGE, REFRESH, REMOVE },
    orphanRemoval = true
  )
  private Set<GamesSessionVersion> versions;
  
  public GamesSession() {
    this.id = null;
    this.therapist = null;

    this.versions = new HashSet<>();
  }
  
  public GamesSession(int id, Therapist therapist, Collection<GamesSessionVersion> versions) {
    this.id = id;
    this.versions = new HashSet<>();
    
    this.setTherapist(therapist);
    versions.forEach(this::addVersion);
  }
  
  public Integer getId() {
    return id;
  }
  
  public Stream<GamesSessionVersion> getVersions() {
    return versions.stream();
  }
  
  public boolean addVersion(GamesSessionVersion version) {
    requireNonNull(version, "version can't be null");
    
    if (this.hasVersion(version)) {
      return false;
    } else {
      version.setSession(this);
      return true;
    }
  }
  
  public boolean removeVersion(GamesSessionVersion version) {
    requireNonNull(version, "version can't be null");
    
    if (this.hasVersion(version)) {
      version.setSession(null);
      return true;
    } else {
      return false;
    }
  }
  
  boolean directAddVersion(GamesSessionVersion version) {
    requireNonNull(version, "version can't be null");
    
    return this.versions.add(version);
  }
  
  boolean directRemoveVersion(GamesSessionVersion version) {
    requireNonNull(version, "version can't be null");
    
    return this.versions.remove(version);
  }
  
  public boolean hasVersion(GamesSessionVersion version) {
    return false;
  }
  
  public Therapist getTherapist() {
    return this.therapist;
  }
  
  public void setTherapist(Therapist therapist) {
    if (this.therapist != null) {
      this.therapist.removeSession(this);
      this.therapist = null;
    }
    
    if (therapist != null) {
      this.therapist = therapist;
      this.therapist.addSession(this);
    }
  }
}
