package net.dries007.tfc.seedmaker;

import net.dries007.tfc.seedmaker.util.WorldGen;

/**
 * @author Dries007
 */
public class Test
{
    public static void main(String[] args)
    {
        System.out.println("Free: " + Runtime.getRuntime().freeMemory() / 1048576 + " Max: " + Runtime.getRuntime().maxMemory() / 1048576 + " Total: " + Runtime.getRuntime().totalMemory() / 1048576);

        int chuncks = 512;
        WorldGen worldGen = new WorldGen("1", false, false, chuncks * 10, chuncks, false);
        for (int i = 0; i < 10 * 10; i++)
        {
            worldGen.biomeIndexLayer.getInts(0, 0, chuncks, chuncks);
            worldGen.rockLayer0.getInts(0, 0, chuncks, chuncks);
            worldGen.treesLayer0.getInts(0, 0, chuncks, chuncks);
            worldGen.evtIndexLayer.getInts(0, 0, chuncks, chuncks);
            worldGen.rainfallLayer.getInts(0, 0, chuncks, chuncks);
            worldGen.stabilityLayer.getInts(0, 0, chuncks, chuncks);
            worldGen.phIndexLayer.getInts(0, 0, chuncks, chuncks);
            worldGen.drainageLayer.getInts(0, 0, chuncks, chuncks);
        }

        System.out.println("Free: " + Runtime.getRuntime().freeMemory() / 1048576 + " Max: " + Runtime.getRuntime().maxMemory() / 1048576 + " Total: " + Runtime.getRuntime().totalMemory() / 1048576);
    }
}
