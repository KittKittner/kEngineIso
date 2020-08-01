package com.kittner.engine;

import java.awt.*;

public class GameContainer implements Runnable
{
    private final double UPDATE_CAP = 1.0d/ 100.0d;
    private final double ONE_BILLION = 1000000000.0;

    private static boolean showFPS = true;

    private static int width = 640, height = 360;
    private static float scale = 2f;
    private String title = "Engine v1.0";
    private boolean running = false;

    private Thread thread;
    private Window window;
    private Renderer renderer;
    private Input input;
    private AbstractGame game;

    public GameContainer(AbstractGame game)
    {
        this.game = game;
    }

    public GameContainer(AbstractGame game, int screenX, int screenY, int zoom)
    {
        this.game = game;
        width = screenX;
        height = screenY;
        scale = zoom;
    }

    public void start()
    {
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);

        thread = new Thread(this);
        thread.run(); //run for main thread, start() for side thread (so ignore the suggestion)
    }

    public void stop()
    {

    }

    public void run()
    {
        running = true;

        boolean render = false;
        double firstTime= 0, lastTime = System.nanoTime() / ONE_BILLION, passedTime = 0, unprocessedTime = 0, frameTime = 0;
        int frames = 0, fps = 0;

        while(running)
        {
            //change to true to ignore frame caps
            render = false; //save the rendering for every frame and nowhere in between (i.e. not on every timer tick)

            firstTime = System.nanoTime() / ONE_BILLION;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while(unprocessedTime >= UPDATE_CAP)
            {
                unprocessedTime -= UPDATE_CAP;
                render = true;
                game.update(this, (float) UPDATE_CAP);
                input.update();

                if(frameTime >= 1.0) //i.e. do this every second
                {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    System.out.println("FPS: " + fps);
                }
            }

            if(render)
            {
                renderer.clear();
                game.render(this, renderer);
                renderer.process();
                if(showFPS)
                    renderer.drawText("FPS: " + fps, 0, 0, Color.CYAN.getRGB());
                window.update();
                frames++;
            }
            else
            {
                try
                {
                    Thread.sleep(1); //minimise cpu usage
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }

        dispose(); //probably unnecessary but is good practice
    }

    private void dispose()
    {

    }



    public static int getWidth()
    {
        return width;
    }

    public static void setWidth(int newWidth)
    {
        width = newWidth;
    }

    public static int getHeight()
    {
        return height;
    }

    public static void setHeight(int newHeight)
    {
        height = newHeight;
    }

    public static float getScale()
    {
        return scale;
    }

    public static void setScale(float newScale)
    {
        scale = newScale;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Window getWindow()
    {
        return window;
    }

    public Input getInput()
    {
        return input;
    }

    public static void toggleShowFPS()
    {
        showFPS = !showFPS;
    }
}
