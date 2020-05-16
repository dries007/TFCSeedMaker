package net.dries007.tfc.seedmaker.genlayers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.IntFunction;
import javax.imageio.ImageIO;

import net.dries007.tfc.seedmaker.datatypes.Biome;
import net.dries007.tfc.seedmaker.datatypes.Drainage;
import net.dries007.tfc.seedmaker.datatypes.Rock;

public abstract class Layer
{
    public static final long MULT = 6364136223846793005L;
    public static final long ADD = 1442695040888963407L;
    // Distinct colors for debug map gen
    private static final Color[] COLORS = new Color[] {
            new Color(0xFFB300),    // Vivid Yellow
            new Color(0x803E75),    // Strong Purple
            new Color(0xFF6800),    // Vivid Orange
            new Color(0xA6BDD7),    // Very Light Blue
            new Color(0xC10020),    // Vivid Red
            new Color(0xCEA262),    // Grayish Yellow
            new Color(0x817066),    // Medium Gray
            new Color(0x007D34),    // Vivid Green
            new Color(0xF6768E),    // Strong Purplish Pink
            new Color(0x00538A),    // Strong Blue
            new Color(0xFF7A5C),    // Strong Yellowish Pink
            new Color(0x53377A),    // Strong Violet
            new Color(0xFF8E00),    // Vivid Orange Yellow
            new Color(0xB32851),    // Strong Purplish Red
            new Color(0xF4C800),    // Vivid Greenish Yellow
            new Color(0x7F180D),    // Strong Reddish Brown
            new Color(0x93AA00),    // Vivid Yellowish Green
            new Color(0x593315),    // Deep Yellowish Brown
            new Color(0xF13A13),    // Vivid Reddish Orange
            new Color(0x232C16),    // Dark Olive Green
    };
    
    protected long worldGenSeed;
    protected Layer parent;
    protected long chunkSeed;
    protected long baseSeed;

    public Layer(final long seed)
    {
        baseSeed = seed;
        baseSeed *= baseSeed * MULT + ADD;
        baseSeed += seed;
        baseSeed *= baseSeed * MULT + ADD;
        baseSeed += seed;
        baseSeed *= baseSeed * MULT + ADD;
        baseSeed += seed;
    }

    public Layer(final long seed, final Layer parent)
    {
        this(seed);
        this.parent = parent;
    }

    public static Layer magnify(final long seed, Layer layer, final int n)
    {
        for (int i = 0; i < n; ++i) layer = new LayerZoom(seed + i, layer);
        return layer;
    }

    public static Layer initBiomes(final long seed)
    {
        Layer continent = new LayerIsland(1L);
        continent = new LayerFuzzyZoom(2000L, continent);
        continent = new LayerAddIsland(1L, continent);
        continent = new LayerZoom(2001L, continent);
        continent = new LayerAddIsland(2L, continent);
        continent = new LayerZoom(2002L, continent);
        continent = new LayerAddIsland(3L, continent);
        continent = new LayerZoom(2003L, continent);
        continent = new LayerAddIsland(4L, continent);
        continent = new LayerDeepOcean(4L, continent);
        // At this point, the output of continent only contains PLAINS, OCEAN and DEEP OCEAN.

        // Create Biomes
        Layer biomes = new LayerBiome(200L, continent);
        biomes = new LayerLakes(200L, biomes);
        biomes = magnify(1000L, biomes, 2);
        biomes = new LayerBiomeEdge(1000L, biomes);
        biomes = new LayerZoom(1000, biomes);
        biomes = new LayerAddIsland(3L, biomes);
        biomes = new LayerZoom(1001, biomes);
        biomes = new LayerShore(1000L, biomes);
        biomes = new LayerZoom(1002, biomes);
        biomes = new LayerZoom(1003, biomes);

        // below river stuff
        biomes = new LayerSmooth(1000L, biomes);

        // Create Rivers
        Layer rivers = magnify(1000L, continent, 2);
        rivers = new LayerRiverInit(100L, rivers);
        rivers = magnify(1000L, rivers, 6);
        rivers = new LayerRiver(1L, rivers);
        rivers = new LayerSmooth(1000L, rivers);

        // Mix rivers
        Layer mixed = new LayerRiverMix(100L, biomes, rivers).initWorldGenSeed(seed);
        mixed = Layer.magnify(1000L, mixed, 2);
        return new LayerSmooth(1001L, mixed).initWorldGenSeed(seed);
    }

    public static void drawImageBiome(int size, Layer genlayer, String name)
    {
        drawImage(size, genlayer, name, (i) -> Biome.values()[i].color);
    }

    public static void drawImageOther(int size, Layer genlayer, String name)
    {
        drawImage(size, genlayer, name, (i) -> COLORS[i]);
    }

    public static void drawImage(int size, Layer genlayer, String name, IntFunction<Color> gibColor)
    {
        try
        {
            File outFile = new File(name + ".png");
            int[] ints = genlayer.getInts(-size/2, -size/2, size, size);
            BufferedImage outBitmap = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) outBitmap.getGraphics();
            graphics.clearRect(0, 0, size, size);
            for (int x = 0; x < size; x++)
            {
                for (int z = 0; z < size; z++)
                {
                    int id = ints[x * size + z];
                    if (id == -1 || x == size/2 || z == size/2)
                    {
                        graphics.setColor(Color.BLUE);
                    }
                    else
                    {
                        graphics.setColor(gibColor.apply(id));
                    }
                    //noinspection SuspiciousNameCombination
                    graphics.drawRect(z, x, 1, 1);
                }
            }
            ImageIO.write(outBitmap, "PNG", outFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Layer initRock(final long seed, final Rock[] rocks)
    {
        Layer parent = new LayerRockInit(1L, rocks);
        parent = new LayerFuzzyZoom(2000L, parent);
        parent = new LayerZoom(2001L, parent);
        parent = new LayerZoom(2002L, parent);
        parent = new LayerZoom(2003L, parent);
        parent = new LayerSmooth(1000L, parent);
        parent = new LayerZoom(1000, parent);
        parent = new LayerZoom(1001, parent);
        parent = new LayerZoom(1002, parent);
        parent = new LayerZoom(1003, parent);
        parent = new LayerZoom(1004, parent);
        parent = new LayerSmooth(1000L, parent);
        return new LayerVoronoiZoom(10L, parent).initWorldGenSeed(seed);
    }

    public static Layer initStability(final long seed)
    {
        Layer continent = new LayerStabilityInit(1L + seed); // seed!
        continent = new LayerFuzzyZoom(2000L, continent);
        continent = new LayerZoom(2001L, continent);
        continent = new LayerZoom(2002L, continent);
        continent = new LayerZoom(2003L, continent);
        continent = magnify(1000L, continent, 2);
        continent = new LayerSmooth(1000L, continent);
        continent = new LayerZoom(1000, continent);
        continent = new LayerZoom(1001, continent);
        continent = new LayerZoom(1002, continent);
        continent = new LayerZoom(1003, continent);
        continent = new LayerSmooth(1000L, continent);
        continent = new LayerVoronoiZoom(10L, continent);
        return continent.initWorldGenSeed(seed);
    }

    public static Layer initDrain(final long seed)
    {
        Layer continent = new LayerDrainageInit(1L);
        continent = new LayerAddGeneric(1L, continent, Drainage.MIN, Drainage.MAX);
        continent = new LayerFuzzyZoom(2000L, continent);
        continent = new LayerAddGeneric(1L, continent, Drainage.MIN, Drainage.MAX);
        continent = new LayerZoom(2001L, continent);
        continent = new LayerAddGeneric(2L, continent, Drainage.MIN, Drainage.MAX);
        continent = new LayerMixGeneric(88L, continent, Drainage.MIN, Drainage.MAX);
        continent = new LayerZoom(2002L, continent);
        continent = new LayerAddGeneric(3L, continent, Drainage.MIN, Drainage.MAX);
        continent = new LayerZoom(2003L, continent);
        continent = new LayerAddGeneric(4L, continent, Drainage.MIN, Drainage.MAX);
        continent = LayerZoom.magnify(1000L, continent, 2);
        continent = new LayerSmooth(1000L, continent);
        continent = new LayerMixGeneric(1000, continent, Drainage.MIN, Drainage.MAX);
        continent = new LayerZoom(1000, continent);
        continent = new LayerZoom(1001, continent);
        continent = new LayerSmooth(1000L, continent);
        return continent.initWorldGenSeed(seed);
    }

    public Layer initWorldGenSeed(final long seed)
    {
        worldGenSeed = seed;
        if (parent != null) parent.initWorldGenSeed(seed);

        worldGenSeed *= worldGenSeed * MULT + ADD;
        worldGenSeed += baseSeed;
        worldGenSeed *= worldGenSeed * MULT + ADD;
        worldGenSeed += baseSeed;
        worldGenSeed *= worldGenSeed * MULT + ADD;
        worldGenSeed += baseSeed;
        return this;
    }

    public void initChunkSeed(final long x, final long y)
    {
        chunkSeed = worldGenSeed;
        chunkSeed *= chunkSeed * MULT + ADD;
        chunkSeed += x;
        chunkSeed *= chunkSeed * MULT + ADD;
        chunkSeed += y;
        chunkSeed *= chunkSeed * MULT + ADD;
        chunkSeed += x;
        chunkSeed *= chunkSeed * MULT + ADD;
        chunkSeed += y;
    }

    protected int nextInt(final int limit)
    {
        int i = (int) ((chunkSeed >> 24) % (long)limit);
        if (i < 0) i += limit;
        chunkSeed *= chunkSeed * MULT + ADD;
        chunkSeed += worldGenSeed;
        return i;
    }

    protected int selectRandom(final int... ints)
    {
        return ints[nextInt(ints.length)];
    }

    public abstract int[] getInts(int x, int y, int sizeX, int sizeY);
}
