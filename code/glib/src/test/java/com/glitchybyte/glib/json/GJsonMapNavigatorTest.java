// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.json;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GJsonMapNavigatorTest {

    private static final String json = """
            {
                "name": "Home",
                "devices": {
                    "mobile": [ "iPhone", "AppleWatch" ],
                    "immobile": [
                        {
                            "device": "AppleTV",
                            "location": "Living room"
                        },
                        {
                            "device": "MacBook Pro",
                            "location": "Office"
                        }
                    ]
                }
            }
            """;

    @Test
    void canGetString() {
        final GJsonMapNavigator navigator = GJsonMapNavigator.fromString(json);
        final String value = navigator.getString("name");
        assertEquals("Home", value);
    }

    @Test
    void canGetList() {
        final GJsonMapNavigator navigator = GJsonMapNavigator.fromString(json);
        final List<String> list = navigator.getStringList("devices/mobile");
        assertIterableEquals(List.of("iPhone", "AppleWatch"), list);
    }

    @Test
    void canTraverseThroughList() {
        final GJsonMapNavigator navigator = GJsonMapNavigator.fromString(json);
        final String value = navigator.getString("devices/immobile[1]/location");
        assertEquals("Office", value);
    }

    @Test
    void invalidPathShouldBeNULL() {
        final GJsonMapNavigator navigator = GJsonMapNavigator.fromString(json);
        final String value1 = navigator.getString("address");
        assertNull(value1);
        final String value2 = navigator.getString("devices/immobile[1]/color");
        assertNull(value2);
    }

    @Test
    void indexOutOfBoundsShouldBeNULL() {
        final GJsonMapNavigator navigator = GJsonMapNavigator.fromString(json);
        final String value = navigator.getString("devices/immobile[2]/device");
        assertNull(value);
    }

    @Test
    void syntaxError() {
        final GJsonMapNavigator navigator = GJsonMapNavigator.fromString(json);
        assertThrows(IllegalArgumentException.class, () -> { navigator.getString("devices/immobile[/device"); });
        assertThrows(IllegalArgumentException.class, () -> { navigator.getString("devices/immobile]0[/device"); });
    }

    @Test
    void invalidIndexError() {
        final GJsonMapNavigator navigator = GJsonMapNavigator.fromString(json);
        assertThrows(NumberFormatException.class, () -> { navigator.getString("devices/immobile[uno]/device"); });
    }
}
