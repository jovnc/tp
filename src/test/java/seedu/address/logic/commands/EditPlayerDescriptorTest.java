package seedu.address.logic.commands;

import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.EditCommand.EditPlayerDescriptor;
import seedu.address.testutil.EditPlayerDescriptorBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.logic.commands.CommandTestUtil.*;

public class EditPlayerDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditPlayerDescriptor descriptorWithSameValues = new EditPlayerDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditPlayerDescriptor editedAmy = new EditPlayerDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPlayerDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditPlayerDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditPlayerDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditPlayerDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditPlayerDescriptor editPlayerDescriptor = new EditPlayerDescriptor();
        String expected = EditPlayerDescriptor.class.getCanonicalName() + "{name="
            + editPlayerDescriptor.getName().orElse(null) + ", phone="
            + editPlayerDescriptor.getPhone().orElse(null) + ", email="
            + editPlayerDescriptor.getEmail().orElse(null) + ", address="
            + editPlayerDescriptor.getAddress().orElse(null) + ", tags="
            + editPlayerDescriptor.getTags().orElse(null) + "}";
        assertEquals(expected, editPlayerDescriptor.toString());
    }
}
