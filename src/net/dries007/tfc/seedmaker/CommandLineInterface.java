package net.dries007.tfc.seedmaker;

import com.beust.jcommander.Parameter;
import com.google.gson.JsonArray;
import net.dries007.tfc.seedmaker.util.WorldGen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Dries007
 */
public class CommandLineInterface implements Runnable
{
    @Parameter(names = {"-taw", "--trees-above-water"}, description = "Count trees in water biomes")
    private boolean treesAboveWater = false;

    @Parameter(names = {"-riw", "--rocks-in-water"}, description = "Count the rock layers in water biomes")
    private boolean rocksInWater = false;

    @Parameter(names = {"-t", "--threads"}, description = "Amount of threads used, by defauld amound of CPU cores available")
    private int threads = Runtime.getRuntime().availableProcessors();

    @Parameter(names = {"-r", "--radius"}, description = "Radius in blocks")
    private int radius = 1024 * 5;

    @Parameter(names = {"-c", "--chunksize"}, description = "Size per 'chunk'")
    private int chunkSize = 128;

    @Parameter(names = {"-s", "--seed", "--seeds"}, description = "The seeds to use, if none provided one random seed is chosen per thread")
    private List<String> seeds = new ArrayList<>();

    @Parameter(names = {"-n", "-count", "-target"}, description = "The amount of seeds to try and find")
    private int targetCount = 10;

    @Parameter(names = {"-m", "--map"}, description = "Save the map to <seed>.png")
    private boolean map = false;

    @Override
    public void run()
    {
        System.out.println("Config: ");
        System.out.println("- treesAboveWater: " + treesAboveWater);
        System.out.println("- rocksInWater: " + rocksInWater);
        System.out.println("- threads: " + threads);
        System.out.println("- radius: " + radius);
        System.out.println("- chunkSize: " + chunkSize);
        System.out.println("- seeds: " + seeds);
        System.out.println("- targetCount: " + targetCount);
        System.out.println("- map: " + map);

        final JsonArray rootArray = new JsonArray();
        Thread[] threadAray = new Thread[threads];
        if (!seeds.isEmpty())
        {
            final ConcurrentLinkedQueue<WorldGen> queue = new ConcurrentLinkedQueue<>();
            for (String seed : seeds) queue.add(new WorldGen(seed, treesAboveWater, rocksInWater, radius, chunkSize, map));

            for (int i = 0; i < threads; i++)
            {
                threadAray[i] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        while (!queue.isEmpty())
                        {
                            WorldGen worldGen = queue.poll();
                            if (worldGen == null) continue;

                            worldGen.run();

                            rootArray.add(worldGen.toJson());
                        }
                    }
                });
            }
        }
        else
        {
            final AtomicInteger goodCount = new AtomicInteger();
            for (int i = 0; i < threads; i++)
            {
                threadAray[i] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        while (goodCount.get() < targetCount)
                        {
                            WorldGen worldGen = new WorldGen(null, treesAboveWater, rocksInWater, radius, chunkSize, map);
                            worldGen.run();

//                            worldGen.evaluate();
//                            if (Helper.evaluate(worldGen))
                            goodCount.decrementAndGet();
                            rootArray.add(worldGen.toJson());
                        }
                    }
                });
            }
        }

        for (int i = 0; i < threads; i++) threadAray[i].start();

        while (true)
        {
            boolean done = true;
            for (int i = 0; i < threads; i++) if (threadAray[i].isAlive()) done = false;
            if (done) break;
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Output: ");
        System.out.println(rootArray);
    }
}
