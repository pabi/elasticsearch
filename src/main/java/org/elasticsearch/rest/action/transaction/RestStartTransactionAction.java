/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.rest.action.transaction;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.transaction.StartTransactionRequest;
import org.elasticsearch.action.transaction.StartTransactionResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;

import static org.elasticsearch.rest.RestRequest.Method.POST;

public class RestStartTransactionAction extends BaseRestHandler {

    @Inject
    protected RestStartTransactionAction(Settings settings, RestController controller, Client client) {
        super(settings, controller, client);
        controller.registerHandler(POST, "/_transaction", this);
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        System.out.println("TRANSACTION START");
        StartTransactionRequest transactionStartRequest = new StartTransactionRequest();
        client.startTransaction(transactionStartRequest, new ActionListener<StartTransactionResponse>() {
            
            @Override
            public void onResponse(StartTransactionResponse response) {
                System.out.println("RESPONSE: " + response);
            }
            
            @Override
            public void onFailure(Throwable e) {
                System.out.println("FAILURE: " + e);
            }
            
        });
    }

}
