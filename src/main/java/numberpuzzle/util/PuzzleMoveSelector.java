package numberpuzzle.util;

import numberpuzzle.model.PuzzleModel;
import numberpuzzle.model.Position;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * Handles the selection of the game piece selected by mouse click event.
 */
public class PuzzleMoveSelector {

    /**
     * Possible phases of the selection process.
     */
    public enum Phase {
        SELECT_FROM,
        SELECT_TO,
        READY_TO_MOVE

    }

    private PuzzleModel model;
    private ReadOnlyObjectWrapper<Phase> phase = new ReadOnlyObjectWrapper<>(Phase.SELECT_FROM);
    private boolean invalidSelection = false;
    private Position from;
    private Position to;

    public PuzzleMoveSelector(PuzzleModel model) {
        this.model = model;
    }

    public Phase getPhase() {
        return phase.get();
    }

    public ReadOnlyObjectProperty<Phase> phaseProperty() {
        return phase.getReadOnlyProperty();
    }

    public boolean isReadyToMove() {
        return phase.get() == Phase.READY_TO_MOVE;
    }

    public void select(Position position) {
        switch (phase.get()) {
            case SELECT_FROM -> selectFrom(position);
            case SELECT_TO -> selectTo(position);
            case READY_TO_MOVE -> throw new IllegalStateException();
        }
    }

    private void selectFrom(Position position) {
        if (!model.isEmpty(position) && model.isNotBlocked(position)) {
            from = position;
            phase.set(Phase.SELECT_TO);
            invalidSelection = false;
        } else {
            invalidSelection = true;
        }
    }

    private void selectTo(Position position) {
        if (model.canMove(from, position)) {
            to = position;
            phase.set(Phase.READY_TO_MOVE);
            invalidSelection = false;
        } else {
            invalidSelection = true;
        }
    }

    public Position getFrom() {
        if (phase.get() == Phase.SELECT_FROM) {
            throw new IllegalStateException();
        }
        return from;
    }

    public Position getTo() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        return to;
    }

    public boolean isInvalidSelection() {
        return invalidSelection;
    }

    public void makeMove() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        model.move(from, to);
        reset();
    }

    public void reset() {
        from = null;
        to = null;
        phase.set(Phase.SELECT_FROM);
        invalidSelection = false;
    }

}
