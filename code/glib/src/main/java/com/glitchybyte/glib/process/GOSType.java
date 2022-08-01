// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.process;

import java.util.Locale;

/**
 * OS type.
 */
public enum GOSType {

    /**
     * Unknown OS.
     */
    UNKNOWN,

    /**
     * Linux.
     */
    LINUX,

    /**
     * macOS.
     */
    MAC_OS,

    /**
     * Windows.
     */
    WINDOWS;

    /**
     * Returns the OS type from the given string which must come from "os.name".
     *
     * @param osString Name from which to extract the type.
     * @return The OS type.
     */
    public static GOSType getType(final String osString) {
        final String osName = osString.toLowerCase(Locale.US);
        if (osName.contains("linux")) {
            return GOSType.LINUX;
        }
        if (osName.contains("mac os")) {
            return GOSType.MAC_OS;
        }
        if (osName.contains("windows")) {
            return GOSType.WINDOWS;
        }
        return GOSType.UNKNOWN;
    }
}
