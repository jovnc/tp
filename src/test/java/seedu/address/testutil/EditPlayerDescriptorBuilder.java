package seedu.address.testutil;

import seedu.address.logic.commands.EditCommand.EditPlayerDescriptor;
import seedu.address.model.player.*;
import seedu.address.model.tag.Tag;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A utility class to help with building EditPlayerDescriptor objects.
 */
public class EditPlayerDescriptorBuilder {

    private EditPlayerDescriptor descriptor;

    public EditPlayerDescriptorBuilder() {
        descriptor = new EditPlayerDescriptor();
    }

    public EditPlayerDescriptorBuilder(EditPlayerDescriptor descriptor) {
        this.descriptor = new EditPlayerDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPlayerDescriptor} with fields containing {@code player}'s details
     */
    public EditPlayerDescriptorBuilder(Player player) {
        descriptor = new EditPlayerDescriptor();
        descriptor.setName(player.getName());
        descriptor.setPhone(player.getPhone());
        descriptor.setEmail(player.getEmail());
        descriptor.setAddress(player.getAddress());
        descriptor.setTags(player.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditPlayerDescriptor} that we are building.
     */
    public EditPlayerDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPlayerDescriptor} that we are building.
     */
    public EditPlayerDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPlayerDescriptor} that we are building.
     */
    public EditPlayerDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPlayerDescriptor} that we are building.
     */
    public EditPlayerDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPlayerDescriptor}
     * that we are building.
     */
    public EditPlayerDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditPlayerDescriptor build() {
        return descriptor;
    }
}
