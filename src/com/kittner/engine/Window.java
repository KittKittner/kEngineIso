package com.kittner.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window
{
    private JFrame frame;
    private BufferedImage image;
    private BufferStrategy bs;
    private Canvas canvas;
    private Graphics g;

    public Window(GameContainer gc)
    {
        image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);

        canvas = new Canvas();
        Dimension d = new Dimension((int) (gc.getWidth() * gc.getScale()),  (int) (gc.getHeight() * gc.getScale()));
        //have no wiggle room for changing the size of the canvas in the frame
        canvas.setPreferredSize(d);
        canvas.setMaximumSize(d);
        canvas.setMinimumSize(d);

        frame = new JFrame(gc.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //the red cross will end the program
        frame.setLayout(new BorderLayout()); //for the canvas to stretch to the border
        frame.add(canvas, BorderLayout.CENTER); //canvas in the centre of the frame
        frame.pack(); //make sure the frame is the same size as the canvas
        frame.setLocationRelativeTo(null); //the window will start in the middle of the screen
        frame.setResizable(false); //ensure no changing of the size of the window
        frame.setVisible(true); //show it to the user

        canvas.createBufferStrategy(2); //there are 2 buffers to render to
        bs = canvas.getBufferStrategy();
        g = bs.getDrawGraphics();
    }

    public void update()
    {
        g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null); //draw the image to the buffer
        bs.show(); //show the image in the buffer
    }



    public BufferedImage getImage() {
        return image;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public JFrame getFrame()
    {
        return frame;
    }

}
