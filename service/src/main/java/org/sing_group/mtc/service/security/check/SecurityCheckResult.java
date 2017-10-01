/*
 * #%L
 * Service
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
package org.sing_group.mtc.service.security.check;

import java.util.Optional;

public class SecurityCheckResult {
  private final boolean valid;
  private final String reason;
  
  public static SecurityCheckResult valid() {
    return new SecurityCheckResult(true, null);
  }
  
  public static SecurityCheckResult invalid(String reason) {
    return new SecurityCheckResult(false, reason);
  }
  
  protected SecurityCheckResult(boolean isValid, String reason) {
    this.valid = isValid;
    this.reason = reason;
  }
  
  public boolean isValid() {
    return this.valid;
  }
  
  public Optional<String> getReason() {
    return Optional.ofNullable(this.reason);
  }
}