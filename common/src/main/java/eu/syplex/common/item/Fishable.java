package eu.syplex.common.item;

import eu.syplex.common.Beta;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Represents a {@link NamedItem} that can fish. <br>
 * <b>Note, that this feature is marked as an {@link Beta} and may change in future releases.</b>
 *
 * @version 1.0.0
 * @since 1.3.0
 */
@Beta
public interface Fishable {

    /**
     * Abstract implementation of the logic performed when the item is used.
     *
     * @param event The {@link PlayerFishEvent} that is fired when using the item
     */
    void onFish(PlayerFishEvent event);

}
