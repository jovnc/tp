package seedu.address.testutil;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPlayerDescriptor;
import seedu.address.model.player.Player;
import seedu.address.model.tag.Tag;

import java.util.Set;

import static seedu.address.logic.parser.CliSyntax.*;

/**
 * A utility class for Player.
 */
public class PlayerUtil {

    /**
     * Returns an add command string for adding the {@code player}.
     */
    public static String getAddCommand(Player player) {
        return AddCommand.COMMAND_WORD + " " + getPlayerDetails(player);
    }

    /**
     * Returns the part of command string for the given {@code player}'s details.
     */
    public static String getPlayerDetails(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + player.getName().fullName + " ");
        sb.append(PREFIX_PHONE + player.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + player.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + player.getAddress().value + " ");
        player.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPlayerDescriptor}'s details.
     */
    public static String getEditPlayerDescriptorDetails(EditPlayerDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
