/**
 * 
 */
package jp.co.csj.tools.utils.poi.core;


public class Pos
{

    private int x;
    private int y;
    private int col;
    private int row;
    
    /**
     * @return the x
     */
    public int getX() {
        return x;
    }
    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * @return the y
     */
    public int getY() {
        return y;
    }
    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * 
     */
    public Pos() {

        // TODO Auto-generated constructor stub
    }

    /**
     * @param x
     * @param y
     * @param col
     */
    public Pos(int x, int y, int col) {
        this.x = x;
        this.y = y;
        this.col = col;
    }
    

    /**
     * @return the col
     */
    public int getCol() {
        return col;
    }
    /**
     * @param col the col to set
     */
    public void setCol(int col) {
        this.col = col;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Pos [x=" + x + ", y=" + y + ", col=" + col + "]";
    }
    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }
    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

}