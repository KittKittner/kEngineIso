package com.kittner.engine.gfx;

public class ImageRequest
{
    public Image image;
    public int zDepth, offX, offY;

    public ImageRequest(Image image, int offX, int offY, int zDepth)
    {
        this.image = image;
        this.offX = offX;
        this.offY = offY;
        this.zDepth = zDepth;
    }
}
