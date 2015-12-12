package net.dries007.tfc.seedmaker.genlayers;

public abstract class GenLayer
{
    protected long worldGenSeed;
    protected GenLayer parent;
    protected long chunkSeed;
    protected long baseSeed;

    public GenLayer(final long seed)
    {
        baseSeed = seed;
        baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
        baseSeed += seed;
        baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
        baseSeed += seed;
        baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
        baseSeed += seed;
    }

    public GenLayer(final long seed, final GenLayer parent)
    {
        this(seed);
        this.parent = parent;
    }

    public GenLayer initWorldGenSeed(final long seed)
    {
        worldGenSeed = seed;
        if (parent != null)
            parent.initWorldGenSeed(seed);

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
        int var2 = (int) ((chunkSeed >> 24) % limit);
        if (var2 < 0)
            var2 += limit;
        chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
        chunkSeed += worldGenSeed;
        return var2;
    }

    protected int selectRandom(final int... ints)
    {
        return ints[nextInt(ints.length)];
    }

    public abstract int[] getInts(final int x, final int y, final int sizeX, final int sizeY);
}
