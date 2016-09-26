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
package org.gradle.api;

import org.gradle.internal.SystemProperties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An enumeration of Java versions.
 */
public enum JavaVersion {
    VERSION_1_1(false), VERSION_1_2(false), VERSION_1_3(false), VERSION_1_4(false), VERSION_1_5(true), VERSION_1_6(true), VERSION_1_7(true), VERSION_1_8(true);

    private final boolean hasMajorVersion;

    private JavaVersion(boolean hasMajorVersion) {
        this.hasMajorVersion = hasMajorVersion;
    }

    /**
     * Converts the given object into a {@code JavaVersion}.
     *
     * @param value An object whose toString() value is to be converted. May be null.
     * @return The version, or null if the provided value is null.
     * @throws IllegalArgumentException when the provided value cannot be converted.
     */
    public static JavaVersion toVersion(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        }
        if (value instanceof JavaVersion) {
            return (JavaVersion) value;
        }

        String name = value.toString();
        if (name.matches("\\d")) {
            int index = Integer.parseInt(name) - 1;
            if (index < values().length && values()[index].hasMajorVersion) {
                return values()[index];
            }
        }

        Matcher matcher = Pattern.compile("1\\.(\\d)(\\D.*)?").matcher(name);
        if (matcher.matches()) {
            return values()[Integer.parseInt(matcher.group(1)) - 1];
        }
        throw new IllegalArgumentException(String.format("Could not determine java version from '%s'.", name));
    }

    /**
     * Returns the version of the current JVM.
     *
     * @return The version of the current JVM.
     */
    public static JavaVersion current() {
        return toVersion(SystemProperties.getJavaVersion());
    }

    public boolean isJava5() {
        return this == VERSION_1_5;
    }

    public boolean isJava6() {
        return this == VERSION_1_6;
    }

    public boolean isJava7() {
        return this == VERSION_1_7;
    }

    private boolean isJava8() {
        return this == VERSION_1_8;
    }

    public boolean isJava5Compatible() {
        return isJava5() || isJava6Compatible();
    }

    public boolean isJava6Compatible() {
        return isJava6() || isJava7Compatible();
    }

    public boolean isJava7Compatible() {
        return isJava7() || isJava8Compatible();
    }

    public boolean isJava8Compatible() {
        return isJava8();
    }

    @Override
    public String toString() {
        return getName();
    }

    private String getName() {
        return name().substring("VERSION_".length()).replace('_', '.');
    }
}
