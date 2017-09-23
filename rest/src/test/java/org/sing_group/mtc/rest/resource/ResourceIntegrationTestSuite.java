/*
 * #%L
 * REST
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
package org.sing_group.mtc.rest.resource;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.sing_group.mtc.rest.resource.game.session.GamesSessionResourceIntegrationTest;
import org.sing_group.mtc.rest.resource.user.AdministratorResourceIntegrationTest;
import org.sing_group.mtc.rest.resource.user.InstitutionResourceIntegrationTest;
import org.sing_group.mtc.rest.resource.user.ManagerResourceIntegrationTest;
import org.sing_group.mtc.rest.resource.user.PatientResourceIntegrationTest;
import org.sing_group.mtc.rest.resource.user.TherapistResourceIntegrationTest;
import org.sing_group.mtc.rest.resource.user.UserResourceIntegrationTest;

@SuiteClasses({
	UserResourceIntegrationTest.class,
	AdministratorResourceIntegrationTest.class,
	ManagerResourceIntegrationTest.class,
	TherapistResourceIntegrationTest.class,
	PatientResourceIntegrationTest.class,
	InstitutionResourceIntegrationTest.class,
	GamesSessionResourceIntegrationTest.class
})
@RunWith(Suite.class)
public class ResourceIntegrationTestSuite {
}
