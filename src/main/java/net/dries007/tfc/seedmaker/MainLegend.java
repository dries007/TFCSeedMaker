package net.dries007.tfc.seedmaker;

import net.dries007.tfc.seedmaker.datatypes.*;
import net.dries007.tfc.seedmaker.util.IDataType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

        Class[] enum_classes = new Class[] {Biome.class, Rock.class, Tree.class, Evt.class, Rain.class, Stability.class, Drainage.class, Ph.class};
        int height = 0;

        for (Class enum_class : enum_classes)
        {
            height += 25 * enum_class.getEnumConstants().length + header_offset;
        }

        BufferedImage bufferedImage = new BufferedImage(500, header_offset + height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 500, header_offset + height);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        graphics.drawString("TFCSeedMaker by Dries007 - Legend", 10, 25);
        graphics.drawLine(10, 27, 500-10, 27);

        graphics.setFont(new Font("Monospace", Font.BOLD, 16));

        int y = header_offset;
        for (Class enum_class : enum_classes)
        {
            graphics.setColor(Color.BLACK);
            graphics.drawString(enum_class.getSimpleName(), 10, y + 20);

            y += header_offset;

            for (Object o : enum_class.getEnumConstants())
            {
                IDataType d = ((IDataType) o);
                graphics.setColor(d.getColor());
                graphics.fillRect(10, y, 20, 20);
                graphics.setColor(Color.BLACK);
                graphics.drawRect(10, y, 20, 20);
                graphics.drawString(nameToNice(o.toString()), 10 + 20 + 10, y + 16);
                graphics.drawString("id: " + d.getId(), 10 + 20 + 10 + 200, y + 16);
                graphics.drawString("#" + Integer.toHexString(d.getColor().getRGB()).toUpperCase(), 10 + 20 + 10 + 300, y + 16);

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
