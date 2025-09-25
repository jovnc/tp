package seedu.address.logic.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.player.Player;
import seedu.address.testutil.PlayerBuilder;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPlayers.getTypicalAddressBook;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPlayer_success() {
        Player validPlayer = new PlayerBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPlayer(validPlayer);

        assertCommandSuccess(new AddCommand(validPlayer), model,
            String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPlayer)),
            expectedModel);
    }

    @Test
    public void execute_duplicatePlayer_throwsCommandException() {
        Player playerInList = model.getAddressBook().getPlayerList().get(0);
        assertCommandFailure(new AddCommand(playerInList), model,
            AddCommand.MESSAGE_DUPLICATE_PLAYER);
    }

}
