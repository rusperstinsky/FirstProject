/*
 * Copyright 2010 the original author or authors.
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

package org.gradle.api.internal.file;

import org.gradle.api.Nullable;
import org.gradle.api.UncheckedIOException;
import org.gradle.internal.Factory;
import org.gradle.util.GFileUtils;
import org.gradle.util.GUtil;

import java.io.File;
import java.io.IOException;

public class DefaultTemporaryFileProvider implements TemporaryFileProvider {
    private final Factory<File> baseDirFactory;

    public DefaultTemporaryFileProvider(final Factory<File> fileFactory) {
        this.baseDirFactory = fileFactory;
    }

    public File newTemporaryFile(String... path) {
        return GFileUtils.canonicalise(new File(baseDirFactory.create(), GUtil.join(path, "/")));
    }

    public File createTemporaryFile(String prefix, @Nullable String suffix, String... path) {
        File dir = new File(baseDirFactory.create(), GUtil.join(path, "/"));
        GFileUtils.createDirectory(dir);
        try {
            return File.createTempFile(prefix, suffix, dir);
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public File createTemporaryDirectory(@Nullable String prefix, @Nullable String suffix, @Nullable String... path) {
        File dir = new File(baseDirFactory.create(), GUtil.join(path, "/"));
        GFileUtils.createDirectory(dir);
        try {
            // TODO: This is not a great paradigm for creating a temporary directory.
            // See http://guava-libraries.googlecode.com/svn/tags/release08/javadoc/com/google/common/io/Files.html#createTempDir%28%29 for an alternative.
            File tmpDir = File.createTempFile("gradle", "projectDir", dir);
            tmpDir.delete();
            tmpDir.mkdir();
            return tmpDir;
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }
}
