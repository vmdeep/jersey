/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.jersey.server.internal.inject;

import javax.ws.rs.BeanParam;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.server.model.Parameter;

import org.glassfish.hk2.api.ServiceLocator;

/**
 * Value factory provider for {@link BeanParam bean parameters}.
 *
 * @author Miroslav Fuksa (miroslav.fuksa at oracle.com)
 */
@Singleton
final class BeanParamValueFactoryProvider extends AbstractValueFactoryProvider {

    @Inject
    private ServiceLocator locator;

    /**
     * {@link InjectionResolver Injection resolver} for {@link BeanParam bean parameters}.
     */
    @Singleton
    static final class InjectionResolver extends ParamInjectionResolver<BeanParam> {

        /**
         * Creates new resolver.
         */
        public InjectionResolver() {
            super(BeanParamValueFactoryProvider.class);
        }
    }

    private static final class BeanParamValueFactory extends AbstractHttpContextValueFactory<Object> {
        private final Parameter parameter;
        private final ServiceLocator locator;

        private BeanParamValueFactory(ServiceLocator locator, Parameter parameter) {
            this.locator = locator;
            this.parameter = parameter;
        }

        @Override
        protected Object get(HttpContext context) {
            return Injections.getOrCreate(locator, parameter.getRawType());
        }
    }


    /**
     * Creates new instance initialized from parameters injected by HK2.
     * @param mpep Multivalued parameter extractor provider.
     * @param injector HK2 Service locator.
     */
    @Inject
    public BeanParamValueFactoryProvider(MultivaluedParameterExtractorProvider mpep, ServiceLocator injector) {
        super(mpep, injector, Parameter.Source.BEAN_PARAM);
    }

    @Override
    public AbstractHttpContextValueFactory<?> createValueFactory(Parameter parameter) {
        return new BeanParamValueFactory(locator, parameter);
    }
}
