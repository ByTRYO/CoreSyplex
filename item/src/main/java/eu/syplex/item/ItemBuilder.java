package eu.syplex.item;

import eu.syplex.common.item.ItemCreator;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ItemBuilder extends ItemCreator {

    public ItemBuilder(Material material) {
        super(material);
    }

    public ItemBuilder(@NotNull Material material, int amount) {
        super(material, amount);
    }

    @Override
    public ItemCreator name(@NotNull TextComponent component) {
        itemMeta.displayName(component);
        return this;
    }

    @Override
    public ItemCreator amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @Override
    public ItemCreator lore(@NotNull TextComponent... components) {
        itemStack.lore(Arrays.asList(components));
        return this;
    }

    @Override
    public ItemCreator removeLore() {
        itemStack.lore(null);
        return this;
    }

    @Override
    public ItemCreator enchant(@NotNull Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, false);
        return this;
    }

    @Override
    public ItemCreator enchantUnsafe(@NotNull Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }
}
