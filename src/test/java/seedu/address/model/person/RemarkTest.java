package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void equals() {
        Remark remark = new Remark("Some remark");

        // same object -> returns true
        assert (remark.equals(remark));

        // same values -> returns true
        Remark remarkWithSameValues = new Remark("Some remark");
        assert (remark.equals(remarkWithSameValues));

        // different types
        assertFalse(remark.equals(5));

        // null -> returns false
        assertFalse(remark.equals(null));

        // different remark -> returns false
        Remark differentRemark = new Remark("Different remark");
        assertFalse(remark.equals(differentRemark));
    }
}
