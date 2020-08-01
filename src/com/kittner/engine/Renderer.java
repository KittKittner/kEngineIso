package com.kittner.engine;

import com.kittner.engine.gfx.Font;
import com.kittner.engine.gfx.Image;
import com.kittner.engine.gfx.ImageRequest;
import com.kittner.engine.gfx.ImageTile;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Comparator;

public class Renderer
{
    private static int renderMethod = 0;

    private ArrayList<ImageRequest> imageRequests = new ArrayList<ImageRequest>();
    private Font font = Font.DEFAULT;

    private int pW, pH, zDepth;
    private int[] p, zBuffer;
    private boolean processing;

    public Renderer(GameContainer gc)
    {
        pW = gc.getWidth();
        pH = gc.getHeight();
        //give p direct access to the pixel data in the window
            //modifying p will directly modify the image shown in the window
        p = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[p.length];
    }

    public void clear()
    {
        for(int i = 0; i < p.length; i++)
        {
            //p[i] = 0; //default/real clearing of the screen

            if(p[i] == 0)
                p[i] = 0xff000000;
            switch(renderMethod)
            {
                case 4:
                    if(i != 0)
                        p[i] /= i;
                    break;
                case 3:
                    p[i] *= i;
                    break;
                case 2:
                    p[i] -= i;
                    break;
                case 1:
                    p[i] += i;
                    break;
                case 0:
                default:
                    p[i] = 0;
                    break;
            }

            zBuffer[i] = 0;
        }
    }

    public void process()
    {
        processing = true;

        imageRequests.sort(new Comparator<ImageRequest>() {
            @Override
            public int compare(ImageRequest i1, ImageRequest i2) {
                /*
                if(i1.zDepth < i2.zDepth)
                    return -1;
                if(i1.zDepth > i2.zDepth)
                    return 1;
                return 0;
                */
                return Integer.compare(i1.zDepth, i2.zDepth);
            }
        });

        for (ImageRequest ir : imageRequests)
        {
            setZDepth(ir.zDepth);
            ir.image.setAlpha(false);
            drawImage(ir.image, ir.offX, ir.offY);
        }

        imageRequests.clear();
        processing = false;
    }

    public void setPixel(int x, int y, int value)
    {
        int alpha = (value >> 24) & 0xff;

        if((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0) //check if the pixel is completely transparent
        {
            return;
        }

        int index = x + y * pW;

        //if an image has already been rendered in this location
        if(zBuffer[index] > zDepth)
            return;

        zBuffer[index] = zDepth;

        if(alpha == 255)
            p[index] = value;
        else
        {
            int pixel = p[index];
            //yes, all these brackets are necessary for alpha blending
            int newR = ((pixel >> 16) & 0xff) - (int)((((pixel >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newG = ((pixel >> 8) & 0xff) - (int)((((pixel >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newB = (pixel & 0xff) - (int)(((pixel & 0xff) - (value & 0xff)) * (alpha / 255f));

            p[x + y * pW] = (newR << 16 | newG << 8 | newB); //combine the four above integers to one integer
        }
    }

    public void drawText(String text, int offX, int offY, int colour)
    {
        drawText(text, offX, offY, colour, Integer.MAX_VALUE);
    }

    public void drawText(String text, int offX, int offY, int colour, int wrapX)
    {
        int offsetX = 0, offsetY = 0;
        boolean isTab = false;
        for(int i = 0; i < text.length(); i++)
        {
            int unicode = text.codePointAt(i);
            if(unicode == 10) //if the character is a newline
            {
                offsetY += font.getFontImage().getH(); //skip the printing down by the height of the font
                offsetX = 0;
                isTab = false;
            }
            else if(offsetX >= wrapX - font.getWidths()[unicode]) //if the line wraps but there was a tab on this line, start at the distance of the tab
            {
                offsetY += font.getFontImage().getH(); //skip the printing down by the height of the font
                offsetX = isTab ? font.getWidths()[' '] * 3 : 0;
            }
            else if(unicode == 9 && !isTab) //if the character is a tab
            {
                isTab = true;
                offsetX += font.getWidths()[' '] * 3; //a tab character is worth 3 spaces (arbitrary)
            }
            for(int y = 0; y < font.getFontImage().getH(); y++)
            {
                for(int x = 0; x < font.getWidths()[unicode]; x++)
                {
                    if(font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff) //if the pixel is part of a character
                    {
                        setPixel(x + offX + offsetX, y + offY + offsetY, colour);
                    }
                }
            }
            offsetX += font.getWidths()[unicode];
        }
    }

    public void drawImage(Image image, int offX, int offY)
    {
        if(image.isAlpha() && !processing)
        {
            imageRequests.add(new ImageRequest(image, offX, offY, zDepth));
            return;
        }

        //check before declaring so we can jump out out of the method before creating the new variables
        //manage if the code is completely offscreen
        if(offX < -image.getW()) return;
        if(offY < -image.getH()) return;
        if(offX >= pW) return;
        if(offY >= pH) return;

        int drawX = 0, drawY = 0, drawWidth = image.getW(), drawHeight = image.getH();
        //manage clipping of the image of the screen
        if(offX < 0) { drawX -= offX; }
        if(offY < 0) { drawY -= offY; }
        if(drawWidth + offX >  pW) { drawWidth -= (drawWidth + offX - pW); }
        if(drawHeight + offY >  pH) { drawHeight -= (drawHeight + offY - pH); }

        //draw the image to the screen
        for(int y = drawY; y < drawHeight; y++)
        {
            for(int x = drawX; x < drawWidth; x++)
            {
                setPixel(x + offX, y + offY, image.getP()[x + y * image.getW()]);
            }
        }
    }

    public void drawImageTile(ImageTile image, int offX, int offY)
    {
        //if the image is flagged as having alpha, leave rendering until later
        if(image.isAlpha() && !processing)
        {
            imageRequests.add(new ImageRequest(image.getCurrentAnimation(), offX, offY, zDepth));
            return;
        }

        //check before declaring so we can jump out out of the method before creating the new variables
        //manage if the code is completely offscreen
        if(offX < -image.getTileW()) return;
        if(offY < -image.getTileH()) return;
        if(offX >= pW) return;
        if(offY >= pH) return;

        int drawX = 0, drawY = 0, drawWidth = image.getTileW(), drawHeight = image.getTileH();
        //manage clipping of the image of the screen
        if(offX < 0) { drawX -= offX; }
        if(offY < 0) { drawY -= offY; }
        if(drawWidth + offX >  pW) { drawWidth -= (drawWidth + offX - pW); }
        if(drawHeight + offY >  pH) { drawHeight -= (drawHeight + offY - pH); }

        //draw the image to the screen
        for(int y = drawY; y < drawHeight; y++)
        {
            for(int x = drawX; x < drawWidth; x++)
            {
                setPixel(x + offX, y + offY, image.getP()[(x + image.getTileX() * image.getTileW()) + (y + image.getTileW() * image.getTileH()) * image.getW()]);
            }
        }
    }

    public void drawRect(int offX, int offY, int w, int h, int colour)
    {
        for(int y = 0; y <= h; y++)
        {
            setPixel(offX, y + offY, colour);
            setPixel(offX + w, y + offY, colour);
        }
        for(int x = 0; x <= w; x++)
        {
            setPixel(x + offX, offY, colour);
            setPixel(x + offX, offY + h, colour);
        }
    }

    public void drawRectFill(int offX, int offY, int w, int h, int colour)
    {
        //check before declaring so we can jump out out of the method before creating the new variables
        //manage if the image is completely offscreen
        if(offX < -w) return;
        if(offY < -h) return;
        if(offX >= pW) return;
        if(offY >= pH) return;

        int drawX = 0, drawY = 0, drawWidth = w, drawHeight = h;
        //manage clipping of the image of the screen
        if(offX < 0) { drawX -= offX; }
        if(offY < 0) { drawY -= offY; }
        if(drawWidth + offX >  pW) { drawWidth -= (drawWidth + offX - pW); }
        if(drawHeight + offY >  pH) { drawHeight -= (drawHeight + offY - pH); }

        for(int y = drawY; y < drawHeight; y++)
            for(int x = drawX; x < drawWidth; x++)
                setPixel(x + offX, y + offY, colour);
    }

    public static void setRenderMethod(int method)
    {
        if(method >=0 && method <= 4)
            renderMethod = method;
    }

    public void setZDepth(int zDepth)
    {
        this.zDepth = zDepth;
    }

    public int getZDepth()
    {
        return zDepth;
    }
}
