package seedu.address.model.player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.player.exceptions.DuplicatePlayerException;
import seedu.address.model.player.exceptions.PlayerNotFoundException;

import java.util.Iterator;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

/**
 * A list of players that enforces uniqueness between its elements and does not allow nulls.
 * A player is considered unique by comparing using {@code Player#isSamePlayer(Player)}. As such, adding and updating of
 * players uses Player#isSamePlayer(Player) for equality so as to ensure that the player being added or updated is
 * unique in terms of identity in the UniquePlayerList. However, the removal of a player uses Player#equals(Object) so
 * as to ensure that the player with exactly the same fields will be removed.
 * <p>
 * Supports a minimal set of list operations.
 *
 * @see Player#isSamePlayer(Player)
 */
public class UniquePlayerList implements Iterable<Player> {

    private final ObservableList<Player> internalList = FXCollections.observableArrayList();
    private final ObservableList<Player> internalUnmodifiableList =
        FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent player as the given argument.
     */
    public boolean contains(Player toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePlayer);
    }

    /**
     * Adds a player to the list.
     * The player must not already exist in the list.
     */
    public void add(Player toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePlayerException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the player {@code target} in the list with {@code editedPlayer}.
     * {@code target} must exist in the list.
     * The player identity of {@code editedPlayer} must not be the same as another existing player in the list.
     */
    public void setPlayer(Player target, Player editedPlayer) {
        requireAllNonNull(target, editedPlayer);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PlayerNotFoundException();
        }

        if (!target.isSamePlayer(editedPlayer) && contains(editedPlayer)) {
            throw new DuplicatePlayerException();
        }

        internalList.set(index, editedPlayer);
    }

    /**
     * Removes the equivalent player from the list.
     * The player must exist in the list.
     */
    public void remove(Player toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new PlayerNotFoundException();
        }
    }

    public void setPlayers(UniquePlayerList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code players}.
     * {@code players} must not contain duplicate players.
     */
    public void setPlayers(List<Player> players) {
        requireAllNonNull(players);
        if (!playersAreUnique(players)) {
            throw new DuplicatePlayerException();
        }

        internalList.setAll(players);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Player> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Player> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniquePlayerList)) {
            return false;
        }

        UniquePlayerList otherUniquePlayerList = (UniquePlayerList) other;
        return internalList.equals(otherUniquePlayerList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code players} contains only unique players.
     */
    private boolean playersAreUnique(List<Player> players) {
        for (int i = 0; i < players.size() - 1; i++) {
            for (int j = i + 1; j < players.size(); j++) {
                if (players.get(i).isSamePlayer(players.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
