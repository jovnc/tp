package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.player.Player;

import java.nio.file.Path;
import java.util.function.Predicate;

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Player> PREDICATE_SHOW_ALL_PLAYERS = unused -> true;

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Returns the AddressBook
     */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /**
     * Returns true if a player with the same identity as {@code player} exists in the address book.
     */
    boolean hasPlayer(Player player);

    /**
     * Deletes the given player.
     * The player must exist in the address book.
     */
    void deletePlayer(Player target);

    /**
     * Adds the given player.
     * {@code player} must not already exist in the address book.
     */
    void addPlayer(Player player);

    /**
     * Replaces the given player {@code target} with {@code editedPlayer}.
     * {@code target} must exist in the address book.
     * The player identity of {@code editedPlayer} must not be the same as another existing player in the address book.
     */
    void setPlayer(Player target, Player editedPlayer);

    /**
     * Returns an unmodifiable view of the filtered player list
     */
    ObservableList<Player> getFilteredPlayerList();

    /**
     * Updates the filter of the filtered player list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPlayerList(Predicate<Player> predicate);
}
