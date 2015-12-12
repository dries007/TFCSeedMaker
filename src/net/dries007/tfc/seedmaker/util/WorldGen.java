package net.dries007.tfc.seedmaker.util;

import net.dries007.tfc.seedmaker.genlayers.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static net.dries007.tfc.seedmaker.util.Biome.SPAWNLIST;

/**
 * @author Dries007
 */
public class WorldGen implements Runnable
{
    public final long seed;
    public final GenLayer rockLayer0;
    public final GenLayer rockLayer1;
    public final GenLayer rockLayer2;
    public final GenLayer genBiomes;
    public final GenLayer biomeIndexLayer;
    public final File folder;

    private Coords spawn;

    public WorldGen(long seed)
    {
        this.seed = seed;

        folder = new File(String.valueOf(seed));
        if (!folder.exists()) folder.mkdir();

        rockLayer0 = initializeRock(seed + 1, RockType.LAYER0);
        rockLayer1 = initializeRock(seed + 2, RockType.LAYER1);
        rockLayer2 = initializeRock(seed + 3, RockType.LAYER2);

        genBiomes = initBiomegen();
        biomeIndexLayer = new GenLayerSmooth(1001L, magnify(1000L, genBiomes, 2));

        genBiomes.initWorldGenSeed(seed);
        biomeIndexLayer.initWorldGenSeed(seed);
    }

    public GenLayer magnify(long seed, GenLayer layer, int n)
    {
        for (int i = 0; i < n; ++i) layer = new GenLayerZoom(seed + i, layer);
        return layer;
    }

    private GenLayer initBiomegen()
    {
        GenLayer continent = genContinent(0);
        continent = new GenLayerDeepOcean(4L, continent);

        //Create Biomes
        GenLayer biomes = magnify(1000L, continent, 0);
        biomes = new GenLayerBiome(200L, biomes);
        biomes = new GenLayerLakes(200L, biomes);
        biomes = magnify(1000L, biomes, 2);
        biomes = new GenLayerBiomeEdge(1000L, biomes);
        biomes = new GenLayerZoom(1000, biomes);
        biomes = new GenLayerAddIsland(3L, biomes);
        biomes = new GenLayerZoom(1001, biomes);
        biomes = new GenLayerShore(1000L, biomes);
        biomes = new GenLayerZoom(1002, biomes);
        biomes = new GenLayerZoom(1003, biomes);
        biomes = new GenLayerSmooth(1000L, biomes);

        //Create Rivers
        GenLayer rivers = magnify(1000L, continent, 2);
        rivers = new GenLayerRiverInit(100L, rivers);
        rivers = magnify(1000L, rivers, 6);
        rivers = new GenLayerRiver(1L, rivers);
        rivers = new GenLayerSmooth(1000L, rivers);

        return new GenLayerRiverMix(100L, biomes, rivers);
    }

    public GenLayer genContinent(long seed)
    {
        GenLayer parent = new GenLayerIsland(1L + seed);
        parent = new GenLayerFuzzyZoom(2000L, parent);
        parent = new GenLayerAddIsland(1L, parent);
        parent = new GenLayerZoom(2001L, parent);
        parent = new GenLayerAddIsland(2L, parent);
        parent = new GenLayerZoom(2002L, parent);
        parent = new GenLayerAddIsland(3L, parent);
        parent = new GenLayerZoom(2003L, parent);
        return new GenLayerAddIsland(4L, parent);
    }

    private GenLayer initializeRock(long seed, RockType[] rocks)
    {
        GenLayer parent = new GenLayerRockInit(1L, rocks);
        parent = new GenLayerFuzzyZoom(2000L, parent);
        parent = new GenLayerZoom(2001L, parent);
        parent = new GenLayerZoom(2002L, parent);
        parent = new GenLayerZoom(2003L, parent);
        parent = new GenLayerSmooth(1000L, parent);
        for (int zoomLevel = 0; zoomLevel < 5; ++zoomLevel) parent = new GenLayerZoom(1000 + zoomLevel, parent);
        parent = new GenLayerSmooth(1000L, parent).initWorldGenSeed(seed);
        return new GenLayerVoronoiZoom(10L, parent).initWorldGenSeed(seed);
    }

    private Coords findBiomePosition(int xCoord, int zCoord, int radius, List biomeList, Random rand)
    {
        int xMin = xCoord - radius >> 2;
        int zMin = zCoord - radius >> 2;
        int xMax = xCoord + radius >> 2;
        int zMax = zCoord + radius >> 2;
        int xSize = xMax - xMin + 1;
        int zSize = zMax - zMin + 1;
        int[] aint = genBiomes.getInts(xMin, zMin, xSize, zSize);
        Coords chunkposition = null;
        int j2 = 0;

        for (int k2 = 0; k2 < xSize * zSize; ++k2)
        {
            int x = xMin + k2 % xSize << 2;
            int z = zMin + k2 / xSize << 2;
            Biome biomegenbase = Biome.LIST[aint[k2]];

            if (biomeList.contains(biomegenbase) && (chunkposition == null || rand.nextInt(j2 + 1) == 0))
            {
                chunkposition = new Coords(x, z);
                ++j2;
            }
        }

        return chunkposition;
    }

    private Coords createSpawn()
    {
        Random rand = new Random(seed);

        Coords chunkCoord = null;
        int xOffset = 0;
        int xCoord = 0;
        //int yCoord = Global.SEALEVEL+1;
        int zCoord = 10000;
        int startingZ = 5000 + rand.nextInt(10000);

        while (chunkCoord == null)
        {
            chunkCoord = findBiomePosition(xOffset, -startingZ, 64, SPAWNLIST, rand);
            if (chunkCoord != null)
            {
                xCoord = chunkCoord.x;
                zCoord = chunkCoord.z;
            }
            else
            {
                xOffset += 64;
            }
        }

//        int var9 = 0;
//        while (!canCoordinateBeSpawn(xCoord, zCoord))
//        {
//            xCoord += rand.nextInt(16) - rand.nextInt(16);
//            zCoord += rand.nextInt(16) - rand.nextInt(16);
//            ++var9;
//            if (var9 == 1000)
//                break;
//        }

        return new Coords(xCoord, zCoord);
    }

//    public boolean canCoordinateBeSpawn(int x, int z)
//    {
//        int y = worldObj.getTopSolidOrLiquidBlock(x, z) - 1;
//        if(y < Global.SEALEVEL || y > Global.SEALEVEL + 25) return false;
//        Block b = worldObj.getBlock(x, y, z);
//        return TFC_Core.isSand(b) || TFC_Core.isGrass(b);
//    }

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

        WorldGen worldGen = (WorldGen) o;

        return seed == worldGen.seed;
    }

    public Coords getSpawn()
    {
        if (spawn == null) spawn = createSpawn();
        return spawn;
    }

    @Override
    public void run()
    {
        try
        {
            File spawn = new File(folder, "spawn.txt");
            PrintWriter pw = new PrintWriter(spawn);
            pw.println(getSpawn());
            pw.close();

            System.out.println("Spawn for " + seed + " is " + getSpawn());
            final int size = 512 * 8;

            drawImage(folder, "Biomes", size, getSpawn(), genBiomes, Biome.COLORS);
            drawImage(folder, "BiomeIndexes", size, getSpawn(), biomeIndexLayer, Biome.COLORS);

            drawImage(folder, "Top", size, getSpawn(), rockLayer0, RockType.COLORS);
            drawImage(folder, "Middle", size, getSpawn(), rockLayer1, RockType.COLORS);
            drawImage(folder, "Bottom", size, getSpawn(), rockLayer2, RockType.COLORS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void drawImage(File folder, String name, int size, Coords center, GenLayer layer, Color[] list)
    {
        try
        {
            File outFile = new File(folder, name + ".png");
            if (outFile.exists()) outFile.delete();
            int[] ints = layer.getInts(center.x - (size / 2), center.z - (size / 2), size, size);
            BufferedImage outBitmap = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) outBitmap.getGraphics();
            graphics.clearRect(0, 0, size, size);
            for (int x = 0; x < size; x++)
            {
                for (int y = 0; y < size; y++)
                {
                    graphics.setColor(list[ints[x + y * size]]);
                    graphics.drawRect(x, y, 1, 1);
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
