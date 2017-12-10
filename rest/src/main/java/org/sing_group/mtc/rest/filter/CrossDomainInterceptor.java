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
package org.sing_group.mtc.rest.filter;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@CrossDomain
@Interceptor
@Dependent
public class CrossDomainInterceptor {
  @Inject
  private HttpServletRequest request;

  @AroundInvoke
  protected Object invoke(InvocationContext ctx) throws Exception {
    final Object target = ctx.getTarget();
    
    if (request.getHeader("Origin") != null) {
      final CrossDomain annotation = getAnnotation(ctx.getMethod(), target.getClass());
      
      final Response response = (Response) ctx.proceed();
      final ResponseHeaderBuilder newResponse = new ResponseHeaderBuilder(response);
      
      buildCorsHeaders(annotation, newResponse::header, request::getHeader);
      
      return newResponse.build();
    } else {
      return ctx.proceed();
    }
  }
  
  private static class ResponseHeaderBuilder {
    private ResponseBuilder builder;

    public ResponseHeaderBuilder(Response response) {
      this.builder = requireNonNull(Response.fromResponse(response));
    }
    
    public void header(String name, Object value) {
      this.builder = this.builder.header(name, value);
    }
    
    public Response build() {
      return this.builder.build();
    }
  }

  public static CrossDomain getAnnotation(Method method, Class<?> targetClass) {
    CrossDomain annotation = null;
    
    if (method != null) {
      method.getAnnotation(CrossDomain.class);
    }
    
    if (annotation == null && targetClass != null) {
      annotation = targetClass.getAnnotation(CrossDomain.class);
    }
    
    return annotation;
  }
  
  protected static void buildCorsPreFlightHeaders(
    final CrossDomain annotation,
    final BiConsumer<String, Object> responseHeaders,
    final Function<String, String> requestHeadersProvider
  ) {
    processOrigin(annotation, responseHeaders, requestHeadersProvider);
    processMaxAge(annotation, responseHeaders);
    processAllowedCredentials(annotation, responseHeaders);
    processAllowedMethods(annotation, responseHeaders);
    processAllowedHeaders(annotation, responseHeaders, requestHeadersProvider, true);
  }
  
  protected static void buildCorsHeaders(
    final CrossDomain annotation,
    final BiConsumer<String, Object> responseHeaders,
    final Function<String, String> requestHeadersProvider
  ) {
    processOrigin(annotation, responseHeaders, requestHeadersProvider);
    processAllowedCredentials(annotation, responseHeaders);
    processAllowedHeaders(annotation, responseHeaders, requestHeadersProvider, false);
  }

  protected static void processAllowedHeaders(
    final CrossDomain annotation,
    final BiConsumer<String, Object> headerConsumer,
    final Function<String, String> requestHeadersProvider,
    final boolean preflight
  ) {
    final List<String> allowedHeaders = new ArrayList<>();
    
    if (annotation.allowRequestHeaders()) {
      final String requestHeaders = requestHeadersProvider.apply("Access-Control-Request-Headers");
      
      if (requestHeaders != null)
        allowedHeaders.add(requestHeaders);
    }
    stream(annotation.allowedHeaders()).forEach(allowedHeaders::add);

    if (!allowedHeaders.isEmpty()) {
      final String header = preflight
        ? "Access-Control-Allow-Headers"
        : "Access-Control-Expose-Headers";
      
      headerConsumer.accept(header, String.join(", ", allowedHeaders));
    }
  }

  private static void processOrigin(
    final CrossDomain annotation,
    final BiConsumer<String, Object> headerConsumer,
    final Function<String, String> requestHeadersProvider
  ) {
    if (annotation.allowedOrigin() == null) {
      final String originHeader = requestHeadersProvider.apply("Origin");
      
      if (originHeader != null) {
        headerConsumer.accept("Access-Control-Allow-Origin", originHeader);
      }
    } else {
      headerConsumer.accept("Access-Control-Allow-Origin", annotation.allowedOrigin());
    }
  }

  private static void processMaxAge(final CrossDomain annotation, final BiConsumer<String, Object> headerConsumer) {
    headerConsumer.accept("Access-Control-Max-Age", Integer.toString(annotation.maxAge() < 0 ? -1 : annotation.maxAge()));
  }

  private static void processAllowedCredentials(final CrossDomain annotation, final BiConsumer<String, Object> headerConsumer) {
    headerConsumer.accept("Access-Control-Allow-Credentials", Boolean.toString(annotation.allowCredentials()));
  }

  private static void processAllowedMethods(final CrossDomain annotation, final BiConsumer<String, Object> headerConsumer) {
    headerConsumer.accept("Access-Control-Allow-Methods", String.join(", ", annotation.allowedMethods()));
  }
}
