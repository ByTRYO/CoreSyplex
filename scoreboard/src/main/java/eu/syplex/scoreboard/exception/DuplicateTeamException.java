package eu.syplex.scoreboard.exception;

/**
 * Represents an exception thrown when an entered team name already exists on a {@link eu.syplex.scoreboard.PlexBoard}.
 */
public class DuplicateTeamException extends Exception {

    /**
     * Instantiates a new {@link DuplicateTeamException} with the conflicting name.
     *
     * @param name The conflicting name
     */
    public DuplicateTeamException(String name) {
        super("A team with the name '" + name + "' is already present on this scoreboard!");
    }
}
