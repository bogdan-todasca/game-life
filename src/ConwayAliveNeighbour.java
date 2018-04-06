import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdantodasca on 03/04/18.
 */
public class ConwayAliveNeighbour implements AliveNeighbour {
    private final Cell[][] cells;
    private final List<Cell> alive;

    public ConwayAliveNeighbour(final Cell[][] cells, final List<Cell> alive) {
        this.cells = cells;
        this.alive = alive;
    }

    @Override
    public int getAliveNeighbours(final Cell c) {
        final int i = c.getI();
        final int j = c.getJ();
        final List<Neighbour> neighbours = new ArrayList<>();
        neighbours.add(new Neighbour(i, j - 1));
        neighbours.add(new Neighbour(i, j + 1));
        neighbours.add(new Neighbour(i - 1, j));
        neighbours.add(new Neighbour(i + 1, j));
        neighbours.add(new Neighbour(i - 1, j - 1));
        neighbours.add(new Neighbour(i - 1, j + 1));
        neighbours.add(new Neighbour(i + 1, j - 1));
        neighbours.add(new Neighbour(i + 1, j + 1));
        return neighbours.stream().mapToInt(value ->
                value.getI() >= 0 && value.getI() < cells.length &&
                        value.getJ() >= 0 && value.getJ() < cells.length &&
                        alive.contains(cells[value.getI()][value.getJ()]) ? 1 :
                        0)
                .sum();

    }
}
