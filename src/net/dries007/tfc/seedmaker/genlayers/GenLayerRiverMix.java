package net.dries007.tfc.seedmaker.genlayers;

import net.dries007.tfc.seedmaker.util.Biome;

import static net.dries007.tfc.seedmaker.util.Biome.*;

public class GenLayerRiverMix extends GenLayer
{
    private final GenLayer biomePatternGeneratorChain;
    private final GenLayer riverPatternGeneratorChain;

    public GenLayerRiverMix(long seed, GenLayer biomePattern, GenLayer riverPattern)
    {
        super(seed);
        biomePatternGeneratorChain = biomePattern;
        riverPatternGeneratorChain = riverPattern;
    }

    public boolean inBounds(int index, int[] array)
    {
        return index < array.length && index >= 0;
    }

    @Override
    public GenLayer initWorldGenSeed(long seed)
    {
        biomePatternGeneratorChain.initWorldGenSeed(seed);
        riverPatternGeneratorChain.initWorldGenSeed(seed);
        return super.initWorldGenSeed(seed);
    }

    @Override
    public int[] getInts(int x, int y, int sizeX, int sizeY)
    {
        final int[] layerBiomes = biomePatternGeneratorChain.getInts(x, y, sizeX, sizeY);
        final int[] layerRivers = riverPatternGeneratorChain.getInts(x, y, sizeX, sizeY);
        final int[] layerOut = new int[sizeX * sizeY];

        for (int yy = 0; yy < sizeY; ++yy)
        {
            for (int xx = 0; xx < sizeX; ++xx)
            {
                int index = xx + yy * sizeX;
                int b = layerBiomes[index];
                int r = layerRivers[index];

                final int xn = index - 1;
                final int xp = index + 1;
                final int yn = index - sizeY;
                final int yp = index + sizeY;

                if (Biome.isOceanicBiome(b) || Biome.isMountainBiome(b)) layerOut[index] = b;
                else if (r > 0)
                {
                    layerOut[index] = r;
                    if (Biome.isBeachBiome(b))
                    {
                        layerOut[index] = Biome.OCEAN.id;
                        if (inBounds(xn, layerOut) && layerOut[xn] == RIVER.id) layerOut[xn] = Biome.OCEAN.id;
                        if (inBounds(yn, layerOut) && layerOut[yn] == RIVER.id) layerOut[yn] = Biome.OCEAN.id;
                        if (inBounds(yp, layerOut) && Biome.isOceanicBiome(layerBiomes[yp]) && layerRivers[yp] == 0) layerOut[index] = b;
                        if (inBounds(yn, layerOut) && Biome.isOceanicBiome(layerBiomes[yn]) && layerRivers[yn] == 0) layerOut[index] = b;
                        if (inBounds(xn, layerOut) && Biome.isOceanicBiome(layerBiomes[xn]) && layerRivers[xn] == 0) layerOut[index] = b;
                        if (inBounds(xp, layerOut) && Biome.isOceanicBiome(layerBiomes[xp]) && layerRivers[xp] == 0) layerOut[index] = b;
                    }
                }
                else layerOut[index] = b;

                if (layerOut[index] == RIVER.id)
                {
                    if (xn >= 0 && layerBiomes[xn] == LAKE.id) layerOut[index] = LAKE.id;
                    if (yn >= 0 && layerBiomes[yn] == LAKE.id) layerOut[index] = LAKE.id;
                    if (xp < layerBiomes.length && layerBiomes[xp] == LAKE.id) layerOut[index] = LAKE.id;
                    if (yp < layerBiomes.length && layerBiomes[yp] == LAKE.id) layerOut[index] = LAKE.id;

                    if (xn >= 0 && layerBiomes[xn] == MOUNTAINS_EDGE.id) layerOut[index] = MOUNTAINS_EDGE.id;
                    if (yn >= 0 && layerBiomes[yn] == MOUNTAINS_EDGE.id) layerOut[index] = MOUNTAINS_EDGE.id;
                    if (xp < layerBiomes.length && layerBiomes[xp] == MOUNTAINS_EDGE.id) layerOut[index] = MOUNTAINS_EDGE.id;
                    if (yp < layerBiomes.length && layerBiomes[yp] == MOUNTAINS_EDGE.id) layerOut[index] = MOUNTAINS_EDGE.id;
                }
            }
        }
        return layerOut.clone();
    }
}
