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

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.sing_group.fluent.compare.Compare;
import org.sing_group.mtc.domain.entities.i18n.I18N;
import org.sing_group.mtc.domain.entities.session.SessionVersion.SessionVersionId;

@Entity
@Table(name = "session_version")
@IdClass(SessionVersionId.class)
public class SessionVersion implements Comparable<SessionVersion> {
  @Id
  @Column(name = "sessionId", insertable = false, updatable = false)
  private int sessionId;
  
  @Id
  @Column(name = "version")
  private int version;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "sessionId", referencedColumnName = "id", nullable = false)
  private Session session;
  
  @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("gameOrder ASC")
  private SortedSet<SessionGame> games;
  
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "session_i18n",
    joinColumns = {
      @JoinColumn(name = "sessionId", referencedColumnName = "sessionId"),
      @JoinColumn(name = "sessionVersion", referencedColumnName = "version")
    },
    inverseJoinColumns = {
      @JoinColumn(name = "i18nLocale", referencedColumnName = "locale"),
      @JoinColumn(name = "i18nKey", referencedColumnName = "messageKey")
    }
  )
  private Set<I18N> messages;
  
  @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AssignedSession> assigned;
  
  public Session getSession() {
    return session;
  }
  
  public int getSessionId() {
    return sessionId;
  }
  
  public int getVersion() {
    return version;
  }
  
  public Stream<AssignedSession> getAssigned() {
    return this.assigned.stream();
  }
  
  public Stream<SessionGame> getGames() {
    return this.games.stream();
  }
  
  public Stream<I18N> getMessages() {
    return this.messages.stream();
  }
  
  @Override
  public int compareTo(SessionVersion o) {
    return Compare.objects(this, o)
      .by(SessionVersion::getSessionId)
      .thenBy(SessionVersion::getVersion)
    .andGet();
  }
  
  public static class SessionVersionId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int sessionId;
    
    private int version;
    
    // For JPA
    SessionVersionId() {}
    
    public SessionVersionId(int sessionId, int version) {
      this.sessionId = sessionId;
      this.version = version;
    }

    public int getSessionId() {
      return sessionId;
    }
    
    public int getVersion() {
      return version;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + sessionId;
      result = prime * result + version;
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
      SessionVersionId other = (SessionVersionId) obj;
      if (sessionId != other.sessionId)
        return false;
      if (version != other.version)
        return false;
      return true;
    }
  }
}
