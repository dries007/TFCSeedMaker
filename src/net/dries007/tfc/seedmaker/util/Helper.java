package net.dries007.tfc.seedmaker.util;

import net.dries007.tfc.seedmaker.genlayers.GenLayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Dries007
 */
public class Helper
{
    private Helper()
    {

    }

    public static void drawPng(File folder, String name, int size, Coords center, GenLayer layer, Color[] colors, boolean edgeOnly)
    {
        try
        {
            File outFile = new File(folder, name + ".png");
            //noinspection ResultOfMethodCallIgnored
            outFile.delete();
            System.out.println("Drawing " + outFile.getPath());
            int[] ints = layer.getInts(center.x - (size / 2), center.z - (size / 2), size, size);
            BufferedImage outBitmap = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = outBitmap.createGraphics();
            graphics.setBackground(new Color(255, 255, 255, 0));
            graphics.clearRect(0, 0, size, size);
            for (int x = 1; x < size-1; x++)
            {
                for (int y = 1; y < size-1; y++)
                {
                    final int us = ints[x + y * size];
                    final int up = ints[x + (y+1) * size];
                    final int dn = ints[x + (y-1) * size];
                    final int lt = ints[(x-1) + y * size];
                    final int rt = ints[(x+1) + y * size];

                    if (!edgeOnly || us != up || us != dn || us != lt || us != rt)
                    {
                        graphics.setColor(colors[us]);
                        graphics.drawRect(x, y, 1, 1);
                    }
                }
            }
            graphics.setColor(Color.white);
            graphics.drawRect(size / 2, 0, 1, size);
            graphics.drawRect(0, size / 2, size, 1);
            ImageIO.write(outBitmap, "PNG", outFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
