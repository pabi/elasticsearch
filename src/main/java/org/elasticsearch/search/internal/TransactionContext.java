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

package org.elasticsearch.search.internal;

import org.apache.lucene.util.FixedBitSet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransactionContext {

    private final long id;
    private final Map<Integer, FixedBitSet> visitedDocs = new HashMap<Integer, FixedBitSet>();
    
    private final AtomicBoolean groupCreated = new AtomicBoolean(false);
    private final LinkedList<Map<Integer, FixedBitSet>> netDocs = new LinkedList<Map<Integer, FixedBitSet>>();
    

    public TransactionContext(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
    
    public FixedBitSet getVisitedDocs(int shardId) {
        return getBitSet(shardId, visitedDocs);
    }
    
    public void nextNetDocs(int shardId) {
        netDocs.pop();
    }
    
    public FixedBitSet getFirstNetDocs(int shardId) {
        return getBitSet(shardId, netDocs.peek());
    }
    
    public FixedBitSet getLastNetDocs(int shardId) {
        return getBitSet(shardId, netDocs.peekLast());
    }
    
    private FixedBitSet getBitSet(int shardId, Map<Integer, FixedBitSet> bitsets) {
        if (bitsets.containsKey(shardId)) {
            return bitsets.get(shardId);
        }
        final FixedBitSet fixedBitSet = new FixedBitSet(3000000);
        bitsets.put(shardId, fixedBitSet);
        return fixedBitSet;
    }

    public synchronized void makeNetGroup() {
        if (groupCreated.compareAndSet(false, true)) {            
            netDocs.add(new HashMap<Integer, FixedBitSet>());
        }
    }
    
    public AtomicBoolean getGroupCreated() {
        return groupCreated;
    }
}
