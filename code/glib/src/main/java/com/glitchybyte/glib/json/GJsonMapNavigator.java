// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.json;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Class to navigate a tree of maps.
 * Specifically designed to navigate freeform json.
 * <p>
 * A path in this class is of the following form: {@code "first/array[2]/resource"}<br>
 * {@code first} is an example of traversing objects by name.<br>
 * {@code array[2]} is an example of accessing a list. In this case the item at index 2.<br>
 * {@code resource} is an example of the final resource you are trying to retrieve.
 */
public final class GJsonMapNavigator {

    /**
     * Type of map. Used for Gson conversions.
     */
    public static final Type MAP_TYPE = TypeToken.getParameterized(Map.class, String.class, Object.class).getType();

    /**
     * Creates a {@code GJsonMapNavigator} from json coming from an {@code InputStream}.
     *
     * @param stream InputStream from which to read json.
     * @return A populated GJsonMapNavigator.
     */
    public static GJsonMapNavigator fromInputStream(final InputStream stream) {
        return new GJsonMapNavigator(GJson.fromInputStream(stream, MAP_TYPE));
    }

    /**
     * Creates a {@code GJsonMapNavigator} from a json {@code String}.
     *
     * @param json Json string.
     * @return A populated GJsonMapNavigator.
     */
    public static GJsonMapNavigator fromString(final String json) {
        return new GJsonMapNavigator(GJson.fromString(json, MAP_TYPE));
    }

    private final Map<String, Object> map;

    /**
     * Constructs a {@code GJsonMapNavigator} from an already populated {@code Map}.
     *
     * @param map Map to navigate.
     */
    public GJsonMapNavigator(final Map<String, Object> map) {
        this.map = map;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> traverseTo(final ResourceLocation location) {
        Map<String, Object> current = map;
        for (final ResourceLocation.Segment segment: location.segments) {
            if (current == null) {
                return null;
            }
            final Object obj = current.get(segment.name);
            if (obj == null) {
                return null;
            }
            if (segment.isList()) {
                final List<Object> list = obj instanceof List ? (List<Object>) obj : null;
                if ((list == null) || (segment.index >= list.size())) {
                    return null;
                }
                final Object value = list.get(segment.index);
                current = value instanceof Map ? (Map<String, Object>) value : null;
            } else {
                current = obj instanceof Map ? (Map<String, Object>) obj : null;
            }
        }
        return current;
    }

    private Object getObject(final String path) {
        final ResourceLocation location = new ResourceLocation(path);
        final Map<String, Object> obj = traverseTo(location);
        if (obj == null) {
            return null;
        }
        return obj.get(location.name);
    }

    /**
     * Retrieves the {@code String} value of the given path.
     *
     * @param path Path to resource.
     * @return String value of the resource, or NULL if resource wasn't found.
     */
    public String getString(final String path) {
        final Object value = getObject(path);
        return value instanceof String ? (String) value : null;
    }

    /**
     * Retrieves the {@code List} value of the given path.
     *
     * @param path Path to resource.
     * @return List value of the resource, or NULL if resource wasn't found.
     */
    @SuppressWarnings("unchecked")
    public List<Object> getObjectList(final String path) {
        final Object value = getObject(path);
        return value instanceof List ? (List<Object>) value : null;
    }

    /**
     * Retrieves the {@code List} value of the given path.
     *
     * @param path Path to resource.
     * @return List value of the resource, or NULL if resource wasn't found.
     */
    @SuppressWarnings("unchecked")
    public List<String> getStringList(final String path) {
        final Object value = getObject(path);
        return value instanceof List ? (List<String>) value : null;
    }

    @Override
    public String toString() {
        return "GJsonMapNavigator{" +
                GJson.toString(map, true) +
                '}';
    }
}
