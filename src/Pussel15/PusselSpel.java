package Pussel15;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.*;

/**
 * Created by Peter Almgren,Nick Danielsson
 * Date: 2020-10-22
 * Time: 09:16
 * Project: JAVASprint3
 * Copyright: MIT
 */

public class PusselSpel extends JPanel {

    private int antalBrickorPerRad;
    private int antalBrickor;
    private int dimension;
    private int[] brickor;
    private int brickStorlek;
    private static int blankPos;
    private int marginal;
    private int spelplan;
    private boolean gameOver;

    private Rectangle2D reset;
    private GradientPaint paint = new GradientPaint(0,0,Color.RED,550,0,Color.BLUE);
    private Random RANDOM = new Random();

    public PusselSpel(int antalBrickorPerRad, int dimension, int marginal) {

        this.antalBrickorPerRad = antalBrickorPerRad;
        this.dimension = dimension;
        this.marginal = marginal;

        setPreferredSize(new Dimension(dimension, dimension + marginal));

        antalBrickor = antalBrickorPerRad * antalBrickorPerRad - 1;
        brickor = new int[antalBrickorPerRad * antalBrickorPerRad];
        spelplan = (dimension +(-3 * marginal));
        brickStorlek = spelplan / antalBrickorPerRad;
        gameOver = true;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (reset.contains(e.getPoint())) {
                    newGame();
                    repaint();
                }

                if (gameOver) {
                    newGame();
                } else {

                    int ex = e.getX() - marginal;
                    int ey = e.getY() - marginal;

                    if (ex < 0 || ex > spelplan  || ey < 0  || ey > spelplan)
                        return;

                    int c1 = ex / brickStorlek;
                    int r1 = ey / brickStorlek;

                    int c2 = blankPos % antalBrickorPerRad;
                    int r2 = blankPos / antalBrickorPerRad;

                    int clickPos = r1 * antalBrickorPerRad + c1;

                    int dir = 0;

                        if (c1 == c2  &&  Math.abs(r1 - r2) > 0)
                            dir = (r1 - r2) > 0 ? antalBrickorPerRad : -antalBrickorPerRad;
                        else if (r1 == r2 && Math.abs(c1 - c2) > 0)
                            dir = (c1 - c2) > 0 ? 1 : -1;


                            if (dir != 0) {
                                do {
                                    int newBlankPos = blankPos + dir;
                                    brickor[blankPos] = brickor[newBlankPos];
                                    blankPos = newBlankPos;
                                } while (blankPos != clickPos);

                                brickor[blankPos] = 0;
                            }
                    gameOver = isSolved();
                }
                repaint();
            }
        });

        newGame();
    }

    private void newGame() {
        for (int i = 0; i < brickor.length; i++) {
            brickor[i] = (i + 1) % brickor.length;
        }
        blankPos = brickor.length - 1;
        shuffle();
        gameOver = false;
    }

    private void shuffle() {
        int n = antalBrickor;

        while (n > 1) {
            int r = RANDOM.nextInt(n--);
            int tmp = brickor[r];
            brickor[r] = brickor[n];
            brickor[n] = tmp;
        }
    }

    private boolean isSolved() {
        if (brickor[brickor.length - 1] != 0) //Kollar om tom bricka är på sista plats, om ej =false
            return false;

        for (int i = antalBrickor - 1; i >= 0; i--) {//Kollar ordningen på siffrorna, om ej ordning=false
            if (brickor[i] != i + 1)
                return false;
        }
        return true;//annars pussel löst=true
    }

    private void drawGrid(Graphics2D g) {
        for (int i = 0; i < brickor.length; i++) {
            int r = i / antalBrickorPerRad;
            int c = i % antalBrickorPerRad;

            int x = marginal + (c * brickStorlek);
            int y = marginal + (r * brickStorlek);

            if(brickor[i] == 0){
                continue;
            }
            g.setPaint(paint);
            g.fillRoundRect(x, y, brickStorlek, brickStorlek, 35, 35);
            g.setColor(Color.white);
            g.drawRoundRect(x, y, brickStorlek, brickStorlek, 35, 35);
            g.setColor(Color.WHITE);

            drawCenteredString(g, String.valueOf(brickor[i]), x , y);
        }
    }

    private void drawResetMessage(Graphics2D g) {
        if (!gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 20));
            g.setColor(Color.white);
            String s = "Click to restart game";
            g.drawString(s,(getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
                    getHeight() - marginal);
            reset= g.getFontMetrics().getStringBounds(s, g);
            reset.setRect(125,525,300,40);
            g.draw(reset);
        }
    }

    private void drawSolvedMessage(Graphics2D g) {
        if (gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setPaint(paint);
            String s = "You solved the puzzle";
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
                    getHeight() - marginal);
        }
    }

    private void drawCenteredString(Graphics2D g, String s, int x, int y) {
        setFont(new Font("Rockwell Extra Bold", Font.BOLD, 50));
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(s,  x + (brickStorlek - fm.stringWidth(s))/ 2 ,
                y + (asc + (brickStorlek - (asc + desc)) / 2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        setBackground(Color.black);
        Graphics2D g2D = (Graphics2D) g;
        drawGrid(g2D);
        drawSolvedMessage(g2D);
        drawResetMessage(g2D);
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Game of Fifteen");
        frame.setResizable(false);
        frame.add(new PusselSpel(4, 550, 20),BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}