import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;


public class GameOfLifeMain extends JFrame implements ActionListener {
    private static final Dimension windowSize = new Dimension(800, 600);
    private static final Dimension minWindowSize = new Dimension(500, 300);


    private JMenuBar menuBar;
    private JMenu mGame;
    private JMenu mHelp;
    private JMenuItem miFileExit;
    private JMenuItem miGameAutoFill, miGamePlay, miGameStop, miGameReset;
    private JMenuItem miHelpAbout;
    private Thread game;
    private LifeBoard plansza;
    private JButton playButton;
    private JButton stopButton;
    private JButton stepButton;
    private JButton resetButton;


    public static void main(String[] args) {
        GameOfLifeMain ramka = new GameOfLifeMain();
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ramka.setTitle("The Game Of Life");
        ramka.setSize(windowSize);
        ramka.setMinimumSize(minWindowSize);
        ramka.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - ramka.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - ramka.getHeight()) / 2);

        ramka.setVisible(true);
    }


    private GameOfLifeMain() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        //Main Manu
        JMenu mFile = new JMenu("File");
        menuBar.add(mFile);

        mGame = new JMenu("Game");
        menuBar.add(mGame);
        mHelp = new JMenu("Help");
        menuBar.add(mHelp);
        //Sub Menu
        miFileExit = new JMenuItem("Exit");
        miFileExit.addActionListener(new ExitButtonListener());
        mFile.add(miFileExit);

        miGameAutoFill = new JMenuItem("AutoFill");
        miGameAutoFill.addActionListener(new AutoFillListener());
        mGame.add(miGameAutoFill);
        miGamePlay = new JMenuItem("Play");
        miGamePlay.addActionListener(new PlayButtonListener());
        mGame.add(miGamePlay);
        miGameStop = new JMenuItem("Stop");
        miGameStop.addActionListener(new StopButtonListener());
        mGame.add(miGameStop);
        miGameReset = new JMenuItem("Reset");
        miGameReset.addActionListener(new ResetButtonListener());
        mGame.add(miGameReset);

        miHelpAbout = new JMenuItem("Info Help");
        miHelpAbout.addActionListener(new HelpButtonListener());
        mHelp.add(miHelpAbout);


        JPanel pulpit = new JPanel();
        playButton = new JButton("Start Game");
        stopButton = new JButton("Stop Game");
        stepButton = new JButton("Next Generation");
        resetButton = new JButton("Reset");

        stopButton.setEnabled(false);
        plansza = new LifeBoard();

        pulpit.add(playButton);
        pulpit.add(stopButton);
        pulpit.add(stepButton);
        pulpit.add(resetButton);
        playButton.addActionListener(new PlayButtonListener());
        stopButton.addActionListener(new StopButtonListener());
        stepButton.addActionListener(new StepButtonListener());
        resetButton.addActionListener(new ResetButtonListener());
        getContentPane().add(pulpit, BorderLayout.NORTH);
        add(plansza);
    }

    public void setGameBeingPlayed(boolean isBeingPlayed) {
        if (isBeingPlayed) {
            miGamePlay.setEnabled(false);
            miGameStop.setEnabled(true);
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
            game = new Thread(plansza);

            game.start();
        } else {
            miGamePlay.setEnabled(true);
            miGameStop.setEnabled(false);
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
            game.interrupt();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    class PlayButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setGameBeingPlayed(true);
        }

    }

    class StopButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setGameBeingPlayed(false);
        }
    }

    class ResetButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            plansza.cleanGrid();
        }
    }

    class ExitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class StepButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            plansza.oneStep();
        }
    }

    class AutoFillListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            plansza.randomLifeGrid(50);
        }
    }

    class HelpButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            URI uri = null;
            try {
                uri = new URI("https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life");
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
            Desktop dt = Desktop.getDesktop();
            //dt.browse(uri);
            if (dt != null && dt.isSupported(Desktop.Action.BROWSE)) {
                try {
                    dt.browse(uri);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }


        }
    }
}
