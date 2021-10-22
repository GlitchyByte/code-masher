// Copyright 2015-2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import com.glitchybyte.glib.log.GLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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
     * Create object from path.
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
     * Create object from File.
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
     * Create object from Path.
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
     * Create object from Reader.
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
     * Write json object to path.
     *
     * @param object Object to convert to json and write.
     * @param path String representation of the path to write json to.
     * @param pretty True, if it should write with pretty printing.
     */
    public static void toPath(final Object object, final String path, final boolean pretty) {
        toPath(object, Paths.get(path), pretty);
    }

    /**
     * Write json object to File.
     *
     * @param object Object to convert to json and write.
     * @param file File to write json to.
     * @param pretty True, if it should write with pretty printing.
     */
    public static void toFile(final Object object, final File file, final boolean pretty) {
        toPath(object, file.toPath(), pretty);
    }

    /**
     * Write json object to Path.
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
     * Write json object to Writer.
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
