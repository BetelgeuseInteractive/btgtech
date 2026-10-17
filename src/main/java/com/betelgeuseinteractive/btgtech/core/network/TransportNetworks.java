package com.betelgeuseinteractive.btgtech.core.network;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * Entry point to the transport networks: one {@link TransportNetworkManager} per server level, driven by the server
 * tick and by chunk events. All events used here fire on the server thread; NeoForge itself invalidates capability
 * caches in the same chunk events.
 */
public final class TransportNetworks {
    private static final Map<ServerLevel, TransportNetworkManager> MANAGERS = new IdentityHashMap<>();

    private TransportNetworks() {}

    /** Adds the server tick, chunk and level listeners to the game event bus. */
    public static void register() {
        NeoForge.EVENT_BUS.addListener(TransportNetworks::onLevelTick);
        NeoForge.EVENT_BUS.addListener(TransportNetworks::onChunkLoad);
        NeoForge.EVENT_BUS.addListener(TransportNetworks::onChunkUnload);
        NeoForge.EVENT_BUS.addListener(TransportNetworks::onLevelUnload);
    }

    /** Re-reads the block at the position; node blocks call this whenever their presence or state changes. */
    public static void refresh(ServerLevel level, BlockPos pos) {
        manager(level).refresh(pos);
    }

    private static TransportNetworkManager manager(ServerLevel level) {
        return MANAGERS.computeIfAbsent(level, TransportNetworkManager::new);
    }

    private static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level) {
            TransportNetworkManager manager = MANAGERS.get(level);
            if (manager != null) {
                manager.tick();
            }
        }
    }

    private static void onChunkLoad(ChunkEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level) {
            manager(level).onChunkLoaded(event.getChunk());
        }
    }

    private static void onChunkUnload(ChunkEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level) {
            TransportNetworkManager manager = MANAGERS.get(level);
            if (manager != null) {
                manager.onChunkUnloaded(event.getChunk());
            }
        }
    }

    private static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level) {
            MANAGERS.remove(level);
        }
    }
}
