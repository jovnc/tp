package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.player.Player;

import java.util.logging.Logger;

/**
 * Panel containing the list of players.
 */
public class PlayerListPanel extends UiPart<Region> {
    private static final String FXML = "PlayerListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PlayerListPanel.class);

    @FXML
    private ListView<Player> playerListView;

    /**
     * Creates a {@code PlayerListPanel} with the given {@code ObservableList}.
     */
    public PlayerListPanel(ObservableList<Player> playerList) {
        super(FXML);
        playerListView.setItems(playerList);
        playerListView.setCellFactory(listView -> new PlayerListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Player} using a {@code PlayerCard}.
     */
    class PlayerListViewCell extends ListCell<Player> {
        @Override
        protected void updateItem(Player player, boolean empty) {
            super.updateItem(player, empty);

            if (empty || player == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PlayerCard(player, getIndex() + 1).getRoot());
            }
        }
    }

}
