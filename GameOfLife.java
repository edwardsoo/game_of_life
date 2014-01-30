import java.io.Serializable;

/*
  Author: Edward Soo
  UNIX login ID: b0e7
  Student number: 71680094
  Date: November 28, 2009
  
  By submitting this file, we acknowledge that the persons whose names appear
  above are the only authors of this code except as acknowledged in the code below.
 */

public class GameOfLife implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int COLS;
    private final int ROWS;
    private boolean[][] board;

    /**
     * 
     * @param aWidth The number of columns on the game board.
     * @param aHeight The number of rows on the game board.
     */
    public GameOfLife( int rows, int cols )
    {

        ROWS = rows;
        COLS = cols;
        board = new boolean[ ROWS ][ COLS ];


        for( int y = 0; y < ROWS; y++ )
        {
            for( int x = 0; x < COLS; x++ )
            {
                this.makeDead( x, y );
            }
        }
    }

    /**
     * 
     * @return The number of columns on the game board.
     */
    public int getWidth()
    {
        return COLS;
    }

    /**
     * 
     * @return The number of rows on the game board.
     */
    public int getHeight()
    {
        return ROWS;
    }

    /**
     * 
     * @param x The horizontal position of the cell being checked.
     * @param y The vertical position of the cell being checked.
     * @return Return true if the cell is alive, false if it is dead.
     */
    public boolean isAlive( int x, int y )
    {
        return board[ y ][ x ];
    }

    public void invertLife( int x, int y )
    {
        if( isAlive( x, y ) )
            board[ y ][ x ] = false;
        else
            board[ y ][ x ] = true;
    }

    /**
     * 
     * @param x The horizontal position of the cell being turned alive.
     * @param y The vertical position of the cell being turned alive.
     */
    public void makeAlive( int x, int y )
    {
        board[ y ][ x ] = true;
    }

    /**
     * 
     * @param x The horizontal position of the cell being turned dead.
     * @param y The vertical position of the cell being turned dead.
     */
    public void makeDead( int x, int y )
    {
        board[ y ][ x ] = false;
    }

    /**
     * Change the game board to simulate the cell's next generation.
     */
    public void nextGeneration()
    {
        boolean[][] board2 = new boolean[ ROWS ][ COLS ];
        for( int y = 0; y < ROWS; y++ )
        {
            for( int x = 0; x < COLS; x++ )
            {
                int nearbyAlive = this.getNearbyAlive( x, y );
                if( board[ y ][ x ] )
                {
                    if( nearbyAlive < 2 || nearbyAlive > 3 )
                        board2[ y ][ x ] = false;
                    else
                        board2[ y ][ x ] = true;
                }
                else
                {
                    if( nearbyAlive == 3 )
                        board2[ y ][ x ] = true;
                    else
                        board2[ y ][ x ] = false;
                }
            }
        }
        board = board2;
    }

    /**
     * 
     * @param x The horizontal position of the cell being checked.
     * @param y The vertical position of the cell being checked.
     * @return The number of cells alive in around the cell being checked.
     */
    private int getNearbyAlive( int x, int y )
    {
        int nearbyAlive = 0;

        int leftBound = x - 1;
        if( leftBound < 0 )
            leftBound = 0;
        int rightBound = x + 1;
        if( rightBound >= COLS )
            rightBound = COLS - 1;
        int upperBound = y - 1;
        if( upperBound < 0 )
            upperBound = 0;
        int lowerBound = y + 1;
        if( lowerBound >= ROWS )
            lowerBound = ROWS - 1;
        for( int i = upperBound; i <= lowerBound; i++ )
        {
            for( int j = leftBound; j <= rightBound; j++ )
            {
                if( !( i == y && j == x ) && board[ i ][ j ] )
                    nearbyAlive++;
            }
        }
        return nearbyAlive;
    }

    /**
     * Return a String that display the game board
     */
    public String toString()
    {
        String grid = "";
        for( int y = 0; y < ROWS; y++ )
        {
            for( int x = 0; x < COLS; x++ )
            {
                if( board[ y ][ x ] )
                    grid = grid + "* ";
                else
                    grid = grid + ". ";
            }
            grid = grid + "\n";
        }
        return grid;
    }

}