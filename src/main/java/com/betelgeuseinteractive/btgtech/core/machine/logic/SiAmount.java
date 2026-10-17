package com.betelgeuseinteractive.btgtech.core.machine.logic;

/**
 * An amount scaled to an SI prefix for display, for example {@code "12.3"} with {@link SiPrefix#KILO} for 12,345.
 *
 * @param number the scaled amount, ready to show
 * @param prefix the prefix that belongs after the number
 */
public record SiAmount(String number, SiPrefix prefix) {}
