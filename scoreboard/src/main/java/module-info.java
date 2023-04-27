/**
 * This module contains concrete implementations to create scoreboards.
 *
 * @since 1.1.0
 */
module Core.scoreboard.main {
    requires Core.common.main;
    requires kotlin.stdlib;

    requires org.bukkit;
    requires org.apache.commons.lang3;
    requires net.kyori.examination.api;
    requires net.kyori.adventure;
    requires net.kyori.adventure.text.minimessage;
    requires net.kyori.adventure.text.serializer.legacy;
}