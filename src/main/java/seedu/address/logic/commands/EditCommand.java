package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.player.*;
import seedu.address.model.tag.Tag;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PLAYERS;

/**
 * Edits the details of an existing player in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the player identified "
        + "by the index number used in the displayed player list. "
        + "Existing values will be overwritten by the input values.\n"
        + "Parameters: INDEX (must be a positive integer) "
        + "[" + PREFIX_NAME + "NAME] "
        + "[" + PREFIX_PHONE + "PHONE] "
        + "[" + PREFIX_EMAIL + "EMAIL] "
        + "[" + PREFIX_ADDRESS + "ADDRESS] "
        + "[" + PREFIX_TAG + "TAG]...\n"
        + "Example: " + COMMAND_WORD + " 1 "
        + PREFIX_PHONE + "91234567 "
        + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PLAYER_SUCCESS = "Edited Player: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PLAYER = "This player already exists in the address book.";

    private final Index index;
    private final EditPlayerDescriptor editPlayerDescriptor;

    /**
     * @param index                of the player in the filtered player list to edit
     * @param editPlayerDescriptor details to edit the player with
     */
    public EditCommand(Index index, EditPlayerDescriptor editPlayerDescriptor) {
        requireNonNull(index);
        requireNonNull(editPlayerDescriptor);

        this.index = index;
        this.editPlayerDescriptor = new EditPlayerDescriptor(editPlayerDescriptor);
    }

    /**
     * Creates and returns a {@code Player} with the details of {@code playerToEdit}
     * edited with {@code editPlayerDescriptor}.
     */
    private static Player createEditedPlayer(Player playerToEdit, EditPlayerDescriptor editPlayerDescriptor) {
        assert playerToEdit != null;

        Name updatedName = editPlayerDescriptor.getName().orElse(playerToEdit.getName());
        Phone updatedPhone = editPlayerDescriptor.getPhone().orElse(playerToEdit.getPhone());
        Email updatedEmail = editPlayerDescriptor.getEmail().orElse(playerToEdit.getEmail());
        Address updatedAddress = editPlayerDescriptor.getAddress().orElse(playerToEdit.getAddress());
        Set<Tag> updatedTags = editPlayerDescriptor.getTags().orElse(playerToEdit.getTags());

        return new Player(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Player> lastShownList = model.getFilteredPlayerList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PLAYER_DISPLAYED_INDEX);
        }

        Player playerToEdit = lastShownList.get(index.getZeroBased());
        Player editedPlayer = createEditedPlayer(playerToEdit, editPlayerDescriptor);

        if (!playerToEdit.isSamePlayer(editedPlayer) && model.hasPlayer(editedPlayer)) {
            throw new CommandException(MESSAGE_DUPLICATE_PLAYER);
        }

        model.setPlayer(playerToEdit, editedPlayer);
        model.updateFilteredPlayerList(PREDICATE_SHOW_ALL_PLAYERS);
        return new CommandResult(String.format(MESSAGE_EDIT_PLAYER_SUCCESS, Messages.format(editedPlayer)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
            && editPlayerDescriptor.equals(otherEditCommand.editPlayerDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("index", index)
            .add("editPlayerDescriptor", editPlayerDescriptor)
            .toString();
    }

    /**
     * Stores the details to edit the player with. Each non-empty field value will replace the
     * corresponding field value of the player.
     */
    public static class EditPlayerDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditPlayerDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPlayerDescriptor(EditPlayerDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPlayerDescriptor)) {
                return false;
            }

            EditPlayerDescriptor otherEditPlayerDescriptor = (EditPlayerDescriptor) other;
            return Objects.equals(name, otherEditPlayerDescriptor.name)
                && Objects.equals(phone, otherEditPlayerDescriptor.phone)
                && Objects.equals(email, otherEditPlayerDescriptor.email)
                && Objects.equals(address, otherEditPlayerDescriptor.address)
                && Objects.equals(tags, otherEditPlayerDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .toString();
        }
    }
}
