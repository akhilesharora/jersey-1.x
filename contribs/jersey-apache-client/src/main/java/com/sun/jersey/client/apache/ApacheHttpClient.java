/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
package com.sun.jersey.client.apache;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

/**
 * Apache HTTP client.
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class ApacheHttpClient extends Client {
    
    /**
     * Create a new client instance.
     *
     */
    public ApacheHttpClient() {
        this(createDefaultClientHander(), new DefaultClientConfig(), null);
    }

    /**
     * Create a new client instance.
     *
     * @param root the root client handler for dispatching a request and
     *        returning a response.
     */
    public ApacheHttpClient(ApacheHttpClientHandler root) {
        this(root, new DefaultClientConfig(), null);
    }

    /**
     * Create a new client instance with a client configuration.
     *
     * @param root the root client handler for dispatching a request and
     *        returning a response.
     * @param config the client configuration.
     */
    public ApacheHttpClient(ApacheHttpClientHandler root, ClientConfig config) {
        this(root, config, null);
    }

    /**
     * Create a new instance with a client configuration and a
     * component provider.
     *
     * @param root the root client handler for dispatching a request and
     *        returning a response.
     * @param config the client configuration.
     * @param provider the IoC component provider factory.
     */
    public ApacheHttpClient(ApacheHttpClientHandler root, ClientConfig config,
            IoCComponentProviderFactory provider) {
        super(root, config, provider);

        HttpClient client = root.getHttpClient();
        
        client.getParams().setAuthenticationPreemptive(
                config.getPropertyAsFeature(ApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION));

        final Integer connectTimeout = (Integer)config.getProperty(ApacheHttpClientConfig.PROPERTY_CONNECT_TIMEOUT);
        if (connectTimeout != null) {
            client.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout);
        }
    }

    /**
     * Create a default client.
     *
     * @return a default client.
     */
    public static ApacheHttpClient create() {
        return new ApacheHttpClient(createDefaultClientHander());
    }

    /**
     * Create a default client with client configuration.
     *
     * @param cc the client configuration.
     * @return a default client.
     */
    public static ApacheHttpClient create(ClientConfig cc) {
        return new ApacheHttpClient(createDefaultClientHander(), cc);
    }

    /**
     * Create a default client with client configuration and component provider.
     *
     * @param cc the client configuration.
     * @param provider the IoC component provider factory.
     * @return a default client.
     */
    public static ApacheHttpClient create(ClientConfig cc, IoCComponentProviderFactory provider) {
        return new ApacheHttpClient(createDefaultClientHander(), cc, provider);
    }

    /**
     * Create a default Apache HTTP client handler.
     *
     * @return a default Apache HTTP client handler.
     */
    private static ApacheHttpClientHandler createDefaultClientHander() {
        final HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());

        return new ApacheHttpClientHandler(client);
    }
}