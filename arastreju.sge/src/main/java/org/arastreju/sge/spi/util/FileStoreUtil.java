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
package org.arastreju.sge.spi.util;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 *  Utilities for handling of file stores.
 * </p>
 *
 * <p>
 *  Created Feb. 22, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class FileStoreUtil {

    public static String prepareTempStore() throws IOException {
        return prepareTempStore("default");
    }

    public static String prepareTempStore(String prefix) throws IOException {
        final File temp = File.createTempFile(prefix, Long.toString(System.nanoTime()));
        if (!temp.delete()) {
            throw new IOException("Could not delete temp file: "
                    + temp.getAbsolutePath());
        }
        if (!temp.mkdir()) {
            throw new IOException("Could not create temp directory: "
                    + temp.getAbsolutePath());
        }

        return temp.getAbsolutePath();
    }

}
