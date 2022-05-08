// Copyright 2015-2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.json;

import com.glitchybyte.glib.log.GLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Json utilities.
 */
public final class GJson {

    private static final Gson gson = new Gson();
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Create object from {@code String}.
     *
     * @param json Json string.
     * @param tClass Class of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromString(final String json, final Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }

    /**
     * Create object from {@code String}.
     *
     * @param json Json string.
     * @param type Type of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromString(final String json, final Type type) {
        return gson.fromJson(json, type);
    }

    /**
     * Create object from {@code String} representation of a path.
     *
     * @param path String representation of the path to read json from.
     * @param tClass Class of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromPath(final String path, final Class<T> tClass) {
        return fromPath(Paths.get(path), tClass);
    }

    /**
     * Create object from {@code String} representation of a path.
     *
     * @param path String representation of the path to read json from.
     * @param type Type of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromPath(final String path, final Type type) {
        return fromPath(Paths.get(path), type);
    }

    /**
     * Create object from {@code File}.
     *
     * @param file File to read json from.
     * @param tClass Class of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromFile(final File file, final Class<T> tClass) {
        return fromPath(file.toPath(), tClass);
    }

    /**
     * Create object from {@code File}.
     *
     * @param file File to read json from.
     * @param type Type of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromFile(final File file, final Type type) {
        return fromPath(file.toPath(), type);
    }

    /**
     * Create object from {@code Path}.
     *
     * @param path Path to read json from.
     * @param tClass Class of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromPath(final Path path, final Class<T> tClass) {
        try (final Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return fromReader(reader, tClass);
        } catch (final IOException e) {
            GLog.warning(e);
        }
        return null;
    }

    /**
     * Create object from {@code Path}.
     *
     * @param path Path to read json from.
     * @param type Type of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromPath(final Path path, final Type type) {
        try (final Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return fromReader(reader, type);
        } catch (final IOException e) {
            GLog.warning(e);
        }
        return null;
    }

    /**
     * Create object from {@code InputStream}.
     *
     * @param stream InputStream to read json from.
     * @param tClass Class of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromInputStream(final InputStream stream, final Class<T> tClass) {
        final Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        try (final BufferedReader bufferedReader = new BufferedReader(reader)) {
            return fromReader(bufferedReader, tClass);
        } catch (final IOException e) {
            GLog.warning(e);
        }
        return null;
    }

    /**
     * Create object from {@code InputStream}.
     *
     * @param stream InputStream to read json from.
     * @param type Type of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromInputStream(final InputStream stream, final Type type) {
        final Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        try (final BufferedReader bufferedReader = new BufferedReader(reader)) {
            return fromReader(bufferedReader, type);
        } catch (final IOException e) {
            GLog.warning(e);
        }
        return null;
    }

    /**
     * Create object from {@code Reader}.
     *
     * @param reader Reader producing json.
     * @param tClass Class of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromReader(final Reader reader, final Class<T> tClass) {
        return gson.fromJson(reader, tClass);
    }

    /**
     * Create object from {@code Reader}.
     *
     * @param reader Reader producing json.
     * @param type Type of resulting object.
     * @param <T> Type of desired object.
     * @return Object populated from json.
     */
    public static <T> T fromReader(final Reader reader, final Type type) {
        return gson.fromJson(reader, type);
    }

    /**
     * Return object as json {@code String}.
     *
     * @param object Object to convert to json.
     * @param pretty True, if it should write with pretty printing.
     * @return Json string.
     */
    public static String toString(final Object object, final boolean pretty) {
        return pretty ? prettyGson.toJson(object) : gson.toJson(object);
    }

    /**
     * Write json object to path represented by a {@code String}.
     *
     * @param object Object to convert to json and write.
     * @param path String representation of the path to write json to.
     * @param pretty True, if it should write with pretty printing.
     */
    public static void toPath(final Object object, final String path, final boolean pretty) {
        toPath(object, Paths.get(path), pretty);
    }

    /**
     * Write json object to {@code File}.
     *
     * @param object Object to convert to json and write.
     * @param file File to write json to.
     * @param pretty True, if it should write with pretty printing.
     */
    public static void toFile(final Object object, final File file, final boolean pretty) {
        toPath(object, file.toPath(), pretty);
    }

    /**
     * Write json object to {@code Path}.
     *
     * @param object Object to convert to json and write.
     * @param path Path to write json to.
     * @param pretty True, if it should write with pretty printing.
     */
    public static void toPath(final Object object, final Path path, final boolean pretty) {
        try (final Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            toWriter(object, writer, pretty);
        } catch (final IOException e) {
            GLog.warning(e);
        }
    }

    /**
     * Write json object to {@code Writer}.
     *
     * @param object Object to convert to json and write.
     * @param writer Writer to write json to.
     * @param pretty True, if it should write with pretty printing.
     */
    public static void toWriter(final Object object, final Writer writer, final boolean pretty) {
        if (pretty) {
            prettyGson.toJson(object, writer);
        } else {
            gson.toJson(object, writer);
        }
    }

    private GJson() {
        // Hiding constructor.
    }
}
