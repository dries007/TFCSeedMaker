package net.dries007.tfc.seedmaker.util;

import net.dries007.tfc.seedmaker.datatypes.Biome;
import net.dries007.tfc.seedmaker.datatypes.RockType;
import net.dries007.tfc.seedmaker.datatypes.Tree;
import net.dries007.tfc.seedmaker.genlayers.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

/**
 * @author Dries007
 */
public class WorldGen implements Runnable
{
    public final long seed;
    public final GenLayer rockLayer0;
    public final GenLayer rockLayer1;
    public final GenLayer rockLayer2;
    public final GenLayer treesLayer0;
    public final GenLayer treesLayer1;
    public final GenLayer treesLayer2;
    public final GenLayer genBiomes;
    public final GenLayer biomeIndexLayer;
    public final File folder;

    private Coords spawn;

    public WorldGen(long seed)
    {
        this.seed = seed;

        folder = new File(String.valueOf(seed));
        //noinspection ResultOfMethodCallIgnored
        folder.mkdirs();

        rockLayer0 = initRock(seed + 1, RockType.LAYER0);
        rockLayer1 = initRock(seed + 2, RockType.LAYER1);
        rockLayer2 = initRock(seed + 3, RockType.LAYER2);

        treesLayer0 = initTree(seed + 4, Tree.TREE_ARRAY);
        treesLayer1 = initTree(seed + 5, Tree.TREE_ARRAY);
        treesLayer2 = initTree(seed + 6, Tree.TREE_ARRAY);

        genBiomes = initBiomegen(seed);
        biomeIndexLayer = new GenLayerSmooth(1001L, magnify(1000L, genBiomes, 2)).initWorldGenSeed(seed);
    }

    private static GenLayer initTree(long seed, Tree[] trees)
    {
        GenLayer parent = new GenLayerTreeInit(1L, trees);
        parent = new GenLayerFuzzyZoom(2000L, parent);
        parent = new GenLayerZoom(2001L, parent);
        parent = new GenLayerZoom(2002L, parent);
        parent = new GenLayerZoom(2003L, parent);
        parent = new GenLayerSmooth(1000L, parent);
        for (int zoomLevel = 0; zoomLevel < 5; ++zoomLevel)
        {
            parent = new GenLayerZoom(1000 + zoomLevel, parent);
        }
        parent = new GenLayerSmooth(1000L, parent);
        return new GenLayerVoronoiZoom(10L, parent).initWorldGenSeed(seed);
    }

    public static GenLayer magnify(long seed, GenLayer layer, int n)
    {
        for (int i = 0; i < n; ++i) layer = new GenLayerZoom(seed + i, layer);
        return layer;
    }

    private static GenLayer initBiomegen(long seed)
    {
        GenLayer continent = new GenLayerIsland(1L);
        continent = new GenLayerFuzzyZoom(2000L, continent);
        continent = new GenLayerAddIsland(1L, continent);
        continent = new GenLayerZoom(2001L, continent);
        continent = new GenLayerAddIsland(2L, continent);
        continent = new GenLayerZoom(2002L, continent);
        continent = new GenLayerAddIsland(3L, continent);
        continent = new GenLayerZoom(2003L, continent);
        continent = new GenLayerAddIsland(4L, continent);
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

        return new GenLayerRiverMix(100L, biomes, rivers).initWorldGenSeed(seed);
    }

    private static GenLayer initRock(long seed, RockType[] rocks)
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

//    public boolean canCoordinateBeSpawn(int x, int z)
//    {
//        int y = worldObj.getTopSolidOrLiquidBlock(x, z) - 1;
//        if(y < Global.SEALEVEL || y > Global.SEALEVEL + 25) return false;
//        Block b = worldObj.getBlock(x, y, z);
//        return TFC_Core.isSand(b) || TFC_Core.isGrass(b);
//    }

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
            chunkCoord = findBiomePosition(xOffset, -startingZ, 64, Biome.SPAWNLIST, rand);
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
            final int size = 1024 * 10;

//            Helper.drawPng(folder, "Biomes", size, getSpawn(), genBiomes, Biome.COLORS_EDGE, Biome.COLORS_FILL);
            Helper.drawPng(folder, "BiomeIndexes", size, getSpawn(), biomeIndexLayer, Biome.COLORS_EDGE, false);

            Helper.drawPng(folder, "Top", size, getSpawn(), rockLayer0, RockType.COLORS_EDGE, true);
            Helper.drawPng(folder, "Middle", size, getSpawn(), rockLayer1, RockType.COLORS_EDGE, true);
            Helper.drawPng(folder, "Bottom", size, getSpawn(), rockLayer2, RockType.COLORS_EDGE, true);

            Helper.drawPng(folder, "Tree0", size, getSpawn(), treesLayer0, Tree.COLORS_EDGE, true);
            Helper.drawPng(folder, "Tree1", size, getSpawn(), treesLayer1, Tree.COLORS_EDGE, true);
            Helper.drawPng(folder, "Tree2", size, getSpawn(), treesLayer2, Tree.COLORS_EDGE, true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
