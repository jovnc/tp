package seedu.address.logic.commands;

import org.junit.jupiter.api.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPlayerDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.player.Player;
import seedu.address.testutil.EditPlayerDescriptorBuilder;
import seedu.address.testutil.PlayerBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PLAYER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PLAYER;
import static seedu.address.testutil.TypicalPlayers.getTypicalAddressBook;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Player editedPlayer = new PlayerBuilder().build();
        EditPlayerDescriptor descriptor = new EditPlayerDescriptorBuilder(editedPlayer).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PLAYER, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PLAYER_SUCCESS, Messages.format(editedPlayer));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPlayer(model.getFilteredPlayerList().get(0), editedPlayer);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPlayer = Index.fromOneBased(model.getFilteredPlayerList().size());
        Player lastPlayer = model.getFilteredPlayerList().get(indexLastPlayer.getZeroBased());

        PlayerBuilder playerInList = new PlayerBuilder(lastPlayer);
        Player editedPlayer = playerInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withTags(VALID_TAG_HUSBAND).build();

        EditPlayerDescriptor descriptor = new EditPlayerDescriptorBuilder().withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPlayer, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PLAYER_SUCCESS, Messages.format(editedPlayer));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPlayer(lastPlayer, editedPlayer);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PLAYER, new EditPlayerDescriptor());
        Player editedPlayer = model.getFilteredPlayerList().get(INDEX_FIRST_PLAYER.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PLAYER_SUCCESS, Messages.format(editedPlayer));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPlayerAtIndex(model, INDEX_FIRST_PLAYER);

        Player playerInFilteredList = model.getFilteredPlayerList().get(INDEX_FIRST_PLAYER.getZeroBased());
        Player editedPlayer = new PlayerBuilder(playerInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PLAYER,
            new EditPlayerDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PLAYER_SUCCESS, Messages.format(editedPlayer));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPlayer(model.getFilteredPlayerList().get(0), editedPlayer);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePlayerUnfilteredList_failure() {
        Player firstPlayer = model.getFilteredPlayerList().get(INDEX_FIRST_PLAYER.getZeroBased());
        EditPlayerDescriptor descriptor = new EditPlayerDescriptorBuilder(firstPlayer).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PLAYER, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PLAYER);
    }

    @Test
    public void execute_duplicatePlayerFilteredList_failure() {
        showPlayerAtIndex(model, INDEX_FIRST_PLAYER);

        // edit player in filtered list into a duplicate in address book
        Player playerInList = model.getAddressBook().getPlayerList().get(INDEX_SECOND_PLAYER.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PLAYER,
            new EditPlayerDescriptorBuilder(playerInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PLAYER);
    }

    @Test
    public void execute_invalidPlayerIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPlayerList().size() + 1);
        EditPlayerDescriptor descriptor = new EditPlayerDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PLAYER_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPlayerIndexFilteredList_failure() {
        showPlayerAtIndex(model, INDEX_FIRST_PLAYER);
        Index outOfBoundIndex = INDEX_SECOND_PLAYER;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPlayerList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
            new EditPlayerDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PLAYER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PLAYER, DESC_AMY);

        // same values -> returns true
        EditPlayerDescriptor copyDescriptor = new EditPlayerDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PLAYER, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PLAYER, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PLAYER, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPlayerDescriptor editPlayerDescriptor = new EditPlayerDescriptor();
        EditCommand editCommand = new EditCommand(index, editPlayerDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPlayerDescriptor="
            + editPlayerDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
