package net.dries007.tfc.seedmaker.util;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import com.google.gson.JsonObject;
import net.dries007.tfc.seedmaker.datatypes.Biome;
import net.dries007.tfc.seedmaker.datatypes.Rock;
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

import static net.dries007.tfc.seedmaker.util.Layers.BIOMES;
import static net.dries007.tfc.seedmaker.util.Layers.COMBINED;

/**
 * @author Dries007
 */
public class WorldGen implements Runnable
{
    // Global array of all colors possible used in all maps.
	public static final int COLORS[] = new int[256];
	// Biomes ids overlap with others, so they have their own maps
    public static final int COLORS_BIOME[] = new int[256];

    public final String seedString;
    public final long seed;

    private final Layer genBiomes;
    private final Layer biomeIndexLayer;
    private final Layer rockTop;
    private final Layer rockMiddle;
    private final Layer rockBottom;
    private final Layer stabilityLayer;
    private final Layer drainageLayer;
    private final File folder;

    private final boolean rocksInWater;
    private final int radius;
    private final int chunkSize;
    private final boolean[] maps;
    private final int expectedChunkCount;

    private Coords spawn;
    private float oceanRatio;
    private Map<Biome, Double> biomeMap;
    private Map<Rock, Double> rockMapTop;
    private Map<Rock, Double> rockMapMiddle;
    private Map<Rock, Double> rockMapBottom;
    private int chunkCount;
    private long time;

    public WorldGen(String seedString, boolean rocksInWater, int radius, int chunkSize, List<String> maps)
    {
        this.rocksInWater = rocksInWater;
        this.chunkSize = chunkSize;
        this.radius = (int) Math.floor(radius / chunkSize) * chunkSize;
        this.maps = new boolean[Layers.values().length];
        // Find out what maps to draw
        for (String map : maps)
        {
            for (int i = 0; i < this.maps.length; i++)
            {
                if (map.equalsIgnoreCase(Layers.values()[i].name())) this.maps[i] = true;
            }
            if (map.equalsIgnoreCase("all"))
            {
                for (int i = 0; i < this.maps.length; i++) this.maps[i] = true;
            }
            else if (map.equalsIgnoreCase("rocks"))
            {
                this.maps[Layers.ROCK_TOP.ordinal()] = true;
                this.maps[Layers.ROCK_MIDDLE.ordinal()] = true;
                this.maps[Layers.ROCK_BOTTOM.ordinal()] = true;
            }
        }

        this.expectedChunkCount = radius * radius * 4 / (chunkSize * chunkSize);
        this.seedString = seedString == null ? "" : seedString;
        seed = Helper.parseSeed(this.seedString);

        folder = new File(String.valueOf(seed));
        folder.mkdir();

        // Make the generators for the int values
        genBiomes = Layer.initBiomes(seed);
        Layer.drawImageBiome(1024, genBiomes, "mixed");

        biomeIndexLayer = new LayerSmooth(1001L, Layer.magnify(1000L, genBiomes, 2)).initWorldGenSeed(seed);
        Layer.drawImageBiome(1024, biomeIndexLayer, "zoomed");

        rockTop = Layer.initRock(seed + 1, Rock.TOP);
        Layer.drawImageOther(1024, rockTop, "rockTOP");
        rockMiddle = Layer.initRock(seed + 2, Rock.MIDDLE);
        Layer.drawImageOther(1024, rockMiddle, "rockMIDDLE");
        rockBottom = Layer.initRock(seed + 3, Rock.BOTTOM);
        Layer.drawImageOther(1024, rockBottom, "rockBOTTOM");
        stabilityLayer = Layer.initStability(seed + 9);
        drainageLayer = Layer.initDrain(seed + 11);
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
        int yCoord = 0;
        int startingY = rand.nextInt(100);

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
        Map<Rock, Long> rockMapTop = new EnumMap<>(Rock.class);
        Map<Rock, Long> rockMapMiddle = new EnumMap<>(Rock.class);
        Map<Rock, Long> rockMapBottom = new EnumMap<>(Rock.class);

        // Fill maps with zeros to make later manipulating more optimized
        for (Biome type : Biome.values()) biomeMap.put(type, 0L);
        for (Rock type : Rock.values()) rockMapTop.put(type, 0L);
        for (Rock type : Rock.values()) rockMapMiddle.put(type, 0L);
        for (Rock type : Rock.values()) rockMapBottom.put(type, 0L);

        // Create an array of PngWriters or null if we don't want a layer.
        PngWriter[] writers = new PngWriter[Layers.values().length];
        for (int i = 0; i < writers.length; i++)
        {
            if (maps[i])
            {
                writers[i] = new PngWriter(new File(folder, Layers.values()[i].name().toLowerCase() + ".png"), new ImageInfo(radius * 2, radius * 2, 8, false), true);
            }
        }

        int chunkCount = 0;
        float oceanRatio = 0;
        final int xOffset = spawn.x - radius; // The lowest X coordinate, required because the image is always starting at 0;0, while the world has negative coordinates.
        // Per chunk of Y lines (Not the same as a Minecraft chunk!)
        for (int y = spawn.y - radius; y < spawn.y + radius; y += chunkSize)
        {
            // Make new image lines (aka rows in the image)
            ImageLineInt[][] imageLines = new ImageLineInt[writers.length][];
            for (int i = 0; i < writers.length; i++)
            {
                if (maps[i]) // only for images we want
                {
                    // Make one image row per row in the chunk
                    imageLines[i] = new ImageLineInt[chunkSize];
                    for (int j = 0; j < chunkSize; j++)
                    {
                        imageLines[i][j] = new ImageLineInt(writers[i].imgInfo);
                    }
                }
            }
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
                final int[] rocksTop = rockTop.getInts(x, y, chunkSize, chunkSize);
                final int[] rocksMiddle = rockMiddle.getInts(x, y, chunkSize, chunkSize);
                final int[] rocksBottom = rockBottom.getInts(x, y, chunkSize, chunkSize);
                final int[] stabilitys = stabilityLayer.getInts(x, y, chunkSize, chunkSize);
                final int[] drainages = drainageLayer.getInts(x, y, chunkSize, chunkSize);

                // Store it in one big array, that is more. The 'combined' layer is null because its special
                // Get rid of layers no longer used
                final int[][] layers = {null, biomes, rocksTop, rocksMiddle, rocksBottom, stabilitys, drainages};
                int oceans = 0;
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
                        if (Biome.isOceanicBiome(biomeId)) oceans++;
                        // Increase biome count
                        biomeMap.put(Biome.LIST[biomeId], biomeMap.get(Biome.LIST[biomeId]) + 1);

                        // Increase rock count, if not under water OR if under water is allowed.
                        if (rocksInWater || !Biome.isWaterBiome(biomeId))
                        {
                            rockMapTop.put(Rock.LIST[rocksTop[i]], rockMapTop.get(Rock.LIST[rocksTop[i]]) + 1);
                            rockMapMiddle.put(Rock.LIST[rocksMiddle[i]], rockMapMiddle.get(Rock.LIST[rocksMiddle[i]]) + 1);
                            rockMapBottom.put(Rock.LIST[rocksBottom[i]], rockMapBottom.get(Rock.LIST[rocksBottom[i]]) + 1);
                        }

                        // If we are drawing the combined layer draw the biome color. (but not if we are on a grid line)
                        if (maps[0] && (x + xx) % 1000 != 0 && (yy + y) % 1000 != 0)
                        {
                            // maps[0] and imageLines[0] are from COMBINED (id = 0)
                        	ImageLineHelper.setPixelRGB8(imageLines[0][yy], x + xx - xOffset, COLORS_BIOME[biomeId]);
                        }

                        // Per image
                        for (Layers layer : Layers.values())
                        {
                            // Skip the combined layer, its special
                            if (layer == COMBINED) continue;
                            // If we aren't drawing it, skip
                            if (!maps[layer.ordinal()]) continue;
                            // Get the int values
                            final int[] ints = layers[layer.ordinal()];
                            // The value at this column
                            final int us = ints[i];
                            // Draw the pixel on row yy, at the adjusted x coordinates.
                            int color = COLORS[us];
                            // Biome colors overlap, those get a seperate color
                            if (layer == BIOMES) color = COLORS_BIOME[us];

                            // Don't draw on grid lines
                            if ((x + xx) % 1000 != 0 && (yy + y) % 1000 != 0)
                            {
                                ImageLineHelper.setPixelRGB8(imageLines[layer.ordinal()][yy], x + xx - xOffset, color);
                            }

                            // if xx or yy isn't on a chunk's edge (because then we can't check the bordering column)
                            if (xx != 0 && yy != 0 && (xx + 1) != chunkSize && (yy + 1) != chunkSize)
                            {
                                // if a tree or rock layer and we are drawing the combined layer
                                if (layer.addToCombined && maps[0])
                                {
                                    // get the 4 neighbouring columns
                                    final int up = ints[xx + (yy + 1) * chunkSize];
                                    final int dn = ints[xx + (yy - 1) * chunkSize];
                                    final int lt = ints[(xx - 1) + yy * chunkSize];
                                    final int rt = ints[(xx + 1) + yy * chunkSize];

                                    // If there is any differences, draw the pixel
                                    if (us != up || us != dn || us != lt || us != rt)
                                    {
                                        ImageLineHelper.setPixelRGB8(imageLines[0][yy], x + xx - xOffset, color);
                                    }
                                }
                            }
                        }
                        // If we are on the spawn grid, draw a pink pixel
                        if (y + yy == spawn.y || x + xx == spawn.x)
                        {
                            for (ImageLineInt[] imageLine : imageLines) if (imageLine != null) ImageLineHelper.setPixelRGB8(imageLine[yy], x + xx - xOffset, Color.pink.getRGB());
                        }
                    }
                }
                // add up ocean percentage
                oceanRatio += (float) oceans / biomes.length;
            }

            System.out.println("Saving lines for seed " + seed);
            for (int i = 0; i < writers.length; i++) if (maps[i]) for (int j = 0; j < chunkSize; j++) writers[i].writeRow(imageLines[i][j]);
        }
        // OceanRatio is already on a per chunk average basis, so it only has to be divided by the amount of chunks.
        oceanRatio /= chunkCount;

        this.chunkCount = chunkCount;
        this.oceanRatio = oceanRatio;

        this.biomeMap = new EnumMap<>(Biome.class);
        this.rockMapTop = new EnumMap<>(Rock.class);
        this.rockMapMiddle = new EnumMap<>(Rock.class);
        this.rockMapBottom = new EnumMap<>(Rock.class);
//        this.treeMap0 = new EnumMap<>(Tree.class);
//        this.treeMap1 = new EnumMap<>(Tree.class);
//        this.treeMap2 = new EnumMap<>(Tree.class);

        // Put all of the data in the WorldGen object. They have to be divided by the column count (chunk size² * chunk count), because they are in absolute values.
        double devider = chunkCount * chunkSize * chunkSize;

        // Only add things that actually exist.
        for (Map.Entry<Biome, Long> entry : biomeMap.entrySet()) if (entry.getValue() != 0) this.biomeMap.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
        for (Map.Entry<Rock, Long> entry : rockMapTop.entrySet()) if (entry.getValue() != 0) this.rockMapTop.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
        for (Map.Entry<Rock, Long> entry : rockMapMiddle.entrySet()) if (entry.getValue() != 0) this.rockMapMiddle.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
        for (Map.Entry<Rock, Long> entry : rockMapBottom.entrySet()) if (entry.getValue() != 0) this.rockMapBottom.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
//        for (Map.Entry<Tree, Long> entry : treeMap0.entrySet()) if (entry.getValue() != 0) this.treeMap0.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
//        for (Map.Entry<Tree, Long> entry : treeMap1.entrySet()) if (entry.getValue() != 0) this.treeMap1.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);
//        for (Map.Entry<Tree, Long> entry : treeMap2.entrySet()) if (entry.getValue() != 0) this.treeMap2.put(entry.getKey(), Double.valueOf(entry.getValue()) / devider);

        this.time = System.currentTimeMillis() - start;

        // Close all of the png files and save the sumary as a json file
        try
        {
            for (PngWriter writer : writers) if (writer != null) writer.close();
            File file = new File(folder, seed + ".json");
            PrintWriter pw = new PrintWriter(file);
            Helper.GSON.toJson(toJson(), pw);
            pw.close();
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
        object.addProperty("rocksInWater", rocksInWater);
        object.addProperty("oceanRatio", oceanRatio);
        object.addProperty("time", time / 1000.0);
        object.add("spawn", getSpawn().toJson());
        object.add("biomes", Helper.toJson(biomeMap));
        object.add("rocksTop", Helper.toJson(rockMapTop));
        object.add("rocksMiddle", Helper.toJson(rockMapMiddle));
        object.add("rocksBottom", Helper.toJson(rockMapBottom));
        return object;
    }
}

