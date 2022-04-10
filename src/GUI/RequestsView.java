package GUI;

import javax.swing.*;
import java.awt.*;

public class RequestsView extends JLabel implements Scrollable {
    public RequestsView(String text) {
        super(text);
        setSize(240, 250);
        setLocation(5,80);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setVerticalTextPosition(SwingConstants.TOP);
        setHorizontalTextPosition(SwingConstants.LEFT);
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableBlockIncrement(Rectangle r, int orientation, int direction) {
        return 10;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public int getScrollableUnitIncrement(Rectangle r, int orientation, int direction) {
        return 10;
    }
}
