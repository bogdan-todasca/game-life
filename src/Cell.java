import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by bogdantodasca on 03/04/18.
 */
public class Cell extends JLabel {
    private final int i, j;
    private volatile CellState state = CellState.DEAD;

    public Cell(final String text, final int i, final int j) {
        super(text);
        this.i = i;
        this.j = j;
        init();
    }

    private void init() {
        setBackground(Color.BLACK);
        setOpaque(true);
        setForeground(Color.white);
        setPreferredSize(new Dimension(20, 20));
        setFont(getFont().deriveFont(30f));
        setBorder(new LineBorder(Color.RED, 1));
        initEvents();
    }

    public void updateState() {
        SwingUtilities.invokeLater(() -> {
            setBackground(Cell.this.state == CellState.ALIVE ? Color.WHITE : Color.BLACK);
            repaint();
        });
    }

    private void initEvents() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Cell.this.state = Cell.this.state == CellState.ALIVE ? CellState.DEAD : CellState.ALIVE;
                updateState();
            }
        });
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public enum CellState {
        ALIVE, DEAD
    }
}
