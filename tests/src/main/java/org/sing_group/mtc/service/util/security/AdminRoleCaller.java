/*-
 * #%L
 * Tests
 * %%
 * Copyright (C) 2017 - 2018 Miguel Reboiro-Jato, Adolfo Piñón Blanco,
 *     Hugo López-Fernández, Rosalía Laza Fidalgo, Reyes Pavón Rial,
 *     Francisco Otero Lamas, Adrián Varela Pomar, Carlos Spuch Calvar,
 *     and Tania Rivera Baltanás
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

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

@Stateless(name = "admin-caller")
@RunAs("ADMIN")
@PermitAll
public class AdminRoleCaller implements RoleCaller {
  public <V> V call(Supplier<V> supplier) {
    return supplier.get();
  }

  public void run(Runnable run) {
    run.run();
  }

  public <V> V throwingCall(ThrowingSupplier<V> supplier) throws Throwable {
    return supplier.get();
  }

  public void throwingRun(ThrowingRunnable run) throws Throwable {
    run.run();
  }
}
