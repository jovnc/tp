package seedu.address.logic.commands;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.player.Player;
import seedu.address.testutil.PlayerBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPlayers.ALICE;

public class AddCommandTest {

    @Test
    public void constructor_nullPlayer_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_playerAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPlayerAdded modelStub = new ModelStubAcceptingPlayerAdded();
        Player validPlayer = new PlayerBuilder().build();

        CommandResult commandResult = new AddCommand(validPlayer).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPlayer)),
            commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPlayer), modelStub.playersAdded);
    }

    @Test
    public void execute_duplicatePlayer_throwsCommandException() {
        Player validPlayer = new PlayerBuilder().build();
        AddCommand addCommand = new AddCommand(validPlayer);
        ModelStub modelStub = new ModelStubWithPlayer(validPlayer);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PLAYER, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Player alice = new PlayerBuilder().withName("Alice").build();
        Player bob = new PlayerBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different player -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPlayer(Player player) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPlayer(Player player) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePlayer(Player target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPlayer(Player target, Player editedPlayer) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Player> getFilteredPlayerList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPlayerList(Predicate<Player> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single player.
     */
    private class ModelStubWithPlayer extends ModelStub {
        private final Player player;

        ModelStubWithPlayer(Player player) {
            requireNonNull(player);
            this.player = player;
        }

        @Override
        public boolean hasPlayer(Player player) {
            requireNonNull(player);
            return this.player.isSamePlayer(player);
        }
    }

    /**
     * A Model stub that always accept the player being added.
     */
    private class ModelStubAcceptingPlayerAdded extends ModelStub {
        final ArrayList<Player> playersAdded = new ArrayList<>();

        @Override
        public boolean hasPlayer(Player player) {
            requireNonNull(player);
            return playersAdded.stream().anyMatch(player::isSamePlayer);
        }

        @Override
        public void addPlayer(Player player) {
            requireNonNull(player);
            playersAdded.add(player);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
