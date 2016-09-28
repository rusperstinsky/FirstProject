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

package org.gradle.tooling.internal.consumer.versioning;

import org.gradle.tooling.internal.consumer.connection.ConsumerConnection;
import org.gradle.tooling.internal.consumer.parameters.ConsumerOperationParameters;
import org.gradle.tooling.model.internal.Exceptions;

/**
 * by Szczepan Faber, created at: 1/9/12
 */
public class FeatureValidator {

    public void validate(ConsumerConnection connection, ConsumerOperationParameters operationParameters) {
        VersionDetails version = connection.getVersionDetails();
        if (operationParameters.getJavaHome() != null) {
            if(!version.supportsConfiguringJavaHome()) {
                throw Exceptions.unsupportedOperationConfiguration("modelBuilder.setJavaHome() and buildLauncher.setJavaHome()");
            }
        }
        if (operationParameters.getJvmArguments() != null) {
            if (!version.supportsConfiguringJvmArguments()) {
                throw Exceptions.unsupportedOperationConfiguration("modelBuilder.setJvmArguments() and buildLauncher.setJvmArguments()");
            }
        }
        if (operationParameters.getStandardInput() != null) {
            if (!version.supportsConfiguringStandardInput()) {
                throw Exceptions.unsupportedOperationConfiguration("modelBuilder.setStandardInput() and buildLauncher.setStandardInput()");
            }
        }
    }
}