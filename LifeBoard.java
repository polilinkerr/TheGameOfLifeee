import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class LifeBoard extends JPanel implements MouseListener, ComponentListener, MouseMotionListener, Runnable {

    private static final int CellSize = 20;
    private static Map<Point,Boolean> LifeGrid = new HashMap<Point,Boolean>();
    private Dimension DimBoardGame = null;
    private int iMovesPerSecond = 3;

    LifeBoard(){
        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);

        for (Map.Entry<Point,Boolean> cell: LifeGrid.entrySet()){

            if(cell.getValue()){
                g.setColor(Color.red);
                g.fillRect(CellSize+CellSize*cell.getKey().x,CellSize+CellSize*cell.getKey().y,CellSize,CellSize);
            }else{
                g.setColor(Color.white);
                g.fillRect(CellSize+CellSize*cell.getKey().x,CellSize+CellSize*cell.getKey().y,CellSize,CellSize);
            }

        }
        for (int i=0; i<=DimBoardGame.width; i++) {
            g.setColor(Color.black);
            g.drawLine(((i*CellSize+CellSize)), CellSize, (i*CellSize)+CellSize, CellSize + (CellSize*DimBoardGame.height));
        }
        for (int i=0; i<=DimBoardGame.height; i++) {
            g.setColor(Color.gray);
            g.drawLine(CellSize, ((i*CellSize)+CellSize), CellSize*(DimBoardGame.width+1), ((i*CellSize)+CellSize));
        }
    }
    public void cleanGrid() {
        LifeGrid.clear();
        repaint();
    }


    public void componentResized(ComponentEvent e) {
        // Setup the game board size with proper boundries
        DimBoardGame = new Dimension(getWidth()/CellSize-2, getHeight()/CellSize-2);
        updateArraySize();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    private void updateArraySize() {
        for (Map.Entry<Point,Boolean> cell: LifeGrid.entrySet()){
            if ((cell.getKey().x > DimBoardGame.width-1) || (cell.getKey().y > DimBoardGame.height-1)) {
                LifeGrid.remove(cell);
            }
        }
        repaint();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        addPoint(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
            addPoint(e);
    }

    private void addPoint(MouseEvent e) {
        int x = e.getPoint().x/CellSize-1;
        int y = e.getPoint().y/CellSize-1;
        if ((x >= 0) && (x < DimBoardGame.width) && (y >= 0) && (y < DimBoardGame.height)) {
            addPoint(x,y);
        }
    }

    private void addPoint(int x, int y) {
        if (!LifeGrid.containsKey(new Point(x,y))){
            LifeGrid.put(new Point(x,y), true);
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        addPoint(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void run() {
        oneStep();
        try {
            Thread.sleep(1000/iMovesPerSecond);
            run();
        } catch (InterruptedException ex) {}
    }
    public void oneStep(){
        boolean[][] gameBoard = new boolean[DimBoardGame.width+2][DimBoardGame.height+2];
        for (Map.Entry<Point,Boolean> cell: LifeGrid.entrySet()){
            if(cell.getValue()) {
                gameBoard[cell.getKey().x + 1][cell.getKey().y + 1] = true;
            }
        }
        LifeGrid.clear();
        Map<Point,Boolean> survivingCells = new HashMap<Point,Boolean>();
        // Iterate through the array, follow game of life rules
        for (int i=1; i<gameBoard.length-1; i++) {
            for (int j=1; j<gameBoard[0].length-1; j++) {
                int surrounding = 0;
                if (gameBoard[i-1][j-1]) { surrounding++; }
                if (gameBoard[i-1][j])   { surrounding++; }
                if (gameBoard[i-1][j+1]) { surrounding++; }
                if (gameBoard[i][j-1])   { surrounding++; }
                if (gameBoard[i][j+1])   { surrounding++; }
                if (gameBoard[i+1][j-1]) { surrounding++; }
                if (gameBoard[i+1][j])   { surrounding++; }
                if (gameBoard[i+1][j+1]) { surrounding++; }
                if (gameBoard[i][j]) {
                    // Cell is alive, Can the cell live? (2-3)
                    if ((surrounding == 2) || (surrounding == 3)) {
                        survivingCells.put(new Point(i-1,j-1),true);
                    }
                } else {
                    // Cell is dead, will the cell be given birth? (3)
                    if (surrounding == 3) {
                        survivingCells.put(new Point(i-1,j-1),true);
                    }
                }
            }
        }

        LifeGrid.putAll(survivingCells);
        repaint();


    }

    public void randomLifeGrid(int percent){
        LifeGrid.clear();
        for (int i=0; i<DimBoardGame.width; i++) {
            for (int j=0; j<DimBoardGame.height; j++) {
                if (Math.random()*100 < percent) {
                    addPoint(i,j);
                }
            }
        }
    }


}
