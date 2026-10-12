package com.betelgeuseinteractive.btgtech;

import com.betelgeuseinteractive.btgtech.client.BTGTechClient;
import net.neoforged.fml.common.Mod;

/**
 * Common entry point of BTG Tech, constructed by NeoForge on both physical sides.
 *
 * <p>Each feature is bootstrapped from here with one call, in the order defined in {@code docs/architecture/core.md}.
 */
@Mod(BTGTech.MOD_ID)
public final class BTGTech {
    public static final String MOD_ID = "btgtech";

    // Deliberate violation: common code referencing a client-only class.
    private static final Class<?> CLIENT_ENTRY_POINT = BTGTechClient.class;
}
