package eu.syplex.common.translator;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a more complex variant of the {@link ComponentTranslator}.
 *
 * @version 1.0.0
 * @since 1.2.0
 */
public class ComplexComponentTranslator extends ComponentTranslator {

    private static ComplexComponentTranslator componentTranslator = null;

    protected ComplexComponentTranslator() {
    }

    /**
     * Returns the singleton instance of the {@link ComplexComponentTranslator}.
     *
     * @return The singleton instance
     */
    public static ComplexComponentTranslator complexTranslator() {
        if (componentTranslator == null) componentTranslator = new ComplexComponentTranslator();
        return componentTranslator;
    }

    /**
     * Removes all known adventure api based {@link StandardTags} from the input string.
     *
     * @param input The input string
     * @return A string without any known adventure api tags
     */
    public String stripTags(@NotNull String input) {
        return MiniMessage.miniMessage().stripTags(input);
    }

}
