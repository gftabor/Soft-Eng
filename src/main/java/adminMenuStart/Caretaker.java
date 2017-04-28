package adminMenuStart;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by mylena on 4/27/17.
 */
public class Caretaker implements MouseListener {
    private boolean m = false;

    public Caretaker(){}

    @Override
    public void mouseClicked(MouseEvent e) {
        m = true;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    public boolean isM() {
        return m;
    }
}
