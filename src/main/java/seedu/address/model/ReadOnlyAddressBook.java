package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.player.Player;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the players list.
     * This list will not contain any duplicate players.
     */
    ObservableList<Player> getPlayerList();

}
