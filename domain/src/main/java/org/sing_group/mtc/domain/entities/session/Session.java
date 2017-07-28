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

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
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
public class Session implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "therapistId", referencedColumnName = "id")
  private Therapist therapist;
  
  @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<SessionVersion> versions;
  
  public Stream<SessionVersion> getVersions() {
    return versions.stream();
  }
  
  public Therapist getTherapist() {
    if (this.therapist == null) 
      throw new IllegalStateException(
        "This entity does not have a therapist assigned. "
        + "This should happen only when it will be removed."
      );
    
    return therapist;
  }
  
  public void setTherapist(Therapist therapist) {
    requireNonNull(therapist, "'therapist' can't be null");
    
    if (therapist.addSession(this)) {
      this.therapist = therapist;
    }
  }
  
  public void removeTherapist() {
    if (this.therapist == null) 
      throw new IllegalStateException(
        "This entity does not have a therapist assigned. "
        + "This should happen only when it will be removed."
      );
    
    if (this.therapist.removeSession(this)) {
      this.therapist = null;
    }
  }
}
