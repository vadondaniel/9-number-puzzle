package boardgame;

import boardgame.model.BoardGameModel;
import boardgame.model.Position;
import boardgame.model.Square;
import boardgame.util.BoardGameMoveSelector;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;

import static boardgame.util.BoardGameMoveSelector.Phase;

public class BoardGameController {

    @FXML
    private GridPane board;

    private BoardGameModel model = new BoardGameModel();

    private BoardGameMoveSelector selector = new BoardGameMoveSelector(model);

    @FXML
    private void initialize() {
        for (var i = 0; i < board.getColumnCount(); i++) {
            for (var j = 0; j < board.getRowCount(); j++) {
                var square = createSquare(j, i);
                if (!model.isNotBlocked(new Position(j, i))) {
                    square.getStyleClass().add("blocked");
                }
                board.add(square, i, j);
            }
        }
        selector.phaseProperty().addListener(this::showSelectionPhaseChange);
    }

    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Circle(50);
        piece.fillProperty().bind(createSquareBinding(model.squareProperty(i, j)));
        square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.info("Click on square ({},{})", row, col);
        selector.select(new Position(row, col));
        if (selector.isReadyToMove()) {
            selector.makeMove();
        }
    }

    private ObjectBinding<Paint> createSquareBinding(ReadOnlyObjectProperty<Square> squareProperty) {
        return new ObjectBinding<Paint>() {
            {
                super.bind(squareProperty);
            }
            @Override
            protected Paint computeValue() {
                return switch (squareProperty.get()) {
                    case NONE, BLOCKED -> Color.TRANSPARENT;
                    case ONE -> Color.rgb(0, 0, 0);
                    case TWO -> Color.rgb(30, 30, 30);
                    case THREE -> Color.rgb(60, 60, 60);
                    case FOUR -> Color.rgb(90, 90, 90);
                    case FIVE -> Color.rgb(120, 120, 120);
                    case SIX -> Color.rgb(150, 150, 150);
                    case SEVEN -> Color.rgb(180, 180, 180);
                    case EIGHT -> Color.rgb(210, 210, 210);
                    case NINE -> Color.rgb(240, 240, 240);
                };
            }
        };
    }

    private void showSelectionPhaseChange(ObservableValue<? extends Phase> value, Phase oldPhase, Phase newPhase) {
        switch (newPhase) {
            case SELECT_FROM -> {}
            case SELECT_TO -> showSelection(selector.getFrom());
            case READY_TO_MOVE -> hideSelection(selector.getFrom());
        }
    }

    private void showSelection(Position position) {
        var square = getSquare(position);
        square.getStyleClass().add("selected");
    }

    private void hideSelection(Position position) {
        var square = getSquare(position);
        square.getStyleClass().remove("selected");
    }

    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

}
