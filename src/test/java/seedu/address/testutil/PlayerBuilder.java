package seedu.address.testutil;

import seedu.address.model.player.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * A utility class to help with building Player objects.
 */
public class PlayerBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;

    /**
     * Creates a {@code PlayerBuilder} with the default details.
     */
    public PlayerBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PlayerBuilder with the data of {@code playerToCopy}.
     */
    public PlayerBuilder(Player playerToCopy) {
        name = playerToCopy.getName();
        phone = playerToCopy.getPhone();
        email = playerToCopy.getEmail();
        address = playerToCopy.getAddress();
        tags = new HashSet<>(playerToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Player} that we are building.
     */
    public PlayerBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Player} that we are building.
     */
    public PlayerBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Player} that we are building.
     */
    public PlayerBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Player} that we are building.
     */
    public PlayerBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Player} that we are building.
     */
    public PlayerBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Player build() {
        return new Player(name, phone, email, address, tags);
    }

}
