package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.player.Player;

import java.util.Comparator;

/**
 * An UI component that displays information of a {@code Player}.
 */
public class PlayerCard extends UiPart<Region> {

    private static final String FXML = "PlayerListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Player player;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PlayerCode} with the given {@code Player} and index to display.
     */
    public PlayerCard(Player player, int displayedIndex) {
        super(FXML);
        this.player = player;
        id.setText(displayedIndex + ". ");
        name.setText(player.getName().fullName);
        phone.setText(player.getPhone().value);
        address.setText(player.getAddress().value);
        email.setText(player.getEmail().value);
        player.getTags().stream()
            .sorted(Comparator.comparing(tag -> tag.tagName))
            .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
