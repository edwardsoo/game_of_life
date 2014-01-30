import java.awt.Color;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class CellButton extends JButton
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean alive;
    public final int x;
    public final int y;

    public CellButton( int x, int y )
    {
        super();
        this.x = x;
        this.y = y;
        alive = false;
        setBackground( Color.BLACK );
        setOpaque( true );
        Border border = new LineBorder( Color.WHITE, 0 );
        setBorder( border );
    }

    public boolean isAlive()
    {
        return alive;
    }

    public void makeAlive()
    {
        alive = true;
        setBackground( Color.WHITE );
    }

    public void makeDead()
    {
        alive = false;
        setBackground( Color.BLACK );
    }

    public void invertColor()
    {
        if( alive )
            makeDead();
        else
            makeAlive();
    }
}
