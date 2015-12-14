package net.dries007.tfc.seedmaker.util;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.dries007.tfc.seedmaker.datatypes.Biome;
import net.dries007.tfc.seedmaker.genlayers.GenLayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author Dries007
 */
public class Helper
{
    public static final Random RANDOM = new Random();
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private Helper()
    {

    }

    public static void drawPng(File folder, int size, Coords center, String[] names, GenLayer[] layers, Color[][] colors)
    {
        if (layers.length != colors.length || colors.length != names.length) throw new IllegalArgumentException("layers.length != colors.length != names.length");
        try
        {
            final long startCombined = System.currentTimeMillis();
            File outFileCombined = new File(folder, "Combined.png");
            //noinspection ResultOfMethodCallIgnored
            outFileCombined.delete();
            BufferedImage outBitmapCombined = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphicsCombined = outBitmapCombined.createGraphics();
            graphicsCombined.setBackground(new Color(255, 255, 255, 0));
            graphicsCombined.clearRect(0, 0, size, size);

            for (int i = 0; i < layers.length; i++)
            {
                final long start = System.currentTimeMillis();
                System.out.println("Drawing layer " + names[i]);
                File outFile = new File(folder, names[i] + ".png");
                //noinspection ResultOfMethodCallIgnored
                outFile.delete();
                BufferedImage outBitmap = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = outBitmap.createGraphics();
                graphics.setBackground(new Color(255, 255, 255, 0));
                graphics.clearRect(0, 0, size, size);

                int[] ints = layers[i].getInts(center.x - (size / 2), center.y - (size / 2), size, size);
                for (int x = 1; x < size - 1; x++)
                {
                    for (int y = 1; y < size - 1; y++)
                    {
                        final int us = ints[x + y * size];
                        graphics.setColor(colors[i][us]);
                        graphics.drawRect(x, y, 1, 1);

                        if (i == 0)
                        {
                            graphicsCombined.setColor(colors[i][us]);
                            graphicsCombined.drawRect(x, y, 1, 1);
                        }
                        else
                        {
                            final int up = ints[x + (y+1) * size];
                            final int dn = ints[x + (y-1) * size];
                            final int lt = ints[(x-1) + y * size];
                            final int rt = ints[(x+1) + y * size];
                            if (us != up || us != dn || us != lt || us != rt)
                            {
                                graphicsCombined.setColor(colors[i][us]);
                                graphicsCombined.drawRect(x, y, 1, 1);
                            }
                        }
                    }
                }

                graphics.setColor(Color.white);
                graphics.drawRect(size / 2, 0, 1, size);
                graphics.drawRect(0, size / 2, size, 1);
                ImageIO.write(outBitmap, "PNG", outFile);
                System.out.println("Done in " + (System.currentTimeMillis() - start) / 1000.0 + "s");
            }

            graphicsCombined.setColor(Color.white);
            graphicsCombined.drawRect(size / 2, 0, 1, size);
            graphicsCombined.drawRect(0, size / 2, size, 1);
            ImageIO.write(outBitmapCombined, "PNG", outFileCombined);
            System.out.println("Combined done in " + (System.currentTimeMillis() - startCombined) / 1000.0 + "s");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static long parseSeed(String seedString)
    {
        if (seedString == null || seedString.length() == 0) return Helper.RANDOM.nextLong();
        try
        {
            long j = Long.parseLong(seedString);
            if (j == 0L) throw new IllegalArgumentException("Zero (0) isn't a valid seed.");
            return j;
        }
        catch (NumberFormatException numberformatexception)
        {
            return seedString.hashCode();
        }
    }

    public static <T extends Comparable<? super T>> JsonElement toSortedJson(Set<T> set)
    {
        JsonArray array = new JsonArray();
        List<T> list = new ArrayList<>();
        list.addAll(set);
        Collections.sort(list);
        for (T e : list) array.add(e.toString());
        return array;
    }

    public static PngWriter[] prepareGraphics(String[] filenames, int size, File folder)
    {
        PngWriter[] out = new PngWriter[filenames.length];
        for (int i = 0; i < out.length; i++) out[i] = new PngWriter(new File(folder, filenames[i] + ".png"), new ImageInfo(size, size, 8, false), true);
        return out;
    }

    public static void finishGraphics(PngWriter[] writers) throws IOException
    {
        for (PngWriter writer : writers) writer.close();
    }
}
