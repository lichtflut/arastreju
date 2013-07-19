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
package org.arastreju.sge.query.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <p>
 *  Engine performing java script to query graph elements.
 * </p>
 *
 * <p>
 *  Created July 17, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class QueryScriptEngine {

    public static final String ARAS_SCRIPT_ENGINE_CONTEXT = "arasScriptEngineContext";

    private static final String BINDING_SCRIPT = "org/arastreju/sge/query/script/query_script_binding.js";

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryScriptEngine.class);

    // ----------------------------------------------------

    private ScriptEngineContext arasCtx;

    // ----------------------------------------------------

    public QueryScriptEngine(ScriptEngineContext arasCtx) throws QueryScriptException {
        this.arasCtx = arasCtx;
    }

    // ----------------------------------------------------

    public void execute(String script) throws QueryScriptException {
        try {
            Context cx = Context.enter();
            Scriptable scope = cx.initStandardObjects();
            scope.put(ARAS_SCRIPT_ENGINE_CONTEXT, scope, arasCtx);
            loadRhinoBinding(scope, cx);
            cx.evaluateString(scope, script, "queryscript", 0, null);
        } catch (Exception e) {
            throw new QueryScriptException("Execution of query script failed.", e);
        } finally {
            Context.exit();
        }
    }

    // ----------------------------------------------------

    private void loadRhinoBinding(Scriptable scope, Context cx) throws QueryScriptException {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(BINDING_SCRIPT)) {
            cx.evaluateReader(scope, new InputStreamReader(in, "UTF-8"), BINDING_SCRIPT, 0, null);
        } catch (IOException e) {
            throw new QueryScriptException("Failed to initialize script engine.", e);
        }
    }

}
