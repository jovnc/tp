package seedu.address.model.player;

import org.junit.jupiter.api.Test;
import seedu.address.testutil.PlayerBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPlayers.ALICE;
import static seedu.address.testutil.TypicalPlayers.BOB;

public class PlayerTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Player player = new PlayerBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> player.getTags().remove(0));
    }

    @Test
    public void isSamePlayer() {
        // same object -> returns true
        assertTrue(ALICE.isSamePlayer(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePlayer(null));

        // same name, all other attributes different -> returns true
        Player editedAlice = new PlayerBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
            .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePlayer(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PlayerBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePlayer(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Player editedBob = new PlayerBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePlayer(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PlayerBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePlayer(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Player aliceCopy = new PlayerBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different player -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Player editedAlice = new PlayerBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PlayerBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PlayerBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PlayerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PlayerBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Player.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
            + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress() + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
