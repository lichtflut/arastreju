/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.io;

import org.arastreju.sge.naming.SimpleNamespace;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 *  Test case for namespace map.
 * </p>
 *
 * <p>
 *  Created July 18, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class NamespaceMapTest {

    @Test
    public void shouldQualifyRegisteredPrefixes() {
        NamespaceMap map = new NamespaceMap();
        map.addNamespace(new SimpleNamespace("http://example.org/base/", "base"));
        map.addNamespace(new SimpleNamespace("http://example.org/base/a/", "a"));
        map.addNamespace(new SimpleNamespace("http://example.org/base/b/", "b"));
        map.addNamespace(new SimpleNamespace("http://example.org/base/a/1#", "a:1"));

        Assert.assertEquals("http://example.org/base/b/Stuff", map.qualify("b:Stuff"));
        Assert.assertEquals("http://example.org/base/Stuff", map.qualify("base:Stuff"));
        Assert.assertEquals("http://example.org/base/a/1#Stuff", map.qualify("a:1:Stuff"));
        Assert.assertEquals("http://example.org/base/a/Stuff", map.qualify("a:Stuff"));
    }

    @Test
    public void shouldSubstitutePrefixesInString() {
        NamespaceMap map = new NamespaceMap();
        map.addNamespace(new SimpleNamespace("http://example.org/base/", "base"));
        map.addNamespace(new SimpleNamespace("http://example.org/base/a/", "a"));
        map.addNamespace(new SimpleNamespace("http://example.org/base/b/", "b"));
        map.addNamespace(new SimpleNamespace("http://example.org/base/a/1#", "a:1"));

        String input = "query(QN='base:Person').walk('a:1:hasAddress').walk('b:isInCity');";

        Assert.assertEquals(
                "query(QN='http://example.org/base/Person').walk('http://example.org/base/a/1#hasAddress')" +
                        ".walk('http://example.org/base/b/isInCity');",
                map.replaceAll(input));
    }

}
