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
package org.gradle.initialization;

import org.gradle.CacheUsage;
import org.gradle.RefreshOptions;
import org.gradle.StartParameter;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.initialization.Settings;
import org.gradle.api.internal.file.BaseDirFileResolver;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.cli.*;
import org.gradle.internal.nativeplatform.filesystem.FileSystems;
import org.gradle.logging.LoggingConfiguration;
import org.gradle.logging.internal.LoggingCommandLineConverter;

import java.util.Map;

/**
 * @author Hans Dockter
 */
public class DefaultCommandLineConverter extends AbstractCommandLineConverter<StartParameter> {
    private static final String NO_SEARCH_UPWARDS = "u";
    private static final String PROJECT_DIR = "p";
    private static final String NO_PROJECT_DEPENDENCY_REBUILD = "a";
    private static final String BUILD_FILE = "b";
    public static final String INIT_SCRIPT = "I";
    private static final String SETTINGS_FILE = "c";
    public static final String GRADLE_USER_HOME = "g";
    private static final String CACHE = "C";
    private static final String DRY_RUN = "m";
    private static final String NO_OPT = "no-opt";
    private static final String RERUN_TASKS = "rerun-tasks";
    private static final String EXCLUDE_TASK = "x";
    private static final String PROFILE = "profile";
    private static final String CONTINUE = "continue";
    private static final String OFFLINE = "offline";
    private static final String REFRESH = "refresh";
    private static final String REFRESH_DEPENDENCIES = "refresh-dependencies";
    private static final String PROJECT_CACHE_DIR = "project-cache-dir";
    private static final String RECOMPILE_SCRIPTS = "recompile-scripts";

    private final CommandLineConverter<LoggingConfiguration> loggingConfigurationCommandLineConverter = new LoggingCommandLineConverter();
    private final SystemPropertiesCommandLineConverter systemPropertiesCommandLineConverter = new SystemPropertiesCommandLineConverter();
    private final ProjectPropertiesCommandLineConverter projectPropertiesCommandLineConverter = new ProjectPropertiesCommandLineConverter();

    public void configure(CommandLineParser parser) {
        loggingConfigurationCommandLineConverter.configure(parser);
        systemPropertiesCommandLineConverter.configure(parser);
        projectPropertiesCommandLineConverter.configure(parser);
        parser.allowMixedSubcommandsAndOptions();
        parser.option(NO_SEARCH_UPWARDS, "no-search-upward").hasDescription(String.format("Don't search in parent folders for a %s file.", Settings.DEFAULT_SETTINGS_FILE));
        parser.option(CACHE, "cache").hasArgument().hasDescription("Specifies how compiled build scripts should be cached. Possible values are: 'rebuild' and 'on'. Default value is 'on'")
                    .deprecated("Use '--rerun-tasks' or '--recompile-scripts' instead");
        parser.option(PROJECT_CACHE_DIR).hasArgument().hasDescription("Specifies the project-specific cache directory. Defaults to .gradle in the root project directory.");
        parser.option(DRY_RUN, "dry-run").hasDescription("Runs the builds with all task actions disabled.");
        parser.option(PROJECT_DIR, "project-dir").hasArgument().hasDescription("Specifies the start directory for Gradle. Defaults to current directory.");
        parser.option(GRADLE_USER_HOME, "gradle-user-home").hasArgument().hasDescription("Specifies the gradle user home directory.");
        parser.option(INIT_SCRIPT, "init-script").hasArguments().hasDescription("Specifies an initialization script.");
        parser.option(SETTINGS_FILE, "settings-file").hasArgument().hasDescription("Specifies the settings file.");
        parser.option(BUILD_FILE, "build-file").hasArgument().hasDescription("Specifies the build file.");
        parser.option(NO_PROJECT_DEPENDENCY_REBUILD, "no-rebuild").hasDescription("Do not rebuild project dependencies.");
        parser.option(NO_OPT).hasDescription("Ignore any task optimization.").deprecated("Use '--rerun-tasks' instead");
        parser.option(RERUN_TASKS).hasDescription("Ignore previously cached task results.");
        parser.option(RECOMPILE_SCRIPTS).hasDescription("Force build script recompiling.");
        parser.option(EXCLUDE_TASK, "exclude-task").hasArguments().hasDescription("Specify a task to be excluded from execution.");
        parser.option(PROFILE).hasDescription("Profiles build execution time and generates a report in the <build_dir>/reports/profile directory.");
        parser.option(CONTINUE).hasDescription("Continues task execution after a task failure.").experimental();
        parser.option(OFFLINE).hasDescription("The build should operate without accessing network resources.");
        parser.option(REFRESH).hasArguments().hasDescription("Refresh the state of resources of the type(s) specified. Currently only 'dependencies' is supported.").deprecated("Use '--refresh-dependencies' instead.");
        parser.option(REFRESH_DEPENDENCIES).hasDescription("Refresh the state of dependencies.");
    }

    @Override
    protected StartParameter newInstance() {
        return new StartParameter();
    }

    public StartParameter convert(final ParsedCommandLine options, final StartParameter startParameter) throws CommandLineArgumentException {
        loggingConfigurationCommandLineConverter.convert(options, startParameter);
        FileResolver resolver = new BaseDirFileResolver(FileSystems.getDefault(), startParameter.getCurrentDir());

        Map<String, String> systemProperties = systemPropertiesCommandLineConverter.convert(options);
        convertCommandLineSystemProperties(systemProperties, startParameter, resolver);

        Map<String, String> projectProperties = projectPropertiesCommandLineConverter.convert(options);
        startParameter.getProjectProperties().putAll(projectProperties);

        if (options.hasOption(NO_SEARCH_UPWARDS)) {
            startParameter.setSearchUpwards(false);
        }

        if (options.hasOption(PROJECT_DIR)) {
            startParameter.setProjectDir(resolver.resolve(options.option(PROJECT_DIR).getValue()));
        }
        if (options.hasOption(GRADLE_USER_HOME)) {
            startParameter.setGradleUserHomeDir(resolver.resolve(options.option(GRADLE_USER_HOME).getValue()));
        }
        if (options.hasOption(BUILD_FILE)) {
            startParameter.setBuildFile(resolver.resolve(options.option(BUILD_FILE).getValue()));
        }
        if (options.hasOption(SETTINGS_FILE)) {
            startParameter.setSettingsFile(resolver.resolve(options.option(SETTINGS_FILE).getValue()));
        }

        for (String script : options.option(INIT_SCRIPT).getValues()) {
            startParameter.addInitScript(resolver.resolve(script));
        }

        if (options.hasOption(CACHE)) {
            try {
                startParameter.setCacheUsage(CacheUsage.fromString(options.option(CACHE).getValue()));
            } catch (InvalidUserDataException e) {
                throw new CommandLineArgumentException(e.getMessage());
            }
        }

        if (options.hasOption(PROJECT_CACHE_DIR)) {
            startParameter.setProjectCacheDir(resolver.resolve(options.option(PROJECT_CACHE_DIR).getValue()));
        }

        if (options.hasOption(NO_PROJECT_DEPENDENCY_REBUILD)) {
            startParameter.setBuildProjectDependencies(false);
        }

        if (!options.getExtraArguments().isEmpty()) {
            startParameter.setTaskNames(options.getExtraArguments());
        }

        if (options.hasOption(DRY_RUN)) {
            startParameter.setDryRun(true);
        }

        if (options.hasOption(NO_OPT)) {
            startParameter.setNoOpt(true);
        }

        if (options.hasOption(RERUN_TASKS)) {
            startParameter.setRerunTasks(true);
        }

        if (options.hasOption(RECOMPILE_SCRIPTS)) {
            startParameter.setRecompileScripts(true);
        }

        if (options.hasOption(EXCLUDE_TASK)) {
            startParameter.setExcludedTaskNames(options.option(EXCLUDE_TASK).getValues());
        }

        if (options.hasOption(PROFILE)) {
            startParameter.setProfile(true);
        }

        if (options.hasOption(CONTINUE)) {
            startParameter.setContinueOnFailure(true);
        }

        if (options.hasOption(OFFLINE)) {
            startParameter.setOffline(true);
        }

        if (options.hasOption(REFRESH)) {
            startParameter.setRefreshOptions(RefreshOptions.fromCommandLineOptions(options.option(REFRESH).getValues()));
        }

        if (options.hasOption(REFRESH_DEPENDENCIES)) {
            startParameter.setRefreshDependencies(true);
        }

        return startParameter;
    }

    void convertCommandLineSystemProperties(Map<String, String> systemProperties, StartParameter startParameter, FileResolver resolver) {
        startParameter.getSystemPropertiesArgs().putAll(systemProperties);
        String gradleUserHomeProp = "gradle.user.home";
        if (systemProperties.containsKey(gradleUserHomeProp)) {
            startParameter.setGradleUserHomeDir(resolver.resolve(systemProperties.get(gradleUserHomeProp)));
        }
    }
}