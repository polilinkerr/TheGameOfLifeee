import java.awt.*;

/**
 * Created by Admin on 2016-09-13.
 */
public class Cell {
    public final byte x_position;
    public final byte y_position;
    private byte neighbour;


    public byte getNeighbour() {
        return neighbour;
    }

    public void setNeighbour(byte neighbour) {
        //zabezpiczenie aby liczba byla pomiedzy 0 -8
        this.neighbour = neighbour;
    }



    public Cell(byte x_position, byte y_position) {
        this.x_position = x_position;
        this.y_position = y_position;
        neighbour = 0;
    }


    public static void main(String[] args){
        Point point = new Point(2,3);
        System.out.print(point.x);
    }
}
