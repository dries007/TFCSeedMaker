package net.dries007.tfc.seedmaker.util;

import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import com.google.gson.JsonObject;
import net.dries007.tfc.seedmaker.datatypes.Biome;
import net.dries007.tfc.seedmaker.datatypes.Rock;
import net.dries007.tfc.seedmaker.datatypes.Tree;
import net.dries007.tfc.seedmaker.genlayers.Layer;
import net.dries007.tfc.seedmaker.genlayers.LayerSmooth;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Dries007
 */
public class WorldGen implements Runnable
{
    private static final int[][] COLORS = {Biome.COLORS, Rock.COLORS, Rock.COLORS, Rock.COLORS, Tree.COLORS, Tree.COLORS, Tree.COLORS};
    private static final String[] FILENAMES = {"Combined", "Rock_Top", "Rock_Middle", "Rock_Bottom", "Tree_0", "Tree_1", "Tree_2"};

    public final String seedString;
    public final long seed;
    public final Layer genBiomes;
    public final Layer biomeIndexLayer;
    public final Layer rockLayer0;
    public final Layer rockLayer1;
    public final Layer rockLayer2;
    public final Layer treesLayer0;
    public final Layer treesLayer1;
    public final Layer treesLayer2;
    public final Layer evtIndexLayer;
    public final Layer rainfallLayer;
    public final Layer stabilityLayer;
    public final Layer phIndexLayer;
    public final Layer drainageLayer;
    public final File folder;

    private final boolean treesAboveWater;
    private final boolean rocksInWater;
    private final int radius;
    private final int chunkSize;
    private final boolean map;
    private final int expectedChunkCount;

    private Coords spawn;
    private float oceanRatio;
    private Set<Biome> biomeSet;
    private Set<Rock> rockSet;
    private Set<Tree> treeSet;
    private int chunkCount;
    private long time;

    public WorldGen(String seedString, boolean treesAboveWater, boolean rocksInWater, int radius, int chunkSize, boolean map)
    {
        this.treesAboveWater = treesAboveWater;
        this.rocksInWater = rocksInWater;
        this.radius = radius;
        this.chunkSize = chunkSize;
        this.map = map;
        this.expectedChunkCount = radius * radius * 4 / (chunkSize * chunkSize);
        this.seedString = seedString == null ? "" : seedString;
        seed = Helper.parseSeed(this.seedString);

        folder = new File(String.valueOf(seed));
        folder.mkdir();

        genBiomes = Layer.initBiomes(seed);
        biomeIndexLayer = new LayerSmooth(1001L, Layer.magnify(1000L, genBiomes, 2)).initWorldGenSeed(seed);

        rockLayer0 = Layer.initRock(seed + 1, Rock.LAYER0);
        rockLayer1 = Layer.initRock(seed + 2, Rock.LAYER1);
        rockLayer2 = Layer.initRock(seed + 3, Rock.LAYER2);

        treesLayer0 = Layer.initTree(seed + 4, Tree.TREE_ARRAY);
        treesLayer1 = Layer.initTree(seed + 5, Tree.TREE_ARRAY);
        treesLayer2 = Layer.initTree(seed + 6, Tree.TREE_ARRAY);

        evtIndexLayer = Layer.initEvt(seed + 7);
        rainfallLayer = Layer.initRain(seed + 8);
        stabilityLayer = Layer.initStability(seed + 9);
        phIndexLayer = Layer.initPh(seed + 10);
        drainageLayer = Layer.initDrain(seed + 11);
    }

    public Coords findBiomePosition(int xCoord, int zCoord, int radius, List biomeList, Random rand)
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
        int zCoord = 10000;
        int startingZ = 5000 + rand.nextInt(10000);

        while (chunkCoord == null)
        {
            chunkCoord = findBiomePosition(xOffset, -startingZ, 64, Biome.SPAWNLIST, rand);
            if (chunkCoord != null)
            {
                xCoord = chunkCoord.x;
                zCoord = chunkCoord.y;
            }
            else
            {
                xOffset += 64;
            }
        }

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
        final long start = System.currentTimeMillis();

        Coords coords = getSpawn();
        Set<Biome> biomeSet = EnumSet.noneOf(Biome.class);
        Set<Rock> rockSet = EnumSet.noneOf(Rock.class);
        Set<Tree> treeSet = EnumSet.noneOf(Tree.class);

        PngWriter[] writers = map ? Helper.prepareGraphics(FILENAMES, radius * 2, folder) : null;

        System.out.println("Seed: " + seed + " Spawn: " + coords);
        int chunkCount = 0;
        float oceanRatio = 0;
        final int xOffset = coords.x - radius;
        for (int y = coords.y - radius; y < coords.y + radius; y += chunkSize)
        {
            ImageLineInt[][] imageLines = null;
            if (map)
            {
                imageLines = new ImageLineInt[writers.length][];
                for (int i = 0; i < writers.length; i++)
                {
                    imageLines[i] = new ImageLineInt[chunkSize];
                    for (int j = 0; j < chunkSize; j++) imageLines[i][j] = new ImageLineInt(writers[0].imgInfo);
                }
            }
            for (int x = coords.x - radius; x < coords.x + radius; x += chunkSize)
            {
                chunkCount++;
                System.out.println("Seed " + seed + " Chunk " + chunkCount + " / " + expectedChunkCount);
                final int[] biomes = biomeIndexLayer.getInts(x, y, chunkSize, chunkSize);
                final int[] rocks0 = rockLayer0.getInts(x, y, chunkSize, chunkSize);
                final int[] rocks1 = rockLayer1.getInts(x, y, chunkSize, chunkSize);
                final int[] rocks2 = rockLayer2.getInts(x, y, chunkSize, chunkSize);
                final int[] trees0 = treesLayer0.getInts(x, y, chunkSize, chunkSize);
                final int[] trees1 = treesLayer1.getInts(x, y, chunkSize, chunkSize);
                final int[] trees2 = treesLayer2.getInts(x, y, chunkSize, chunkSize);
                final int[] evts = evtIndexLayer.getInts(x, y, chunkSize, chunkSize);
                final int[] rains = rainfallLayer.getInts(x, y, chunkSize, chunkSize);
                final int[] stabilitys = stabilityLayer.getInts(x, y, chunkSize, chunkSize);
                final int[] phs = phIndexLayer.getInts(x, y, chunkSize, chunkSize);
                final int[] drainages = drainageLayer.getInts(x, y, chunkSize, chunkSize);

                final int[][] layers = {biomes, rocks0, rocks1, rocks2, trees0, trees1, trees2};

                int oceans = 0;

                for (int yy = 0; yy < chunkSize; yy++)
                {
                    for (int xx = 0; xx < chunkSize; xx++)
                    {
                        final int i = xx + yy * chunkSize;

                        final int biomeId = biomes[i];

                        if (Biome.isOceanicBiome(biomeId)) oceans++;
                        biomeSet.add(Biome.LIST[biomeId]);

                        if (rocksInWater || !Biome.isWaterBiome(biomeId))
                        {
                            rockSet.add(Rock.LIST[rocks0[i]]);
                            rockSet.add(Rock.LIST[rocks1[i]]);
                            rockSet.add(Rock.LIST[rocks2[i]]);
                        }
                        if (treesAboveWater || !Biome.isWaterBiome(biomeId))
                        {
                            treeSet.add(Tree.LIST[trees0[i]]);
                            treeSet.add(Tree.LIST[trees1[i]]);
                            treeSet.add(Tree.LIST[trees2[i]]);
                        }

                        if (map)
                        {
                            if (x + xx % 1000 != 0 && y + yy % 1000 != 0)
                            {
                                ImageLineHelper.setPixelRGB8(imageLines[0][yy], x + xx - xOffset, COLORS[0][biomeId]);
                            }
                            if (xx != 0 && yy != 0 && xx + 1 != chunkSize && yy + 1 != chunkSize)
                            {
                                for (int layer = 1; layer < imageLines.length; layer++)
                                {
                                    final int[] ints = layers[layer];
                                    final int us = ints[i];

                                    ImageLineHelper.setPixelRGB8(imageLines[layer][yy], x + xx - xOffset, COLORS[layer][us]);

                                    final int up = ints[xx + (yy + 1) * chunkSize];
                                    final int dn = ints[xx + (yy - 1) * chunkSize];
                                    final int lt = ints[(xx - 1) + yy * chunkSize];
                                    final int rt = ints[(xx + 1) + yy * chunkSize];

                                    if (us != up || us != dn || us != lt || us != rt)
                                    {
                                        ImageLineHelper.setPixelRGB8(imageLines[0][yy], x + xx - xOffset, COLORS[layer][us]);
                                    }
                                }
                            }
                            if (y + yy == coords.y || x + xx == coords.x)
                            {
                                for (ImageLineInt[] imageLine : imageLines) ImageLineHelper.setPixelRGB8(imageLine[yy], x + xx - xOffset, Color.pink.getRGB());
                            }
                        }
                    }
                }
                oceanRatio += (float) oceans / biomes.length;
            }

            if (map)
            {
                System.out.println("Saving lines for seed " + seed);
                for (int i = 0; i < writers.length; i++) for (int j = 0; j < chunkSize; j++) writers[i].writeRow(imageLines[i][j]);
            }
        }
        oceanRatio /= chunkCount;

        this.chunkCount = chunkCount;
        this.oceanRatio = oceanRatio;
        this.biomeSet = biomeSet;
        this.rockSet = rockSet;
        this.treeSet = treeSet;
        this.time = System.currentTimeMillis() - start;

        try
        {
            File file = new File(folder, seed + ".json");
            PrintWriter pw = new PrintWriter(file);
            Helper.GSON.toJson(toJson(), pw);
            pw.close();
            if (map) Helper.finishGraphics(writers);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Done " + seed + " in " + time / 1000.0 + "s.");
    }

    public JsonObject toJson()
    {
        JsonObject object = new JsonObject();
        object.addProperty("seed", seed);
        object.addProperty("seedString", seedString);
        object.addProperty("radius", radius);
        object.addProperty("chunkSize", chunkSize);
        object.addProperty("expectedChunkCount", expectedChunkCount);
        object.addProperty("chunkCount", chunkCount);
        object.addProperty("treesAboveWater", treesAboveWater);
        object.addProperty("rocksInWater", rocksInWater);
        object.addProperty("oceanRatio", oceanRatio);
        object.addProperty("time", time / 1000.0);
        object.add("spawn", getSpawn().toJson());
        object.add("biomes", Helper.toSortedJson(biomeSet));
        object.add("rocks", Helper.toSortedJson(rockSet));
        object.add("trees", Helper.toSortedJson(treeSet));
        return object;
    }
}
