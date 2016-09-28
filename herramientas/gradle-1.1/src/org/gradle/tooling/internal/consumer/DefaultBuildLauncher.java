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
package org.gradle.tooling.internal.consumer;

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.ProgressListener;
import org.gradle.tooling.ResultHandler;
import org.gradle.tooling.internal.consumer.async.AsyncConnection;
import org.gradle.tooling.internal.consumer.parameters.ConsumerOperationParameters;
import org.gradle.tooling.internal.protocol.BuildParametersVersion1;
import org.gradle.tooling.model.Task;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DefaultBuildLauncher implements BuildLauncher {
    private final List<String> tasks = new ArrayList<String>();
    private final AsyncConnection connection;
    private ConsumerOperationParameters operationParameters;

    public DefaultBuildLauncher(AsyncConnection connection, ConnectionParameters parameters) {
        operationParameters = new ConsumerOperationParameters(parameters);
        this.connection = connection;
    }

    public BuildLauncher forTasks(String... tasks) {
        this.tasks.clear();
        this.tasks.addAll(Arrays.asList(tasks));
        return this;
    }

    public BuildLauncher forTasks(Task... tasks) {
        forTasks(Arrays.asList(tasks));
        return this;
    }

    public BuildLauncher forTasks(Iterable<? extends Task> tasks) {
        this.tasks.clear();
        for (Task task : tasks) {
            this.tasks.add(task.getPath());
        }
        return this;
    }

    public BuildLauncher withArguments(String... arguments) {
        operationParameters.setArguments(arguments);
        return this;
    }

    public DefaultBuildLauncher setStandardError(OutputStream outputStream) {
        operationParameters.setStandardError(outputStream);
        return this;
    }

    public DefaultBuildLauncher setStandardOutput(OutputStream outputStream) {
        operationParameters.setStandardOutput(outputStream);
        return this;
    }

    public DefaultBuildLauncher setStandardInput(InputStream inputStream) {
        operationParameters.setStandardInput(inputStream);
        return this;
    }

    public DefaultBuildLauncher setJavaHome(File javaHome) {
        operationParameters.setJavaHome(javaHome);
        return this;
    }

    public DefaultBuildLauncher setJvmArguments(String... jvmArguments) {
        operationParameters.setJvmArguments(jvmArguments);
        return this;
    }

    public DefaultBuildLauncher addProgressListener(ProgressListener listener) {
        operationParameters.addProgressListener(listener);
        return this;
    }

    public void run() {
        BlockingResultHandler<Void> handler = new BlockingResultHandler<Void>(Void.class);
        run(handler);
        handler.getResult();
    }

    public void run(final ResultHandler<? super Void> handler) {
        connection.executeBuild(new DefaultBuildParameters(), operationParameters, new ResultHandlerAdapter<Void>(handler){
            @Override
            protected String connectionFailureMessage(Throwable failure) {
                return String.format("Could not execute build using %s.", connection.getDisplayName());
            }
        });
    }

    private class DefaultBuildParameters implements BuildParametersVersion1 {
        public List<String> getTasks() {
            return tasks;
        }
    }
}
