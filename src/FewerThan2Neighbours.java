import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdantodasca on 03/04/18.
 */
class FewerThan2Neighbours implements ConwayRule {

    private final List<Cell> alive;
    private final AliveNeighbour aliveNeighbour;

    public FewerThan2Neighbours(final List<Cell> alive, final AliveNeighbour aliveNeighbour) {
        this.alive = alive;
        this.aliveNeighbour = aliveNeighbour;
    }

    @Override
    public void apply() {
        final List<Cell> live = new ArrayList<>(alive);
        for (Cell c : live) {
            final int neighbours = aliveNeighbour.getAliveNeighbours(c);
            if (neighbours < 2) {
                c.setState(Cell.CellState.DEAD);
                System.out.println("Cell " + c.getI() + "," + c.getJ() + " dies");
                c.updateState();
            }
        }
    }
}
