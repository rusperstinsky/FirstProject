/*
 * Copyright 2011 the original author or authors.
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
package org.gradle.api.internal.artifacts.repositories.layout;

import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.internal.artifacts.repositories.PatternBasedResolver;

import java.net.URI;

/**
 * A Repository Layout that applies the following patterns:
 * <ul>
 *     <li>Artifacts: $baseUri/{@value IvyArtifactRepository#GRADLE_ARTIFACT_PATTERN}</li>
 *     <li>Ivy: $baseUri/{@value IvyArtifactRepository#GRADLE_IVY_PATTERN}</li>
 * </ul>
 *
 * Note the pattern is the same for both artifacts and ivy files.
 */
public class GradleRepositoryLayout extends RepositoryLayout {

    public void apply(URI baseUri, PatternBasedResolver resolver) {
        if (baseUri == null) {
            return;
        }

        resolver.addArtifactLocation(baseUri, IvyArtifactRepository.GRADLE_ARTIFACT_PATTERN);
        resolver.addDescriptorLocation(baseUri, IvyArtifactRepository.GRADLE_IVY_PATTERN);
    }
}
