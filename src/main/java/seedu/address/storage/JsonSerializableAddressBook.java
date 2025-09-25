package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PLAYER = "Players list contains duplicate player(s).";

    private final List<JsonAdaptedPlayer> players = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given players.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("players") List<JsonAdaptedPlayer> players) {
        this.players.addAll(players);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        players.addAll(source.getPlayerList().stream().map(JsonAdaptedPlayer::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPlayer jsonAdaptedPlayer : players) {
            Player player = jsonAdaptedPlayer.toModelType();
            if (addressBook.hasPlayer(player)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PLAYER);
            }
            addressBook.addPlayer(player);
        }
        return addressBook;
    }

}
