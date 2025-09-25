package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.player.Player;
import seedu.address.model.player.UniquePlayerList;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePlayer comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePlayerList players;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */ {
        players = new UniquePlayerList();
    }

    public AddressBook() {
    }

    /**
     * Creates an AddressBook using the Players in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the player list with {@code players}.
     * {@code players} must not contain duplicate players.
     */
    public void setPlayers(List<Player> players) {
        this.players.setPlayers(players);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPlayers(newData.getPlayerList());
    }

    //// player-level operations

    /**
     * Returns true if a player with the same identity as {@code player} exists in the address book.
     */
    public boolean hasPlayer(Player player) {
        requireNonNull(player);
        return players.contains(player);
    }

    /**
     * Adds a player to the address book.
     * The player must not already exist in the address book.
     */
    public void addPlayer(Player p) {
        players.add(p);
    }

    /**
     * Replaces the given player {@code target} in the list with {@code editedPlayer}.
     * {@code target} must exist in the address book.
     * The player identity of {@code editedPlayer} must not be the same as another existing player in the address book.
     */
    public void setPlayer(Player target, Player editedPlayer) {
        requireNonNull(editedPlayer);

        players.setPlayer(target, editedPlayer);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePlayer(Player key) {
        players.remove(key);
    }

    /// / util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("players", players)
            .toString();
    }

    @Override
    public ObservableList<Player> getPlayerList() {
        return players.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return players.equals(otherAddressBook.players);
    }

    @Override
    public int hashCode() {
        return players.hashCode();
    }
}
