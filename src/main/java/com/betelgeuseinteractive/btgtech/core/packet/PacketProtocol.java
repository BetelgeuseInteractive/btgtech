package com.betelgeuseinteractive.btgtech.core.packet;

/** Network protocol of BTG Tech's custom payloads (ADR 0054). */
public final class PacketProtocol {
    /**
     * The single protocol version of all payloads, passed to {@code RegisterPayloadHandlersEvent#registrar}. Increment it
     * whenever any payload changes; NeoForge then rejects clients with a different version.
     */
    public static final String VERSION = "1";

    private PacketProtocol() {}
}
