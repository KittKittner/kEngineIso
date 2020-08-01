package com.kittner.engine.gfx;

public class ImageTile extends Image
{
    private int tileW, tileH, tileX, tileY, timeSinceLastUpdate, framesToUpdate;

    public ImageTile(String path, int tileW, int tileH)
    {
        this(path, tileW, tileH, 0, 0, 100);
    }

    public ImageTile(String path, int tileW, int tileH, int framesToUpdate)
    {
        this(path, tileW, tileH, 0, 0, framesToUpdate);
    }

    public ImageTile(String path, int tileW, int tileH, int tileX, int tileY)
    {
        this(path, tileW, tileH, tileX, tileY, 100);
    }

    public ImageTile(String path, int tileW, int tileH, int tileX, int tileY, int framesToUpdate)
    {
        super(path);
        this.tileW = tileW;
        this.tileH = tileH;
        this.tileX = tileX;
        this.tileY = tileY;
        this.framesToUpdate = framesToUpdate;
        this.timeSinceLastUpdate = 0;
    }

    public Image getCurrentAnimation()
    {
        timeSinceLastUpdate += 1; //one frame has passed
        if(timeSinceLastUpdate >= framesToUpdate) //if the animation is to be moved along
        {
            timeSinceLastUpdate = 0;
            if ((tileX+1) * tileW < this.getW())
                tileX += 1;
            else
                tileX = 0;
        }

        int[] p = new int[tileW*tileH];
        for(int y = 0; y < tileH; y++)
        {
            for(int x = 0; x < tileW; x++)
            {
                p[x + y * tileW] = this.getP()[(x + tileX * tileW) + (y + tileY * tileH) * this.getW()];
            }
        }

        return new Image(p, tileW, tileH);
    }

    public void reset()
    {
        this.tileX = 0;
        this.tileY = 0;
        this.timeSinceLastUpdate = 0;
    }

    public int getTileX()
    {
        return tileX;
    }
    public void setTileX(int tileX)
    {
        this.tileX = tileX;
    }

    public int getTileY()
    {
        return tileY;
    }
    public void setTileY(int tileY)
    {
        this.tileY = tileY;
    }

    public int getTileW()
    {
        return tileW;
    }
    public void setTileW(int tileW)
    {
        this.tileW = tileW;
    }

    public int getTileH()
    {
        return tileH;
    }
    public void setTileH(int tileH)
    {
        this.tileH = tileH;
    }

    public int getFramesToUpdate()
    {
        return framesToUpdate;
    }
    public void setFramesToUpdate(int frames)
    {
        framesToUpdate = frames;
    }

}
