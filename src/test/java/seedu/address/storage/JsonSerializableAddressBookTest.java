package seedu.address.storage;

import org.junit.jupiter.api.Test;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.testutil.TypicalPlayers;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PLAYERS_FILE = TEST_DATA_FOLDER.resolve("typicalPlayersAddressBook.json");
    private static final Path INVALID_PLAYER_FILE = TEST_DATA_FOLDER.resolve("invalidPlayerAddressBook.json");
    private static final Path DUPLICATE_PLAYER_FILE = TEST_DATA_FOLDER.resolve("duplicatePlayerAddressBook.json");

    @Test
    public void toModelType_typicalPlayersFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PLAYERS_FILE,
            JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPlayersAddressBook = TypicalPlayers.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalPlayersAddressBook);
    }

    @Test
    public void toModelType_invalidPlayerFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PLAYER_FILE,
            JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePlayers_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PLAYER_FILE,
            JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PLAYER,
            dataFromFile::toModelType);
    }

}
