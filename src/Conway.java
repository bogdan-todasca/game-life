import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by bogdantodasca on 31/03/18.
 */
public class Conway extends JFrame {
    private final int n, tick;
    private Cell[][] cells;
    private JButton start, save, load, quit, cleanAndStop;
    private java.util.List<Cell> alive, dead;
    private AliveNeighbour aliveNeighbour;
    private List<ConwayRule> rules;
    private ScheduledExecutorService executor;

    public Conway(final int n, final int tick) {
        this.n = n;
        this.tick = tick;
        this.alive = new ArrayList<>();
        this.dead = new ArrayList<>();
        init();
        setTitle("Conway's Game Of Life");
    }

    public static void main(String[] args) {
        if(args.length != 2){
            System.exit(1);
        }
        final int squareSize = Integer.valueOf(args[0]);
        final int tick = Integer.valueOf(args[1]);
        final Conway c = new Conway(squareSize, tick);
        c.setLocation(0, 0);
        c.setVisible(true);
        c.pack();

    }

    private void init() {
        initComponents();
        initLayout();
        initEvents();
        initRules();
    }

    private void initRules() {
        this.aliveNeighbour = new ConwayAliveNeighbour(this.cells, this.alive);
        this.rules = Arrays.asList(
                new FewerThan2Neighbours(alive, this.aliveNeighbour),
                new MoreThanThreeNeighbours(alive, this.aliveNeighbour),
                new Reproduction(dead, this.aliveNeighbour));
    }


    private void initLayout() {
        setLayout(new BorderLayout());
        final JPanel cellsPanel = new JPanel(new GridLayout(n, n));
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cellsPanel.add(cells[i][j]);
            }
        }
        add(cellsPanel, BorderLayout.CENTER);

        final JPanel buttonPanel = new JPanel(new GridLayout(5, 1));
        final JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.start = new JButton("Start");
        this.save = new JButton("Save Configuration");
        this.load = new JButton("Load Configuration");
        this.quit = new JButton("Exit");
        this.cleanAndStop = new JButton("Clean & Stop");
        buttonPanel.add(start);
        buttonPanel.add(cleanAndStop);
        buttonPanel.add(save);
        buttonPanel.add(load);
        buttonPanel.add(quit);
        flow.add(buttonPanel);
        add(flow, BorderLayout.EAST);
    }

    private void initComponents() {
        this.cells = new Cell[n][];
        for (int i = 0; i < n; i++) {
            cells[i] = new Cell[n];
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cells[i][j] = new Cell(" ", i, j);
            }
        }
    }

    private void initEvents() {
        this.start.addActionListener(e -> Conway.this.start());

        this.quit.addActionListener(e -> Conway.this.quit());
        this.cleanAndStop.addActionListener(e -> {
            Conway.this.executor.shutdownNow();
            for (Cell c : alive) {
                c.setState(Cell.CellState.DEAD);
                c.updateState();
            }
            alive.clear();
            for (Cell d : dead) {
                d.setState(Cell.CellState.DEAD);
                d.updateState();
            }
            dead.clear();

        });
    }


    private void start() {
        this.executor = Executors.newScheduledThreadPool(1);
        executor.execute(this::collectLiveCells);
        executor.scheduleWithFixedDelay(() -> {
            try {
                for (ConwayRule rule : Conway.this.rules) {
                    rule.apply();
                }
                final List<Cell> markAsDead = new ArrayList<>(), markAsAlive = new ArrayList<>();
                for (Cell a : alive) {
                    if (a.getState() == Cell.CellState.DEAD) {
                        markAsDead.add(a);
                    }
                }
                for (Cell d : dead) {
                    if (d.getState() == Cell.CellState.ALIVE) {
                        markAsAlive.add(d);
                    }
                }
                alive.removeAll(markAsDead);
                alive.addAll(markAsAlive);

                dead.removeAll(markAsAlive);
                dead.addAll(markAsDead);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 0, this.tick, TimeUnit.MILLISECONDS);
    }

    private void collectLiveCells() {
        this.alive.clear();
        this.dead.clear();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cells[i][j].getState() == Cell.CellState.ALIVE) {
                    this.alive.add(cells[i][j]);
                } else {
                    this.dead.add(cells[i][j]);
                }
            }
        }
    }

    private void quit() {
        System.exit(0);
    }

}
