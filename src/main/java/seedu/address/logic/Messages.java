package seedu.address.logic;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.player.Player;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PLAYER_DISPLAYED_INDEX = "The player index provided is invalid";
    public static final String MESSAGE_PLAYERS_LISTED_OVERVIEW = "%1$d players listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
        "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
            Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code player} for display to the user.
     */
    public static String format(Player player) {
        final StringBuilder builder = new StringBuilder();
        builder.append(player.getName())
            .append("; Phone: ")
            .append(player.getPhone())
            .append("; Email: ")
            .append(player.getEmail())
            .append("; Address: ")
            .append(player.getAddress())
            .append("; Tags: ");
        player.getTags().forEach(builder::append);
        return builder.toString();
    }

}
