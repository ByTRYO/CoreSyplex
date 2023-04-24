package eu.syplex.common.translator;

import eu.syplex.common.Serializable;
import eu.syplex.common.Translatable;
import eu.syplex.common.exception.NotTranslatableException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a utility class for translating (legacy) strings into {@link Component}s and serializes them back into an adventure api based string.
 *
 * @version 1.0.0
 * @see Translatable
 * @see Serializable
 */
public class ComponentTranslator implements Translatable<Component>, Serializable<String> {

    private static ComponentTranslator componentTranslator = null;

    /**
     * Instantiates a new singleton {@link ComponentTranslator}.
     */
    protected ComponentTranslator() {
        componentTranslator = this;
    }

    /**
     * Returns the singleton instance of the {@link ComponentTranslator}.
     *
     * @return The singleton
     */
    public static ComponentTranslator translator() {
        if (componentTranslator == null) componentTranslator = new ComponentTranslator();
        return componentTranslator;
    }

    /**
     * {@inheritDoc}
     * This used all {@link StandardTags} per default and because of this, only adventure api based string are supported here. <br>
     * If legacy codes have to be used, call {@link ComponentTranslator#translateLegacy(String)}.
     *
     * @param input The input to translate
     * @param <I>   The input datatype
     * @return A {@link Component}
     * @throws NotTranslatableException Is thrown if the input is not of the type string
     */
    @Override
    public @Nullable <I> Component translate(@NotNull I input) throws NotTranslatableException {
        if (!(input instanceof String string)) throw new NotTranslatableException(input, String.class);
        return MiniMessage.miniMessage().deserialize(string, TagResolver.standard());
    }

    /**
     * {@inheritDoc}
     *
     * @param input The input
     * @param <I>   The type of the input
     * @return An adventure api based string
     * @throws NotTranslatableException Is thrown if the input is not a {@link Component}
     */
    @Override
    public @Nullable <I> String serialize(@NotNull I input) throws NotTranslatableException {
        if (!(input instanceof Component component)) throw new NotTranslatableException(input, Component.class);
        return MiniMessage.miniMessage().serialize(component);
    }

    /**
     * Translates the input legacy string into a {@link TextComponent}. This method is using the color char {@link LegacyComponentSerializer#AMPERSAND_CHAR} to detect colors. <br>
     * If an adventure api based string has to be translated into a {@link Component}, use {@link ComponentTranslator#translate(Object)}.
     *
     * @param input The legacy input
     * @return A {@link TextComponent}
     */
    public @NotNull TextComponent translateLegacy(String input) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(input);
    }
}
