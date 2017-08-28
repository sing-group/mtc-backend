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
import static java.util.stream.Collectors.toSet;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
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
import org.sing_group.mtc.domain.entities.i18n.I18NLocale;
import org.sing_group.mtc.domain.entities.i18n.LocalizedMessage;
import org.sing_group.mtc.domain.entities.session.GamesSessionVersion.GamesSessionVersionId;

@Entity
@Table(name = "session_version")
@IdClass(GamesSessionVersionId.class)
public class GamesSessionVersion implements Comparable<GamesSessionVersion>, Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "sessionId")
  private Integer sessionId;
  
  @Id
  @Column(name = "version")
  private int version;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "sessionId", referencedColumnName = "id", insertable = false, updatable = false)
  private GamesSession session;
  
  @OneToMany(
    mappedBy = "session",
    fetch = FetchType.LAZY,
    cascade = { DETACH, MERGE, REFRESH, REMOVE },
    orphanRemoval = true
  )
  @OrderBy("gameOrder ASC")
  private SortedSet<GameConfigurationForSession> gameConfigurations;
  
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "session_i18n",
    joinColumns = {
      @JoinColumn(name = "sessionId", referencedColumnName = "sessionId", insertable = false, updatable = false),
      @JoinColumn(name = "sessionVersion", referencedColumnName = "version", insertable = false, updatable = false)
    },
    inverseJoinColumns = {
      @JoinColumn(name = "i18nLocale", referencedColumnName = "locale"),
      @JoinColumn(name = "i18nKey", referencedColumnName = "messageKey")
    }
  )
  private Set<I18N> messages;
  
  @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AssignedGamesSession> assigned;
  
  GamesSessionVersion() {}
  
  public GamesSessionVersion(
    Map<I18NLocale, String> nameMessages,
    Map<I18NLocale, String> descriptionMessages
  ) {
    this(1, nameMessages, descriptionMessages);
  }
  
  public GamesSessionVersion(
    int version,
    Map<I18NLocale, String> nameMessages,
    Map<I18NLocale, String> descriptionMessages
  ) {
    this.version = version;
    this.session = null;
    this.sessionId = null;
    
    this.gameConfigurations = new TreeSet<>();
    this.assigned = new HashSet<>();
    
    this.messages = Stream.concat(
      new LocalizedMessage(this.getNameKey(), nameMessages).toI18N(),
      new LocalizedMessage(this.getDescriptionKey(), descriptionMessages).toI18N()
    ).collect(toSet());
  }
  
  public GamesSessionVersion(
    GamesSession session,
    int version,
    Map<I18NLocale, String> nameMessages,
    Map<I18NLocale, String> descriptionMessages,
    Collection<GameConfigurationForSession> gameConfigs,
    Collection<AssignedGamesSession> assigned
  ) {
    this.version = version;
    this.gameConfigurations = new TreeSet<>();
    this.assigned = new HashSet<>();
    
    this.setSession(session);
    
    this.messages = Stream.concat(
      new LocalizedMessage(this.getNameKey(), nameMessages).toI18N(),
      new LocalizedMessage(this.getDescriptionKey(), descriptionMessages).toI18N()
    ).collect(toSet());
    
    gameConfigs.forEach(this::addGameConfiguration);
    assigned.forEach(this::addAssigned);
  }
  
  private Integer getSessionId() {
    return this.sessionId;
  }
  
  private void setSessionId(Integer sessionId) {
    final String currentNameKey = this.getNameKey();
    final String currentDescriptionKey = this.getDescriptionKey();
    
    this.sessionId = sessionId;
    
    final Map<String, String> keyReplacement = new HashMap<>();
    keyReplacement.put(currentNameKey, this.getNameKey());
    keyReplacement.put(currentDescriptionKey, this.getDescriptionKey());
    
    if (this.messages != null) {
      for (I18N message : this.messages) {
        final String messageKey = message.getKey();
        
        if (keyReplacement.containsKey(messageKey)) {
          message.setKey(keyReplacement.get(messageKey));
        }
      }
    }
  }
  
  private String getKeyPrefix() {
    return "session." + this.sessionId + ".version." + this.version;
  }
  
  public String getNameKey() {
    return this.getKeyPrefix() + ".name";
  }
  
  public String getDescriptionKey() {
    return this.getKeyPrefix() + ".description";
  }

  public int getVersion() {
    return version;
  }
  
  public LocalizedMessage getName() {
    return LocalizedMessage.from(this.getNameKey(), this.messages);
  }
  
  public LocalizedMessage getDescription() {
    return LocalizedMessage.from(this.getDescriptionKey(), this.messages);
  }
  
  public void setSession(GamesSession session) {
    if (this.session != null) {
      this.session.directRemoveVersion(this);
      this.session = null;
      this.setSessionId(null);
    }
    
    if (session != null) {
      this.session = session;
      this.setSessionId(this.session.getId());
      this.session.directAddVersion(this);
    }
  }
  
  public GamesSession getSession() {
    return this.session;
  }
  
  public Stream<GameConfigurationForSession> getGameConfigurations() {
    return this.gameConfigurations.stream();
  }
  
  public boolean hasGameConfiguration(GameConfigurationForSession gameConfiguration) {
    return this.gameConfigurations.contains(gameConfiguration);
  }
  
  public boolean addGameConfiguration(GameConfigurationForSession config) {
    requireNonNull(config, "config can't be null");
    
    if (this.hasGameConfiguration(config)) {
      return false;
    } else {
      config.setSession(this);
      return true;
    }
  }
  
  public boolean removeGameConfiguration(GameConfigurationForSession config) {
    requireNonNull(config, "config can't be null");
    
    if (this.hasGameConfiguration(config)) {
      config.setSession(null);
      return true;
    } else {
      return false;
    }
  }
  
  protected boolean directAddGameConfiguration(GameConfigurationForSession config) {
    return this.gameConfigurations.add(config);
  }
  
  protected boolean directRemoveGameConfiguration(GameConfigurationForSession config) {
    return this.gameConfigurations.remove(config);
  }
  
  public Stream<AssignedGamesSession> getAssigned() {
    return this.assigned.stream();
  }
  
  public boolean hasAssigned(AssignedGamesSession assigned) {
    return this.assigned.contains(assigned);
  }
  
  public void addAssigned(AssignedGamesSession assigned) {
    requireNonNull(assigned, "assigned can't be null");
    
    assigned.setSession(this);
  }
  
  public void removeAssigned(AssignedGamesSession assigned) {
    requireNonNull(assigned, "assigned can't be null");
    
    assigned.setSession(null);
  }
  
  protected boolean directAddAssigned(AssignedGamesSession assigned) {
    return this.assigned.add(assigned);
  }
  
  protected boolean directRemoveAssigned(AssignedGamesSession assigned) {
    return this.assigned.remove(assigned);
  }
  
  @Override
  public int compareTo(GamesSessionVersion o) {
    return Compare.objects(this, o)
      .by(GamesSessionVersion::getSessionId)
      .thenBy(GamesSessionVersion::getVersion)
    .andGet();
  }

  public static class GamesSessionVersionId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int sessionId;
    
    private int version;
    
    // For JPA
    GamesSessionVersionId() {}
    
    public GamesSessionVersionId(int sessionId, int version) {
      this.sessionId = sessionId;
      this.version = version;
    }

    public int getSessionId() {
      return sessionId;
    }
    
    public void setSessionId(int sessionId) {
      this.sessionId = sessionId;
    }
    
    public int getVersion() {
      return version;
    }
    
    public void setVersion(int version) {
      this.version = version;
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
      GamesSessionVersionId other = (GamesSessionVersionId) obj;
      if (sessionId != other.sessionId)
        return false;
      if (version != other.version)
        return false;
      return true;
    }
  }
}
