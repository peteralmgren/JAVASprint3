import javax.swing.*;
import java.awt.*;

/**
 * Created by Peter Almgren
 * Date: 2020-10-21
 * Time: 14:09
 * Project: JAVASprint3
 * Copyright: MIT
 */
public class BoardGame extends JFrame {

    JPanel panel = new JPanel();
    JButton b1 = new JButton("1");JButton b2 = new JButton("2");JButton b3 = new JButton("3");
    JButton b4 = new JButton("4");

    public BoardGame(){


        panel.setLayout(new GridLayout(5,3));
        panel.setSize(800,800);
        panel.add(b1);panel.add(b2);panel.add(b3);panel.add(b4);
        add(panel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


    }

    public static void main(String[] args) { BoardGame bg = new BoardGame();
    }

}
