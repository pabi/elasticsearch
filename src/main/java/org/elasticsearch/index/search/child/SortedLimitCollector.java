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

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.util.FixedBitSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SortedLimitCollector extends UniqueCountCollector {

    private FieldCache.Doubles currentReaderValues;
    private final String field;
    private final List<SortLimitedDoc> docValues = new ArrayList<SortLimitedDoc>();

    public SortedLimitCollector(FixedBitSet visited, FixedBitSet net, String field) {
        super(visited, net);
        this.field = field;
    }

    @Override
    public void collect(int doc) throws IOException {
        super.collect(doc);
        double docValue = currentReaderValues.get(doc);
        docValues.add(new SortLimitedDoc(docBase + doc, docValue));
    }

    @Override
    public void setNextReader(AtomicReaderContext context) throws IOException {
        super.setNextReader(context);
        currentReaderValues = getDoubleValues(context, field);
    }

    protected FieldCache.Doubles getDoubleValues(AtomicReaderContext context, String field) throws IOException {
        return FieldCache.DEFAULT.getDoubles(context.reader(), field, null, false);
    }

    public List<SortLimitedDoc> getDocValues() {
        return docValues;
    }

}
