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
package org.sing_group.mtc.rest;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import io.swagger.annotations.SwaggerDefinition;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.ReaderListener;
import io.swagger.models.Swagger;
import io.swagger.models.auth.BasicAuthDefinition;

@SwaggerDefinition
@Startup
@Singleton
public class SwaggerConfiguration implements ReaderListener {
  @Resource(name = "java:global/mtc/swagger/version")
  private String version;
  @Resource(name = "java:global/mtc/swagger/schemes")
  private String schemes;
  @Resource(name = "java:global/mtc/swagger/host")
  private String host;
  @Resource(name = "java:global/mtc/swagger/basePath")
  private String basePath;

  @PostConstruct
  public void init() {
    final BeanConfig config = new BeanConfig();
    config.setVersion(this.version);
    config.setSchemes(this.schemes.split(","));
    config.setHost(this.host);
    config.setBasePath(this.basePath);
    config.setResourcePackage(this.getClass().getPackage().getName());
    config.setScan(true);
  }
  
  @Override
  public void beforeScan(Reader reader, Swagger swagger) {
    swagger.addSecurityDefinition("basicAuth", new BasicAuthDefinition());
  }

  @Override
  public void afterScan(Reader reader, Swagger swagger) {}

}
