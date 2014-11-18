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

package org.elasticsearch.index.search.child;

public class SortLimitedDoc implements Comparable<SortLimitedDoc>{

    final int docId;
    final double value;
    
    public SortLimitedDoc(int docId, double value) {
        this.docId = docId;
        this.value = value;
    }
    
    public int getDocId() {
        return docId;
    }
    
    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(SortLimitedDoc o) {
        return (int) Math.signum(o.getValue() - getValue());
    }
}
