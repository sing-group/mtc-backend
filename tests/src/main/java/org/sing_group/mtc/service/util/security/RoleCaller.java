/*
 * #%L
 * Tests
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
package org.sing_group.mtc.service.util.security;

import java.util.function.Supplier;

import javax.ejb.Local;

@Local
public interface RoleCaller {
  public <V> V call(Supplier<V> supplier);

  public void run(Runnable run);

  public <V> V throwingCall(ThrowingSupplier<V> supplier) throws Throwable;

  public void throwingRun(ThrowingRunnable run) throws Throwable;

  public interface ThrowingRunnable {
    public void run() throws Throwable;
  }

  public interface ThrowingSupplier<V> {
    public V get() throws Throwable;
  }
}
