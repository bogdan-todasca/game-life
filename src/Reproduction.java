import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdantodasca on 03/04/18.
 */
class Reproduction implements ConwayRule {

    private final List<Cell> dead;
    private final AliveNeighbour aliveNeighbour;

    public Reproduction(final List<Cell> dead, final AliveNeighbour aliveNeighbour) {
        this.dead = dead;
        this.aliveNeighbour = aliveNeighbour;
    }

    @Override
    public void apply() {
        final List<Cell> deadCopy = new ArrayList<>(dead);
        for (Cell c : deadCopy) {
            final int neighbours = aliveNeighbour.getAliveNeighbours(c);
            if (neighbours == 3) {
                c.setState(Cell.CellState.ALIVE);
                System.out.println("Cell " + c.getI() + "," + c.getJ() + " reborns");
                c.updateState();

            }
        }
    }
}
