package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.datatypes.*;

public abstract class Layer
{
    protected long worldGenSeed;
    protected Layer parent;
    protected long chunkSeed;
    protected long baseSeed;

    public Layer(final long seed)
    {
        baseSeed = seed;
        baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
        baseSeed += seed;
        baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
        baseSeed += seed;
        baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
        baseSeed += seed;
    }

    public Layer(final long seed, final Layer parent)
    {
        this(seed);
        this.parent = parent;
    }

    public static Layer initTree(final long seed, final Tree[] trees)
    {
        Layer parent = new LayerTreeInit(1L, trees);
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

        //Create Biomes
        Layer biomes = magnify(1000L, continent, 0);
        biomes = new LayerBiome(200L, biomes);
        biomes = new LayerLakes(200L, biomes);
        biomes = magnify(1000L, biomes, 2);
        biomes = new LayerBiomeEdge(1000L, biomes);
        biomes = new LayerZoom(1000, biomes);
        biomes = new LayerAddIsland(3L, biomes);
        biomes = new LayerZoom(1001, biomes);
        biomes = new LayerShore(1000L, biomes);
        biomes = new LayerZoom(1002, biomes);
        biomes = new LayerZoom(1003, biomes);
        biomes = new LayerSmooth(1000L, biomes);

        //Create Rivers
        Layer rivers = magnify(1000L, continent, 2);
        rivers = new LayerRiverInit(100L, rivers);
        rivers = magnify(1000L, rivers, 6);
        rivers = new LayerRiver(1L, rivers);
        rivers = new LayerSmooth(1000L, rivers);

        return new LayerRiverMix(100L, biomes, rivers).initWorldGenSeed(seed);
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
        parent = new LayerSmooth(1000L, parent).initWorldGenSeed(seed);
        return new LayerVoronoiZoom(10L, parent).initWorldGenSeed(seed);
    }

    public static Layer initEvt(final long seed)
    {
        Layer continent = new LayerEVTInit(1);
        continent = new LayerAddGeneric(1L, continent, Evt.LOW, Evt.HIGH);
//        continent = new LayerAddEVT(1L, continent);
        continent = new LayerFuzzyZoom(2000L, continent);
        continent = new LayerAddGeneric(1L, continent, Evt.LOW, Evt.HIGH);
//        continent = new LayerAddEVT(1L, continent);
        continent = new LayerZoom(2001L, continent);
        continent = new LayerAddGeneric(2L, continent, Evt.LOW, Evt.HIGH);
//        continent = new LayerAddEVT(2L, continent);
        continent = new LayerMixGeneric(88L, continent, Evt.EVT_0_125.id, Evt.EVT_16.id);
//        continent = new LayerEVTMix(88L, continent);
        continent = new LayerZoom(2002L, continent);
        continent = new LayerAddGeneric(3L, continent, Evt.LOW, Evt.HIGH);
//        continent = new LayerAddEVT(3L, continent);
        continent = new LayerZoom(2003L, continent);
        continent = new LayerAddGeneric(4L, continent, Evt.LOW, Evt.HIGH);
//        continent = new LayerAddEVT(4L, continent);
        continent = magnify(1000L, continent, 2);
        continent = new LayerSmooth(1000L, continent);
        continent = new LayerMixGeneric(1000, continent, Evt.EVT_0_125.id, Evt.EVT_16.id);
//        continent = new LayerEVTMix(1000, continent);
        continent = new LayerZoom(1000, continent);
        continent = new LayerZoom(1001, continent);
        continent = new LayerZoom(1002, continent);
        continent = new LayerZoom(1003, continent);
        continent = new LayerSmooth(1000L, continent);
        continent = new LayerVoronoiZoom(10L, continent);
        return continent.initWorldGenSeed(seed);
    }

    public static Layer initRain(final long seed)
    {
        Layer continent = new LayerRainInit(1);
        continent = new LayerAddRain(1L, continent);
        continent = new LayerFuzzyZoom(2000L, continent);
        continent = new LayerZoom(2001L, continent);
        continent = new LayerRainMix(88L, continent);
        continent = new LayerZoom(2002L, continent);
        continent = new LayerRainMix(88L, continent);
        continent = new LayerZoom(2003L, continent);
        continent = magnify(1000L, continent, 2);
        continent = new LayerSmooth(1000L, continent);
        continent = new LayerZoom(1000, continent);
        continent = new LayerRainMix(1001, continent);
        continent = new LayerZoom(1001, continent);
        continent = new LayerZoom(1002, continent);
        continent = new LayerRainMix(1003, continent);
        continent = new LayerZoom(1003, continent);
        continent = new LayerSmooth(1000L, continent);
        continent = new LayerVoronoiZoom(10L, continent);
        return continent.initWorldGenSeed(seed);
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

    public static Layer initPh(final long seed)
    {
        Layer continent = new LayerPHInit(1L);
        continent = new LayerAddGeneric(1L, continent, Ph.MIN, Ph.MAX);
//        continent = new LayerAddPH(1L, continent);
        continent = new LayerFuzzyZoom(2000L, continent);
        continent = new LayerAddGeneric(1L, continent, Ph.MIN, Ph.MAX);
//        continent = new LayerAddPH(1L, continent);
        continent = new LayerZoom(2001L, continent);
        continent = new LayerAddGeneric(2L, continent, Ph.MIN, Ph.MAX);
//        continent = new LayerAddPH(2L, continent);
        continent = new LayerMixGeneric(88L, continent, Ph.MIN, Ph.MAX);
//        continent = new LayerPHMix(88L, continent);
        continent = new LayerZoom(2002L, continent);
        continent = new LayerAddGeneric(3L, continent, Ph.MIN, Ph.MAX);
//        continent = new LayerAddPH(3L, continent);
        continent = new LayerZoom(2003L, continent);
        continent = new LayerAddGeneric(4L, continent, Ph.MIN, Ph.MAX);
//        continent = new LayerAddPH(4L, continent);
        continent = magnify(1000L, continent, 2);
        continent = new LayerSmooth(1000L, continent);
        continent = new LayerMixGeneric(1000, continent, Ph.MIN, Ph.MAX);
//        continent = new LayerPHMix(1000, continent);
        continent = new LayerZoom(1000, continent);
        continent = new LayerZoom(1001, continent);
        continent = new LayerSmooth(1000L, continent);
        return continent.initWorldGenSeed(seed);
    }

    public static Layer initDrain(final long seed)
    {
        Layer continent = new LayerDrainageInit(1L);
        continent = new LayerAddGeneric(1L, continent, Drainage.MIN, Drainage.MAX);
//        continent = new LayerAddDrainage(1L, continent);
        continent = new LayerFuzzyZoom(2000L, continent);
        continent = new LayerAddGeneric(1L, continent, Drainage.MIN, Drainage.MAX);
//        continent = new LayerAddDrainage(1L, continent);
        continent = new LayerZoom(2001L, continent);
        continent = new LayerAddGeneric(2L, continent, Drainage.MIN, Drainage.MAX);
//        continent = new LayerAddDrainage(2L, continent);
        continent = new LayerMixGeneric(88L, continent, Drainage.MIN, Drainage.MAX);
//        continent = new LayerDrainageMix(88L, continent);
        continent = new LayerZoom(2002L, continent);
        continent = new LayerAddGeneric(3L, continent, Drainage.MIN, Drainage.MAX);
//        continent = new LayerAddDrainage(3L, continent);
        continent = new LayerZoom(2003L, continent);
        continent = new LayerAddGeneric(4L, continent, Drainage.MIN, Drainage.MAX);
//        continent = new LayerAddDrainage(4L, continent);
        continent = LayerZoom.magnify(1000L, continent, 2);
        continent = new LayerSmooth(1000L, continent);
        continent = new LayerMixGeneric(1000, continent, Drainage.MIN, Drainage.MAX);
//        continent = new LayerDrainageMix(1000, continent);
        continent = new LayerZoom(1000, continent);
        continent = new LayerZoom(1001, continent);
        continent = new LayerSmooth(1000L, continent);
        return continent.initWorldGenSeed(seed);
    }

    public Layer initWorldGenSeed(final long seed)
    {
        worldGenSeed = seed;
        if (parent != null) parent.initWorldGenSeed(seed);

        worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        worldGenSeed += baseSeed;
        worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        worldGenSeed += baseSeed;
        worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        worldGenSeed += baseSeed;
        return this;
    }

    public void initChunkSeed(final long x, final long y)
    {
        chunkSeed = worldGenSeed;
        chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
        chunkSeed += x;
        chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
        chunkSeed += y;
        chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
        chunkSeed += x;
        chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
        chunkSeed += y;
    }

    protected int nextInt(final int limit)
    {
        int i = (int) ((chunkSeed >> 24) % limit);
        if (i < 0) i += limit;
        chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
        chunkSeed += worldGenSeed;
        return i;
    }

    protected int selectRandom(final int... ints)
    {
        return ints[nextInt(ints.length)];
    }

    public abstract int[] getInts(int x, int y, int sizeX, int sizeY);
}
