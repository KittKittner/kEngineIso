package com.kittner.engine.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class Image
{
    private static HashMap<String, Image> cache = new HashMap<String, Image>();

    private int w, h;
    private int[] p;
    private boolean alpha = false;

    public Image(String path)
    {
        BufferedImage image = null;
        if(cache.get(path) == null) {
            try {
                image = ImageIO.read(Image.class.getResourceAsStream(path));
                w = image.getWidth();
                h = image.getHeight();
                p = image.getRGB(0, 0, w, h, null, 0, w);
                image.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cache.put(path, this);
        } else {
            Image temp = cache.get(path);
            w = temp.getW();
            h = temp.getH();
            p = temp.getP();
        }
    }

    public Image(int[] p, int w, int h)
    {
        this.p = p;
        this.w = w;
        this.h = h;
    }

    public int getW()
    {
        return w;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public int getH()
    {
        return h;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public int[] getP()
    {
        return p;
    }

    public void setP(int[] p)
    {
        this.p = p;
    }

    public boolean isAlpha() {
        return alpha;
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }
}
