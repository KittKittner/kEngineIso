package com.kittner.engine.gfx;

public class Font
{
    public static final Font DEFAULT = new Font("/fonts/calibri-10.png");

    private Image fontImage;
    private int[] offsets, widths;

    public Font(String path)
    {
        fontImage = new Image(path);
        offsets = new int[256]; //using 58 unicode characters (32 or space to 90 or z) using only uppercase
        widths = new int[256];

        int unicode = 0;
        for(int i = 0; i < fontImage.getW(); i++)
        {
            if(fontImage.getP()[i] == 0xff0000ff) //if the pixel is blue it is the start of a character
            {
                offsets[unicode] = i;
            }
            if(fontImage.getP()[i] == 0xffffff00) //if the pixel is yellow it is the end of a character
            {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }


    public Image getFontImage()
    {
        return fontImage;
    }

    public void setFontImage(Image fontImage)
    {
        this.fontImage = fontImage;
    }

    public int[] getOffsets()
    {
        return offsets;
    }

    public void setOffsets(int[] offsets)
    {
        this.offsets = offsets;
    }

    public int[] getWidths()
    {
        return widths;
    }

    public void setWidths(int[] widths)
    {
        this.widths = widths;
    }
}
