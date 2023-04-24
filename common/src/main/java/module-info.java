/**
 * This module contains abstractions and common used data structures.
 *
 * @since 1.0.0
 */
module Core.common.main {
    exports eu.syplex.common;
    exports eu.syplex.common.item;
    exports eu.syplex.common.data;
    exports eu.syplex.common.exception;
    exports eu.syplex.common.translator;

    requires org.bukkit;
    requires kotlin.stdlib;
    requires org.jetbrains.annotations;
    requires net.kyori.adventure;
    requires net.kyori.adventure.text.minimessage;
    requires net.kyori.adventure.text.serializer.legacy;
}