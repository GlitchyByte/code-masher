// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import com.glitchybyte.glib.function.GSupplierWithException;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Network utilities.
 */
public final class GNetwork {

    /**
     * Constant for localhost network address.
     */
    public static final String LOCALHOST = "localhost";

    /**
     * Returns all currently up IPv4 addresses of this host.
     *
     * <p>Loopback addresses are returned as a single "localhost" entry.
     * And in case of an exception, it will return en empty set.
     *
     * <p>This method is designed to be a convenience.
     *
     * @return Set of IPv4 addresses that are currently up.
     */
    public static Set<String> getHostIPv4Addresses() {
        try {
            return NetworkInterface.networkInterfaces()
                    .filter(networkInterface -> GSupplierWithException.getOrDefault(networkInterface::isUp, false))
                    .flatMap(NetworkInterface::inetAddresses)
                    .filter(inetAddress -> inetAddress.getAddress().length == 4)
                    .map(inetAddress -> inetAddress.isLoopbackAddress() ? LOCALHOST : inetAddress.getHostAddress())
                    .collect(Collectors.toSet());
        } catch (final SocketException e) {
            return Collections.emptySet();
        }
    }

    private GNetwork() {
        // Hiding constructor.
    }
}
