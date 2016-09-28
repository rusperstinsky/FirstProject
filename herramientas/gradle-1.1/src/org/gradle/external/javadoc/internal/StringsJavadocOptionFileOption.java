/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.external.javadoc.internal;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * @author Tom Eyckmans
 */
public class StringsJavadocOptionFileOption extends AbstractListJavadocOptionFileOption<List<String>> {
    protected StringsJavadocOptionFileOption(String option, String joinBy) {
        super(option, new ArrayList<String>(), joinBy);
    }

    protected StringsJavadocOptionFileOption(String option, List<String> value, String joinBy) {
        super(option, value, joinBy);
    }

    public void writeCollectionValue(JavadocOptionFileWriterContext writerContext) throws IOException {
        writerContext.writeValuesOption(option, value, joinBy);
    }
}
