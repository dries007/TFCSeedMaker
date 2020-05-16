package net.dries007.tfc.seedmaker.util;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import net.dries007.tfc.seedmaker.genlayers.Layer;

/**
 * @author Dries007
 */
public class WorldGen implements Runnable
{
    public final String seedString;
    public final long seed;
    private final EnumSet<Layers> layers;
    private final File folder;

    private final int radius;
    private final int chunkSize;
    private final int expectedChunkCount;

    public WorldGen(String seedString, int radius, int chunkSize, EnumSet<Layers> layers)
    {
        this.chunkSize = chunkSize;
        this.radius = (radius / chunkSize) * chunkSize;
        this.expectedChunkCount = radius * radius * 4 / (chunkSize * chunkSize);
        this.seedString = seedString == null ? "" : seedString;
        this.seed = parseSeed(this.seedString);
        this.layers = layers;
        this.folder = new File(String.valueOf(seed));
    }

    @Override
    public int hashCode()
    {
        return (int) (seed ^ (seed >>> 32));
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return seed == ((WorldGen) o).seed;
    }

    class LayerGen
    {
        private final Layer layer;
        private final PngWriter writer;
        private final Layers layers;
        private final int[][] buffers = new int[chunkSize][];
        private final int[] colors;

        public LayerGen(Layers layers)
        {
            this.layers = layers;
            this.layer = layers.init(seed);
            this.colors = layers.colors();
            this.writer = new PngWriter(new File(folder, layers.name().toLowerCase() + ".png"), new ImageInfo(radius * 2, radius * 2, 8, false), true);
            for (int i = 0; i < chunkSize; i++) this.buffers[i] = new int[this.writer.imgInfo.samplesPerRow];
        }

        @Override
        public String toString()
        {
            return "LayerGen{" + layers + "}";
        }

        public void close()
        {
            writer.close();
        }

        public void row(int y)
        {
            // A chunk of image lines
            ImageLineInt[] lines = IntStream.range(0, chunkSize).mapToObj((i) -> new ImageLineInt(this.writer.imgInfo, this.buffers[i])).toArray(ImageLineInt[]::new);

            // x and y are chuck coordinates
            for (int x = -radius; x < radius; x += chunkSize)
            {
                int[] ints = layer.getInts(x, y, chunkSize, chunkSize);

                // xx and yy are within a chunk.
                for (int yy = 0; yy < chunkSize; yy++)
                {
                    for (int xx = 0; xx < chunkSize; xx++)
                    {
                        // Don't draw on grid lines, set pixel.
                        ImageLineHelper.setPixelRGB8(lines[yy], x + xx + radius, ((x + xx) % 1000 == 0 || (yy + y) % 1000 == 0) ? 0x000000 : colors[ints[xx + yy * chunkSize]]);
                    }
                }
            }

            // Write to image
            for (ImageLineInt line : lines)
            {
                writer.writeRow(line);
            }
        }
    }

    @Override
    public void run()
    {
        final long start = System.currentTimeMillis();

        //noinspection ResultOfMethodCallIgnored
        folder.mkdir();

        List<LayerGen> maps = layers.stream().map(LayerGen::new).collect(Collectors.toList());

        int chunkCount = 0;
        // Per chunk of Y lines (Not the same as a Minecraft chunk!)
        for (int y = -radius; y < radius; y += chunkSize)
        {
            final int finalY = y;
            maps.forEach((e) -> e.row(finalY));

            chunkCount += chunkSize;
            System.out.println("Seed " + seed + " Chunk " + chunkCount + " / " + expectedChunkCount);
        }
        // Close
        maps.forEach(LayerGen::close);

        long time = System.currentTimeMillis() - start;
        System.out.println("Done " + seed + " in " + time / 1000.0 + "s.");
    }

    public static long parseSeed(String seedString)
    {
        if (seedString == null || seedString.length() == 0) return ThreadLocalRandom.current().nextLong();
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
}

