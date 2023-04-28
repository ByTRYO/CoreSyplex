package eu.syplex.common.item;

import eu.syplex.common.Beta;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Represents an {@link NamedItem} that can be used with a right-/left-click. <br>
 * <b>Note, that this feature is marked as an {@link Beta} and may change in future releases.</b>
 *
 * @version 1.0.0
 * @since 1.3.0
 */
@Beta
public interface Intractable {

    /**
     * Abstract implementation of the logic performed when the item is used.
     *
     * @param event The {@link PlayerInteractEvent} that is fired when using the item
     */
    void onInteract(PlayerInteractEvent event);

}
