package eu.syplex.common.player;

import eu.syplex.common.exception.NoMessageFoundException;
import eu.syplex.common.exception.NoMessagesFoundException;
import eu.syplex.common.translator.ComponentTranslator;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a message sent into the action bar above the player hotbar.
 *
 * @version 1.0.0
 * @since 1.3.0
 */
public class Actionbar {

	private final JavaPlugin plugin;
	private String message = "";
	private List<String> messages = Collections.emptyList();
	private long delay = 0;
	private long period = 0;

	/**
	 * Instantiates a new {@link Actionbar} with the instance of the plugins main class.
	 *
	 * @param plugin The plugins main class instance
	 */
	public Actionbar(@NotNull JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Sends the message once using {@link ComponentTranslator#translateLegacy(String)} to the target player. <br>
	 * Requires {@link #message} to be set using {@link #message(String)}.
	 *
	 * @param player The target player
	 *
	 * @throws NoMessageFoundException Thrown if no message it provided
	 */
	public void send(@NotNull Player player) throws NoMessageFoundException {
		if (message == null) throw new NoMessageFoundException();
		player.sendActionBar(ComponentTranslator.translator().translateLegacy(message));
	}

	/**
	 * Sends the message permanently to the target player. <br>
	 * Requires {@link #delay}, {@link #period} and {@link #message} to be set using {@link #delay(long)}, {@link #period(long)} and {@link #message(String)}. <br>
	 * Otherwise, the default values {@code 0} will be used.
	 *
	 * @param player The target player
	 *
	 * @throws NoMessageFoundException Thrown if no message it provided
	 */
	public void sendPermanently(@NotNull Player player) throws NoMessageFoundException {
		if (message == null) throw new NoMessageFoundException();
		new BukkitRunnable() {
			@Override
			public void run() {
				player.sendActionBar(ComponentTranslator.translator().translateLegacy(message));
			}
		}.runTaskTimerAsynchronously(plugin, delay, period);
	}

	/**
	 * Sends changing messages to the target player. To keep the message visible for a period of 3 seconds, after 1.5 seconds the message will be resend.<br>
	 * Requires {@link #delay}, {@link #period} and {@link #messages} to be set using {@link #delay(long)}, {@link #period(long)} and {@link #messages(List)}. <br>
	 * Otherwise, the default values {@code 0} will be used.
	 *
	 * @param player The target player
	 *
	 * @throws NoMessagesFoundException Thrown if no messages are provided
	 */
	public void sendChanging(@NotNull Player player) throws NoMessagesFoundException {
		if (messages.isEmpty()) throw new NoMessagesFoundException();

		AtomicInteger current = new AtomicInteger(0);
		new BukkitRunnable() {
			final Component component = ComponentTranslator.translator().translateLegacy(messages.get(current.get()));

			@Override
			public void run() {
				player.sendActionBar(component);

				new BukkitRunnable() {
					@Override
					public void run() {
						player.sendActionBar(component);
					}
				}.runTaskLater(plugin, delay / 2);

				if (current.get() == messages.size()) current.set(0);
				else current.getAndIncrement();
			}
		}.runTaskTimer(plugin, delay, period);
	}

	/**
	 * Provides a legacy message to send.
	 *
	 * @param message The message
	 *
	 * @return The current instance
	 */
	public Actionbar message(@NotNull String message) {
		this.message = message;
		return this;
	}

	/**
	 * Provides a list of legacy messages to send.
	 *
	 * @param messages The messages
	 *
	 * @return The current instance
	 */
	public Actionbar messages(@NotNull List<String> messages) {
		this.messages = messages;
		return this;
	}

	/**
	 * Configures the {@link #delay}.
	 *
	 * @param delay The new delay
	 *
	 * @return The current instance
	 */
	public Actionbar delay(long delay) {
		this.delay = delay;
		return this;
	}

	/**
	 * Configures the {@link #period}.
	 *
	 * @param period The new period
	 *
	 * @return The current instance
	 */
	public Actionbar period(long period) {
		this.period = period;
		return this;
	}

	/**
	 * Returns the {@link #message} to send. If not configured, an empty string.
	 *
	 * @return The message
	 */
	public String message() {
		return message;
	}

	/**
	 * Returns the list of {@link #messages} to send. If not configured, it will be empty.
	 *
	 * @return The messages
	 */
	public List<String> messages() {
		return messages;
	}

	/**
	 * Returns the configured {@link #delay} or {@code 0}.
	 *
	 * @return The current delay
	 */
	public long delay() {
		return delay;
	}

	/**
	 * Returns the configured {@link #period} or {@code 0}.
	 *
	 * @return The current period
	 */
	public long period() {
		return period;
	}
}
