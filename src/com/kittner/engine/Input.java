package com.kittner.engine;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    private GameContainer gc;

    private final int NUM_OF_KEYS = 256;
    private boolean[] keys = new boolean[NUM_OF_KEYS];
    private boolean[] keysLast = new boolean[NUM_OF_KEYS];

    private final int NUM_OF_BUTTONS = 5;
    private boolean[] buttons = new boolean[NUM_OF_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_OF_BUTTONS];

    private int mouseX = 0, mouseY = 0;
    private int scroll = 0;

    public Input(GameContainer gc)
    {
        this.gc = gc;

        gc.getWindow().getCanvas().addKeyListener(this);
        gc.getWindow().getCanvas().addMouseListener(this);
        gc.getWindow().getCanvas().addMouseMotionListener(this);
        gc.getWindow().getCanvas().addMouseWheelListener(this);
    }

    public void update()
    {
        scroll = 0;
        /*
        for(int i = 0; i < NUM_OF_KEYS; i++)
        {
            keysLast[i] = keys[i];
        }
        */
        System.arraycopy(keys, 0, keysLast, 0, NUM_OF_KEYS);

        /*
        for(int i = 0; i < NUM_OF_BUTTONS; i++)
        {
            buttonsLast[i] = buttons[i];
        }

        */
        System.arraycopy(buttons, 0, buttonsLast, 0, NUM_OF_BUTTONS);
    }

    public boolean isKey(int keyCode)
    {
        return keys[keyCode];
    }

    public boolean isKeyUp(int keyCode)
    {
        return !keys[keyCode] && keysLast[keyCode];
    }

    public boolean isKeyDown(int keyCode)
    {
        return keys[keyCode] && !keysLast[keyCode];
    }

    //whenthe user presses a button its index in the array is changed true and changed back to false on its release
    public boolean isButton(int button)
    {
        return buttons[button];
    }

    public boolean isButtonUp(int button)
    {
        return !buttons[button] && buttonsLast[button];
    }

    public boolean isButtonDown(int button)
    {
        return buttons[button] && !buttonsLast[button];
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        buttons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        buttons[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        mouseX = (int) (e.getX() / gc.getScale());
        mouseY = (int) (e.getY() / gc.getScale());
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        mouseX = (int) (e.getX() / gc.getScale());
        mouseY = (int) (e.getY() / gc.getScale());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        scroll = e.getWheelRotation();
    }

    public int getMouseX()
    {
        return mouseX;
    }

    public int getMouseY()
    {
        return mouseY;
    }

    public int getScroll()
    {
        return scroll;
    }
}
