import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class FlippingTiles extends JFrame implements ActionListener {
    private JButton[] buttons;
    private ImageIcon[] icons;
    private int[] values;
    private int currentIndex;
    private int previousIndex;
    private int pairsFound;
    private JLabel timerLabel;
    private Timer timer;
    private int elapsedTime;

    public FlippingTiles() {
        super("Flipping Tiles");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
        setResizable(false);

        int numberOfButtons = 16;
        buttons = new JButton[numberOfButtons];
        icons = new ImageIcon[8];
        values = new int[numberOfButtons];
        currentIndex = -1;
        previousIndex = -1;
        pairsFound = 0;
        elapsedTime = 0;

        JPanel board = new JPanel();
        board.setLayout(new GridLayout(4, 4, 2, 2));

        // Load images
        for (int i = 0; i < 8; i++) {
            icons[i] = new ImageIcon(getClass().getResource("tile" + (i + 1) + ".png"));
        }

        // Assign images to values
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < numberOfButtons / 2; i++) {
            list.add(i);
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i = 0; i < numberOfButtons; i++) {
            buttons[i] = new JButton();
            buttons[i].addActionListener(this);
            board.add(buttons[i]);
            values[i] = list.get(i);
        }

        // Assign images to buttons
        Collections.shuffle(Arrays.asList(icons));
        for (int i = 0; i < numberOfButtons; i++) {
            buttons[i].setIcon(new ImageIcon(getClass().getResource("blank.png")));
            buttons[i].setDisabledIcon(icons[values[i]]);
        }

        getContentPane().add(board, BorderLayout.CENTER);

        // Timer panel
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerLabel = new JLabel("Time: 0");
        timerPanel.add(timerLabel);
        getContentPane().add(timerPanel, BorderLayout.NORTH);

        // Matches panel
        JPanel matchesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel matchesLabel = new JLabel("Matches: 0/" + numberOfButtons / 2);
        matchesPanel.add(matchesLabel);
        getContentPane().add(matchesPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Start timer
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                timerLabel.setText("Time: " + elapsedTime);
            }
        });
        timer.start();
    }
  

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < buttons.length; i++) {
            if (e.getSource() == buttons[i]) {
                if (currentIndex == -1) {
                    currentIndex = i;
                    buttons[currentIndex].setEnabled(false);
                    break;
                } else {
                    if (i == currentIndex) {
                        break;
                    }
                    buttons[i].setEnabled(false);
                    previousIndex = currentIndex;
                    currentIndex = i;
                    if (values[currentIndex] == values[previousIndex]) {
                        pairsFound++;
                        if (pairsFound == buttons.length / 2) {
                            int choice = JOptionPane.showConfirmDialog(null, "You won!\nWould you like to play again?",
                                                                       "Congratulations!", JOptionPane.YES_NO_OPTION);
                            if (choice == JOptionPane.YES_OPTION) {
                                dispose();
                                new FlippingTiles();
                            } else {
                                dispose();
                            }
                        }
                        currentIndex = -1;
                        previousIndex = -1;
                    } else {
                        Timer timer = new Timer(500, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                buttons[currentIndex].setEnabled(true);
                                buttons[previousIndex].setEnabled(true);
                                currentIndex = -1;
                                previousIndex = -1;
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new FlippingTiles();
    }
}