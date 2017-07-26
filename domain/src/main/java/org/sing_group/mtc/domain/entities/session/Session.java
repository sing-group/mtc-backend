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

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSortedSet;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.sing_group.mtc.domain.entities.i18n.I18N;

@Entity
@Table(name = "session")
public class Session implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
  @OrderBy("gameOrder ASC")
  private SortedSet<SessionGame> games;
  
  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "session_i18n",
    joinColumns = @JoinColumn(name = "sessionId", referencedColumnName = "id"),
    inverseJoinColumns = {
      @JoinColumn(name = "i18nLocale", referencedColumnName = "locale"),
      @JoinColumn(name = "i18nKey", referencedColumnName = "messageKey")
    }
  )
  private List<I18N> messages;
  
  public SortedSet<SessionGame> getGames() {
    return unmodifiableSortedSet(games);
  }
  
  public List<I18N> getMessages() {
    return unmodifiableList(messages);
  }
}
