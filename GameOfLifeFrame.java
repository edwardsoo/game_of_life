import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class GameOfLifeFrame extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ActionListener listener = new ActionListener()
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            nextGeneration();
        }

    };
    private Timer t = new Timer( 0, listener );
    private int rows;
    private int cols;
    private GameOfLife game;
    private JPanel displayPanel = new JPanel();
    private CellButton[][] cellMap;
    private JMenuItem saveGame;
    private JButton nextGen;
    private JButton run;
    private JButton stop;
    private JButton setDelay;
    private JTextField delayField;
    private int generation;
    private JLabel generationLabel;
    private JLabel delayLabel;
    private final JFileChooser fc = new JFileChooser(
            System.getProperty( "user.dir" ) + "\\save" );

    public GameOfLifeFrame()
    {
        setJMenuBar( createMenuBar() );
        Container c = getContentPane();
        c.add( createInfoPanel(), BorderLayout.NORTH );
        c.add( displayPanel, BorderLayout.CENTER );
        c.add( createOptionPanel(), BorderLayout.SOUTH );
        pack();
        setVisible( true );
    }

    private JMenuBar createMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu( "File" );
        JMenuItem newGame = new JMenuItem( "New" );
        newGame.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                stop.doClick();
                try
                {
                    String input1 = JOptionPane.showInputDialog( null,
                            "Please enter number of columns (1~100)" );
                    if( input1 == null || input1.isEmpty())
                        return;
                    cols = Integer.parseInt( input1 );
                    String input2 = JOptionPane.showInputDialog( null,
                            "Please enter number of rows (1~100)" );
                    if( input2 == null || input2.isEmpty())
                        return;
                    rows = Integer.parseInt( input2 );
                    if( cols>100 || cols<1 || rows>100 || rows < 1)
                    {
                    	displayError( "Invalid Input." );
                    	return;
                    }
                    game = new GameOfLife( rows, cols );
                    cellMap = new CellButton[ rows ][ cols ];
                    createGameBoard();
                }
                catch( NumberFormatException exception )
                {
                    displayError( "Not an integer." );
                }
            }
        } );
        JMenuItem loadGame = new JMenuItem( "Load" );
        loadGame.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                stop.doClick();
                int returnVal = fc.showOpenDialog( null );
                if( returnVal == JFileChooser.APPROVE_OPTION )
                {
                	FileInputStream f_in = null;
                	ObjectInputStream obj_in = null;
                    try
                    {
                        File file = fc.getSelectedFile();
                        f_in = new FileInputStream( file );
                        obj_in = new ObjectInputStream( f_in );
                        game = (GameOfLife) obj_in.readObject();
                        rows = game.getHeight();
                        cols = game.getWidth();
                        cellMap = new CellButton[ rows ][ cols ];
                        createGameBoard();
                        if (obj_in != null) {
                    		obj_in.close();
                    	}
                    }
                    catch( IOException e1 )
                    {
                        displayError( "IOException" );
                    }
                    catch( ClassNotFoundException e2 )
                    {
                        displayError( "Not an instance of game of life" );
                    }
                }
            }

        } );
        saveGame = new JMenuItem( "Save" );
        saveGame.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                stop.doClick();
                int returnVal = fc.showDialog( null, "Save" );
                if( returnVal == JFileChooser.APPROVE_OPTION )
                {
                	FileOutputStream f_out = null;
                	ObjectOutputStream obj_out = null;
                    try
                    {
                        File file = fc.getSelectedFile();
                        f_out = new FileOutputStream( file );
                        obj_out = new ObjectOutputStream(
                                f_out );
                        obj_out.writeObject( game );
                        if (obj_out != null) {
                        	obj_out.close();
                    	}
                    }
                    catch( IOException e1 )
                    {
                        displayError( "IOException" );
                    }
                }
            }

        } );
        saveGame.setEnabled( false );
        JMenuItem exit = new JMenuItem( "Exit" );
        exit.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                stop.doClick();
                System.exit( 0 );
            }

        } );
        menu.add( newGame );
        menu.add( loadGame );
        menu.add( saveGame );
        menu.add( exit );
        menubar.add( menu );
        return menubar;
    }

    private JPanel createInfoPanel()
    {
        JPanel infoPanel = new JPanel();
        generationLabel = new JLabel();
        infoPanel.add( generationLabel );
        return infoPanel;
    }

    private JPanel createOptionPanel()
    {
        JPanel optionPanel = new JPanel();
        nextGen = new JButton( "Next Generation" );
        nextGen.addActionListener( listener );
        nextGen.setEnabled( false );
        run = new JButton( "Run" );
        run.setEnabled( false );
        run.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                t.start();
                nextGen.setEnabled( false );
                run.setEnabled( false );
                stop.setEnabled( true );
            }
        } );
        stop = new JButton( "Stop" );
        stop.setEnabled( false );
        stop.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                t.stop();
                nextGen.setEnabled( true );
                run.setEnabled( true );
                stop.setEnabled( false );
            }
        } );

        optionPanel.add( nextGen );
        optionPanel.add( run );
        optionPanel.add( stop );
        delayLabel = new JLabel( "Delay(ms):" );
        optionPanel.add( delayLabel );
        delayField = new JTextField( 5 );
        optionPanel.add( delayField );
        setDelay = new JButton( "Set Delay" );
        setDelay.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                try
                {
                    int delayTime = Integer.parseInt( delayField.getText() );
                    t.setDelay( delayTime );
                }
                catch( NumberFormatException exception )
                {
                    displayError( "Not an integer." );
                }

            }

        } );
        optionPanel.add( setDelay );
        return optionPanel;
    }

    private void createGameBoard()
    {
        displayPanel.setBackground( Color.GRAY );
        displayPanel.removeAll();
        GridLayout grid = new GridLayout( rows, cols );
        grid.setHgap( 1 );
        grid.setVgap( 1 );
        displayPanel.setLayout( grid );
        for( int i = 0; i < rows; i++ )
        {
            for( int j = 0; j < cols; j++ )
            {
                CellButton cell = new CellButton( j, i );
                if( game.isAlive( j, i ) )
                    cell.makeAlive();
                else
                    cell.makeDead();
                cell.addActionListener( new ActionListener()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        stop.doClick();
                        CellButton button = (CellButton) e.getSource();
                        game.invertLife( button.x, button.y );
                        button.invertColor();
                    }

                } );
                displayPanel.add( cell );
                cellMap[ i ][ j ] = cell;
            }
        }
        generation = 1;
        generationLabel.setText( "Generation : " + generation );
        nextGen.setEnabled( true );
        run.setEnabled( true );
        saveGame.setEnabled( true );
        repaint();
        setVisible( true );
    }

    private void nextGeneration()
    {
        game.nextGeneration();
        generation++;
        generationLabel.setText( "Generation : " + generation );
        for( int i = 0; i < rows; i++ )
        {
            for( int j = 0; j < cols; j++ )
            {
                if( game.isAlive( j, i ) )
                    cellMap[ i ][ j ].makeAlive();
                else
                    cellMap[ i ][ j ].makeDead();
            }
        }

    }

    private void displayError( String msg )
    {
        JOptionPane.showMessageDialog( null, msg, "Error",
                JOptionPane.ERROR_MESSAGE );
    }

}
