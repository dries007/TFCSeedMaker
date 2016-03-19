package net.dries007.tfc.seedmaker.util;

import ar.com.hjg.pngj.ImageInfo;
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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.dries007.tfc.seedmaker.util.Layers.COMBINED;

/**
 * @author Dries007
 */
public class WorldGen implements Runnable
{
    // Global array of all colors possible used in all maps.
    public static final int COLORS[] = new int[256];
    //private static final String[] FILENAMES = {"Combined", "Rock_Top", "Rock_Middle", "Rock_Bottom", "Tree_0", "Tree_1", "Tree_2", "EVT", "Rain", "Stability", "PH", "Drainage", "Biomes"};
    //private static final boolean[] COMBINE = {false, true, true, true, true, true, true, false, false, false, false, false, false};

    public final String seedString;
    public final long seed;
    private final Layer genBiomes;
    private final Layer biomeIndexLayer;
//    private final Layer rockLayer0;
//    private final Layer rockLayer1;
//    private final Layer rockLayer2;
//    private final Layer treesLayer0;
//    private final Layer treesLayer1;
//    private final Layer treesLayer2;
//    private final Layer evtIndexLayer;
//    private final Layer rainfallLayer;
//    private final Layer stabilityLayer;
//    private final Layer phIndexLayer;
//    private final Layer drainageLayer;
//    private final File folder;

//    private final boolean treesAboveWater;
//    private final boolean rocksInWater;
    private final int radius;
    private final int chunkSize;
//    private final boolean[] maps;
    private final int expectedChunkCount;

    private Coords spawn;
//    private float oceanRatio;
//    private Map<Biome, Double> biomeMap;
//    private Map<Rock, Double> rockMap0;
//    private Map<Rock, Double> rockMap1;
//    private Map<Rock, Double> rockMap2;
//    private Map<Tree, Double> treeMap0;
//    private Map<Tree, Double> treeMap1;
//    private Map<Tree, Double> treeMap2;
    private int chunkCount;
    private long time;

    //public WorldGen(String seedString, boolean treesAboveWater, boolean rocksInWater, int radius, int chunkSize, List<String> maps)
    public WorldGen(String seedString, int radius, int chunkSize)
    {
//        this.treesAboveWater = treesAboveWater;
//        this.rocksInWater = rocksInWater;
        this.radius = radius;
        this.chunkSize = chunkSize;
//        this.maps = new boolean[Layers.values().length];
        // Find out what maps to draw
//        for (String map : maps)
//        {
//            for (int i = 0; i < this.maps.length; i++)
//            {
//                if (map.equalsIgnoreCase(Layers.values()[i].name())) this.maps[i] = true;
//            }
//            if (map.equalsIgnoreCase("all"))
//            {
//                for (int i = 0; i < this.maps.length; i++) this.maps[i] = true;
//            }
//            else if (map.equalsIgnoreCase("trees"))
//            {
//                this.maps[Layers.TREE_0.ordinal()] = true;
//                this.maps[Layers.TREE_1.ordinal()] = true;
//                this.maps[Layers.TREE_2.ordinal()] = true;
//            }
//            else if (map.equalsIgnoreCase("rocks"))
//            {
//                this.maps[Layers.ROCK_TOP.ordinal()] = true;
//                this.maps[Layers.ROCK_MIDDLE.ordinal()] = true;
//                this.maps[Layers.ROCK_BOTTOM.ordinal()] = true;
//            }
//        }

        this.expectedChunkCount = radius * radius * 4 / (chunkSize * chunkSize);
        this.seedString = seedString == null ? "" : seedString;
        seed = Helper.parseSeed(this.seedString);

//        folder = new File(String.valueOf(seed));
//        folder.mkdir();

        // Make the generators for the int values
        genBiomes = Layer.initBiomes(seed);
        biomeIndexLayer = new LayerSmooth(1001L, Layer.magnify(1000L, genBiomes, 2)).initWorldGenSeed(seed);
//
//        rockLayer0 = Layer.initRock(seed + 1, Rock.LAYER0);
//        rockLayer1 = Layer.initRock(seed + 2, Rock.LAYER1);
//        rockLayer2 = Layer.initRock(seed + 3, Rock.LAYER2);
//
//        treesLayer0 = Layer.initTree(seed + 4, Tree.TREE_ARRAY);
//        treesLayer1 = Layer.initTree(seed + 5, Tree.TREE_ARRAY);
//        treesLayer2 = Layer.initTree(seed + 6, Tree.TREE_ARRAY);
//
//        evtIndexLayer = Layer.initEvt(seed + 7);
//        rainfallLayer = Layer.initRain(seed + 8);
//        stabilityLayer = Layer.initStability(seed + 9);
//        phIndexLayer = Layer.initPh(seed + 10);
//        drainageLayer = Layer.initDrain(seed + 11);
    }

    /**
     * Required for the spawn point finding
     * TFC code
     */
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

    /**
     * Find the world spawn.
     * TFC code
     */
    private Coords createSpawn()
    {
        Random rand = new Random(seed);

        Coords chunkCoord = null;
        int xOffset = 0;
        int xCoord = 0;
        int yCoord = 10000;
        int startingY = 5000 + rand.nextInt(10000);

        while (chunkCoord == null)
        {
            chunkCoord = findBiomePosition(xOffset, -startingY, 64, Biome.SPAWNLIST, rand);
            if (chunkCoord != null)
            {
                xCoord = chunkCoord.x;
                yCoord = chunkCoord.y;
            }
            else
            {
                xOffset += 64;
            }
        }

        return new Coords(xCoord, yCoord);
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

        Coords spawn = getSpawn();
        System.out.println("Seed: " + seed + " Spawn: " + spawn);

        // Make empty maps
        Map<Biome, Long> biomeMap = new EnumMap<>(Biome.class);
        Map<Rock, Long> rockMap0 = new EnumMap<>(Rock.class);
        Map<Rock, Long> rockMap1 = new EnumMap<>(Rock.class);
        Map<Rock, Long> rockMap2 = new EnumMap<>(Rock.class);
        Map<Tree, Long> treeMap0 = new EnumMap<>(Tree.class);
        Map<Tree, Long> treeMap1 = new EnumMap<>(Tree.class);
        Map<Tree, Long> treeMap2 = new EnumMap<>(Tree.class);

        // Fill maps with zeros to make later manipulating more optimized
        for (Biome type : Biome.values()) biomeMap.put(type, 0L);
        for (Rock type : Rock.values()) rockMap0.put(type, 0L);
        for (Rock type : Rock.values()) rockMap1.put(type, 0L);
        for (Rock type : Rock.values()) rockMap2.put(type, 0L);
        for (Tree type : Tree.values()) treeMap0.put(type, 0L);
        for (Tree type : Tree.values()) treeMap1.put(type, 0L);
        for (Tree type : Tree.values()) treeMap2.put(type, 0L);

        // Prepare the PNG writers
//        PngWriter[] writers = Helper.prepareGraphics(radius * 2, folder, maps);
        PngWriter writer = new PngWriter(new File(seed + ".png"), new ImageInfo(radius * 2, radius * 2, 8, false), true);

        int chunkCount = 0;
        float oceanRatio = 0;
        final int xOffset = spawn.x - radius; // The lowest X coordinate, required because the image is always starting at 0;0, while the world has negative coordinates.
        // Per chunk of Y lines (Not the same as a Minecraft chunk!)
        for (int y = spawn.y - radius; y < spawn.y + radius; y += chunkSize)
        {
            // Make new image lines (aka rows in the image)
//            ImageLineInt[][] imageLines = new ImageLineInt[writers.length][];
            ImageLineInt[] imageLines = new ImageLineInt[chunkSize];
            for (int j = 0; j < chunkSize; j++) imageLines[j] = new ImageLineInt(writer.imgInfo);

//            for (int i = 0; i < writers.length; i++)
//            {
//                if (maps[i]) // only for images we want
//                {
//                    // Make one image row per row in the chunk
//                    imageLines[i] = new ImageLineInt[chunkSize];
//                    for (int j = 0; j < chunkSize; j++) imageLines[i][j] = new ImageLineInt(writers[0].imgInfo);
//                }
//            }
            // Per chunk of X lines (again, not per se 16 like in MC. The bigger the better, but more RAM intensive)
            for (int x = spawn.x - radius; x < spawn.x + radius; x += chunkSize)
            {
                // Progress report
                chunkCount++;
                System.out.println("Seed " + seed + " Chunk " + chunkCount + " / " + expectedChunkCount);

                // Actually generate the chunks
                // The int values are the ID's given by TFC.
                // They are used to fetch the global color from the COLORS array.
                final int[] biomes = biomeIndexLayer.getInts(x, y, chunkSize, chunkSize);
//                final int[] rocks0 = rockLayer0.getInts(x, y, chunkSize, chunkSize);
//                final int[] rocks1 = rockLayer1.getInts(x, y, chunkSize, chunkSize);
//                final int[] rocks2 = rockLayer2.getInts(x, y, chunkSize, chunkSize);
//                final int[] trees0 = treesLayer0.getInts(x, y, chunkSize, chunkSize);
//                final int[] trees1 = treesLayer1.getInts(x, y, chunkSize, chunkSize);
//                final int[] trees2 = treesLayer2.getInts(x, y, chunkSize, chunkSize);
//                final int[] evts = evtIndexLayer.getInts(x, y, chunkSize, chunkSize);
//                final int[] rains = rainfallLayer.getInts(x, y, chunkSize, chunkSize);
//                final int[] stabilitys = stabilityLayer.getInts(x, y, chunkSize, chunkSize);
//                final int[] phs = phIndexLayer.getInts(x, y, chunkSize, chunkSize);
//                final int[] drainages = drainageLayer.getInts(x, y, chunkSize, chunkSize);

                // Store it in one big array, that is more. The 'combined' layer is null because its special
//                final int[][] layers = {null, biomes, rocks0, rocks1, rocks2, trees0, trees1, trees2, evts, rains, stabilitys, phs, drainages};

//                int oceans = 0;
                // YY and XX are the inner X and Y coordinates, they represent one column of blocks at a (X + XX) by (Y + YY)
                for (int yy = 0; yy < chunkSize; yy++)
                {
                    for (int xx = 0; xx < chunkSize; xx++)
                    {
                        // I is the index of the column within the chunk's data array
                        final int i = xx + yy * chunkSize;

                        // Because we need it a lot, save the biomeId eperately
                        final int biomeId = biomes[i];

                        // Count ocean columns
//                        if (Biome.isOceanicBiome(biomeId)) oceans++;

                        if ((x + xx) % 1000 != 0 && (y + yy) % 1000 != 0)
                        {
                            int rgb = 0x008000;


                            if (biomeId == Biome.OCEAN.id) rgb = 0x0000FF;
                            else if (biomeId == Biome.DEEP_OCEAN.id) rgb = 0x00008B;
                            else if (biomeId == Biome.LAKE.id) rgb = 0x00BFFF;
                            else if (biomeId == Biome.RIVER.id) rgb = 0x7FFFD4;

                            ImageLineHelper.setPixelRGB8(imageLines[yy], x + xx - xOffset, rgb);
                        }


                        // Increase biome count
//                        biomeMap.put(Biome.LIST[biomeId], biomeMap.get(Biome.LIST[biomeId]) + 1);

                        // Increase rock count, if not under water OR if under water is allowed.
//                        if (rocksInWater || !Biome.isWaterBiome(biomeId))
//                        {
//                            rockMap0.put(Rock.LIST[rocks0[i]], rockMap0.get(Rock.LIST[rocks0[i]]) + 1);
//                            rockMap1.put(Rock.LIST[rocks1[i]], rockMap1.get(Rock.LIST[rocks1[i]]) + 1);
//                            rockMap2.put(Rock.LIST[rocks2[i]], rockMap2.get(Rock.LIST[rocks2[i]]) + 1);
//                        }
                        // Increase tree count, if not under water OR if under water is allowed.
//                        if (treesAboveWater || !Biome.isWaterBiome(biomeId))
//                        {
//                            treeMap0.put(Tree.LIST[trees0[i]], treeMap0.get(Tree.LIST[trees0[i]]) + 1);
//                            treeMap1.put(Tree.LIST[trees1[i]], treeMap1.get(Tree.LIST[trees1[i]]) + 1);
//                            treeMap2.put(Tree.LIST[trees2[i]], treeMap2.get(Tree.LIST[trees2[i]]) + 1);
//                        }

                        // If we are drawing the combined layer draw the biome color. (But not if we are on a 1000 x 1000 grid line)
//                        if (maps[COMBINED.ordinal()] && x + xx % 1000 != 0 && y + yy % 1000 != 0)
//                        {
//                            ImageLineHelper.setPixelRGB8(imageLines[0][yy], x + xx - xOffset, COLORS[biomeId]);
//                        }
                        // if xx or yy isn't on a chunk's edge (because then we can't check the bordering column)
//                        if (xx != 0 && yy != 0 && xx + 1 != chunkSize && yy + 1 != chunkSize)
//                        {
//                            // Per image
//                            for (Layers layer : Layers.values())
//                            {
//                                // Skip the combined layer, its special
//                                if (layer == COMBINED) continue;
//                                // If we aren't drawing it, skip
//                                if (!maps[layer.ordinal()]) continue;
//                                // Get the int values
//                                final int[] ints = layers[layer.ordinal()];
//                                // The value at this column
//                                final int us = ints[i];
//
//                                // Draw the pixel on row yy, at the adjusted x coordinates
//                                ImageLineHelper.setPixelRGB8(imageLines[layer.ordinal()][yy], x + xx - xOffset, COLORS[us]);
//
//                                // if a tree or rock layer and we are drawing the combined layer
//                                if (layer.addToCombined && maps[COMBINED.ordinal()])
//                                {
//                                    // get the 4 neighbouring columns
//                                    final int up = ints[xx + (yy + 1) * chunkSize];
//                                    final int dn = ints[xx + (yy - 1) * chunkSize];
//                                    final int lt = ints[(xx - 1) + yy * chunkSize];
//                                    final int rt = ints[(xx + 1) + yy * chunkSize];
//
//                                    // If there is any differences, draw the pixel
//                                    if (us != up || us != dn || us != lt || us != rt)
//                                    {
//                                        ImageLineHelper.setPixelRGB8(imageLines[0][yy], x + xx - xOffset, COLORS[us]);
//                                    }
//                                }
//                            }
//                        }
                        // If we are on the spawn grid, draw a pink pixel
                        if ((y + yy <= spawn.y + 2 && y + yy >= spawn.y - 2) || (x + xx <= spawn.x + 2 && x + xx >= spawn.x - 2))
                        {
//                            for (ImageLineInt[] imageLine : imageLines) if (imageLine != null)
                                ImageLineHelper.setPixelRGB8(imageLines[yy], x + xx - xOffset, Color.pink.getRGB());
                        }
                    }
                }
                // add up ocean percentage
//                oceanRatio += (float) oceans / biomes.length;
            }

            System.out.println("Saving lines for seed " + seed);
//            for (int i = 0; i < writers.length; i++) if (maps[i])
                for (int j = 0; j < chunkSize; j++) writer.writeRow(imageLines[j]);
        }
        // OceanRatio is already on a per chunk average basis, so it only has to be divided by the amount of chunks.
//        oceanRatio /= chunkCount;
//
//        this.chunkCount = chunkCount;
//        this.oceanRatio = oceanRatio;
//
//        this.biomeMap = new EnumMap<>(Biome.class);
//        this.rockMap0 = new EnumMap<>(Rock.class);
//        this.rockMap1 = new EnumMap<>(Rock.class);
//        this.rockMap2 = new EnumMap<>(Rock.class);
//        this.treeMap0 = new EnumMap<>(Tree.class);
//        this.treeMap1 = new EnumMap<>(Tree.class);
//        this.treeMap2 = new EnumMap<>(Tree.class);

        // Put all of the data in the WorldGen object. They have to be divided by the column count (chunk size² * chunk count), because they are in absolute values.
//        double devider = chunkCount * chunkSize * chunkSize;

        // Only add things that actually exist.
//        for (Map.Entry<Biome, Long> entry : biomeMap.entrySet()) if (entry.getValue() != 0) this.biomeMap.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
//        for (Map.Entry<Rock, Long> entry : rockMap0.entrySet()) if (entry.getValue() != 0) this.rockMap0.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
//        for (Map.Entry<Rock, Long> entry : rockMap1.entrySet()) if (entry.getValue() != 0) this.rockMap1.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
//        for (Map.Entry<Rock, Long> entry : rockMap2.entrySet()) if (entry.getValue() != 0) this.rockMap2.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
//        for (Map.Entry<Tree, Long> entry : treeMap0.entrySet()) if (entry.getValue() != 0) this.treeMap0.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
//        for (Map.Entry<Tree, Long> entry : treeMap1.entrySet()) if (entry.getValue() != 0) this.treeMap1.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
//        for (Map.Entry<Tree, Long> entry : treeMap2.entrySet()) if (entry.getValue() != 0) this.treeMap2.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);

        this.time = System.currentTimeMillis() - start;

        // Close all of the png files and save the sumary as a json file
        try
        {
//            for (PngWriter writer : writers) if (writer != null)
                writer.close();
//            File file = new File(folder, seed + ".json");
//            PrintWriter pw = new PrintWriter(file);
//            Helper.GSON.toJson(toJson(), pw);
//            pw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Done " + seed + " in " + time / 1000.0 + "s.");
    }

//    public JsonObject toJson()
//    {
//        JsonObject object = new JsonObject();
//        object.addProperty("seed", seed);
//        object.addProperty("seedString", seedString);
//        object.addProperty("radius", radius);
//        object.addProperty("chunkSize", chunkSize);
//        object.addProperty("expectedChunkCount", expectedChunkCount);
//        object.addProperty("chunkCount", chunkCount);
//        object.addProperty("treesAboveWater", treesAboveWater);
//        object.addProperty("rocksInWater", rocksInWater);
//        object.addProperty("oceanRatio", oceanRatio);
//        object.addProperty("time", time / 1000.0);
//        object.add("spawn", getSpawn().toJson());
//        object.add("biomes", Helper.toJson(biomeMap));
//        object.add("rocksTop", Helper.toJson(rockMap0));
//        object.add("rocksMiddle", Helper.toJson(rockMap1));
//        object.add("rocksBottom", Helper.toJson(rockMap2));
//        object.add("trees0", Helper.toJson(treeMap0));
//        object.add("trees1", Helper.toJson(treeMap1));
//        object.add("trees2", Helper.toJson(treeMap2));
//        return object;
//    }
}
