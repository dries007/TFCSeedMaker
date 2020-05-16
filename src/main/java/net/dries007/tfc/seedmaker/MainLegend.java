package net.dries007.tfc.seedmaker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;

import net.dries007.tfc.seedmaker.datatypes.Biome;
import net.dries007.tfc.seedmaker.datatypes.Drainage;
import net.dries007.tfc.seedmaker.datatypes.Rock;
import net.dries007.tfc.seedmaker.datatypes.Stability;
import net.dries007.tfc.seedmaker.util.Layers;

/**
 * This doesn't use pngj, cause it doesn't have easy text support.
 * @author Dries007
 */
public class MainLegend
{
    public static void main(String[] args) throws IOException
    {
        String filename = "legend.png";
        if (args.length == 1) filename = args[0];
        File file = new File(filename);
        System.out.println("Legend will be output to " + filename);

        final int header_offset = 30;

        Class<Enum<?>>[] classes = (Class<Enum<?>>[]) Arrays.stream(Layers.values()).map((e) -> e.enumClass).distinct().toArray(Class[]::new);
        int height = Arrays.stream(classes).mapToInt((e) -> 25 * e.getEnumConstants().length + header_offset).sum();

        BufferedImage bufferedImage = new BufferedImage(500, header_offset + height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 500, header_offset + height);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        graphics.drawString("TFCSeedMaker by Dries007 - Legend", 10, 25);
        graphics.drawLine(10, 27, 500-10, 27);

        Font defaultFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        Font monoFont = new Font(Font.MONOSPACED, Font.BOLD, 16);

        int y = header_offset;
        for (Class<Enum<?>> enum_class : classes)
        {
            graphics.setColor(Color.BLACK);
            graphics.drawString(enum_class.getSimpleName(), 10, y + 20);

            y += header_offset;

            int[] colors = Layers.layersForClass(enum_class).colors();

            for (Enum<?> o : enum_class.getEnumConstants())
            {
                graphics.setColor(new Color(colors[o.ordinal()]));
                graphics.fillRect(10, y, 20, 20);
                graphics.setColor(Color.BLACK);
                graphics.drawRect(10, y, 20, 20);
                graphics.drawString(nameToNice(o.toString()), 10 + 20 + 10, y + 16);
                graphics.setFont(monoFont);
                graphics.drawString("#" + Integer.toHexString(colors[o.ordinal()]).toUpperCase().substring(2), 10 + 20 + 10 + 300, y + 16);
                graphics.setFont(defaultFont);

                y += 25;
            }
        }

        ImageIO.write(bufferedImage, "png", file);
        System.out.println("Image Created");
    }

    private static String nameToNice(String name)
    {
        return name.substring(0, 1) + name.toLowerCase().replace('_', ' ').substring(1);
    }
}
