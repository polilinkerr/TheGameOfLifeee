import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class LifeBoardRes extends JPanel implements MouseListener, ComponentListener, MouseMotionListener, Runnable {

    private Dimension DimBoardGame = null;
    private static Map<Point,Boolean> LifeGrid = new HashMap<Point,Boolean>();
    private static final int CellSize = 20;
    private int iMovesPerSecond = 3;

    LifeBoardRes(){
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

    public void RunGeneration(){
        Map<Point,Boolean> NextGenerationGrid = new HashMap<Point,Boolean>();
        for (Map.Entry<Point,Boolean> cell: LifeGrid.entrySet()){
            int LiveCount = GetLiveNeighbors(cell.getKey().x,cell.getKey().y);
            if (cell.getValue()){ //Cell is live
                if (LiveCount==3 || LiveCount==2){
                    NextGenerationGrid.put(new Point(cell.getKey().x,cell.getKey().y),true);
                }else{
                    NextGenerationGrid.put(new Point(cell.getKey().x,cell.getKey().y),false);
                }
            }else{//cell is dead
                if(LiveCount ==3){
                    NextGenerationGrid.put(new Point(cell.getKey().x,cell.getKey().y),true);
                }else{
                    NextGenerationGrid.put(new Point(cell.getKey().x,cell.getKey().y),false);
                }
            }

        }
        LifeGrid.clear();
        LifeGrid.putAll(NextGenerationGrid);
        repaint();
    }

    private int GetLiveNeighbors(int xi,int yi){
        xi = xi+1;
        yi = yi+1;
        int leftColumn = xi-1;
        if(leftColumn<0){leftColumn = LifeGrid.size()-1;};
        int rightColumn = (xi+1) % LifeGrid.size();
        int UpRow = yi-1;
        if(UpRow<0){UpRow=LifeGrid.size()-1;}
        int DownRow = (yi+1)% LifeGrid.size();
        int LiveCount = 0;
        for (Map.Entry<Point,Boolean> cell: LifeGrid.entrySet()){
            if(cell.getKey().x == xi-1 && cell.getKey().y == yi-1){  if(cell.getValue()){LiveCount++;}}
            if(cell.getKey().x == xi-1 && cell.getKey().y == yi){  if(cell.getValue()){LiveCount++;}}
            if(cell.getKey().x == xi-1 && cell.getKey().y == yi+1){  if(cell.getValue()){LiveCount++;}}
            if(cell.getKey().x == xi && cell.getKey().y == yi){  if(cell.getValue()){LiveCount++;}}
            if(cell.getKey().x == xi && cell.getKey().y == yi){  if(cell.getValue()){LiveCount++;}}
            if(cell.getKey().x == xi+1 && cell.getKey().y == yi-1){  if(cell.getValue()){LiveCount++;}}
            if(cell.getKey().x == xi+1 && cell.getKey().y == yi+1){  if(cell.getValue()){LiveCount++;}}
            if(cell.getKey().x == xi+1 && cell.getKey().y == yi){  if(cell.getValue()){LiveCount++;}}
        }

        System.out.println("x:"+xi+" y:"+yi+" value:"+LiveCount);
        return LiveCount;
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
