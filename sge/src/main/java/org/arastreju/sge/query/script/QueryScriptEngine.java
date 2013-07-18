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

    private ScriptEngine engine;

    // ----------------------------------------------------

    public QueryScriptEngine(ScriptEngineContext arasCtx) throws QueryScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
        engine.put(ARAS_SCRIPT_ENGINE_CONTEXT, arasCtx);
        loadJsBinding(engine);
    }

    // ----------------------------------------------------

    public void execute(String script) throws QueryScriptException {
        try {
            engine.eval(script);
        } catch (ScriptException e) {
            throw new QueryScriptException("Failed to execute script.", e);
        }
    }

    // ----------------------------------------------------

    private void loadJsBinding(ScriptEngine engine) throws QueryScriptException {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(BINDING_SCRIPT)) {
            engine.eval(new InputStreamReader(in, "UTF-8"));
        } catch (IOException | ScriptException e) {
            throw new QueryScriptException("Failed to initialize script engine.", e);
        }
    }

}
