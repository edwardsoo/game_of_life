import javax.swing.JFrame;

public class GameOfLifeApp
{

    /**
     * @param args
     */
    public static void main( String[] args )
    {
        JFrame frame = new GameOfLifeFrame();
        frame.setTitle( "Game Of Life" );
        frame.setSize( 720, 830 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible( true );
    }

}
