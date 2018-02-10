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
    @Parameter(names = {"-T", "--trees-above-water"}, description = "Count trees in water biomes")
    public boolean treesAboveWater = false;

    @Parameter(names = {"-R", "--rocks-in-water"}, description = "Count the rock layers in water biomes")
    public boolean rocksInWater = false;

    @Parameter(names = {"-t", "--threads"}, description = "Amount of threads used, by default amount of CPU cores available")
    public int threads = Runtime.getRuntime().availableProcessors();

    @Parameter(names = {"-r", "--radius"}, description = "Radius in blocks")
    public int radius = 1024 * 5;

    @Parameter(names = {"-c", "--chunksize"}, description = "Size per 'chunk'")
    public int chunkSize = 128;

    @Parameter(names = {"-s", "--seed", "--seeds"}, description = "The seeds to use, if none provided one random seed is chosen per thread. Comma separated list of strings")
    public List<String> seeds = new ArrayList<>();

    @Parameter(names = {"-n", "-count", "-target"}, description = "The amount of seeds to try and find. Only used when no seeds are given. If -1, the program will run forever")
    public int targetCount = 10;

    @Parameter(names = {"-m", "--map", "--maps"}, description = "Possible maps: Combined, Rock_Top, Rock_Middle, Rock_Bottom, Tree_0, Tree_1, Tree_2, EVT, Rain, Stability, PH, Drainage. You can also specify Rocks or Trees or All")
    public List<String> maps = new ArrayList<>();

    @Parameter(names = {"-?", "--help"}, help = true, description = "Display command line interface parameters")
    public boolean help;

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
        System.out.println("- maps: " + maps);

        final JsonArray rootArray = new JsonArray();
        final Thread[] threadArray = new Thread[threads];

        if (!seeds.isEmpty()) // We got seeds via CLI
        {
            // Queue up all the seeds
            final ConcurrentLinkedQueue<WorldGen> queue = new ConcurrentLinkedQueue<>();
            for (String seed : seeds) queue.add(new WorldGen(seed, treesAboveWater, rocksInWater, radius, chunkSize, maps));

            // Make a bunch of worker threads
            for (int i = 0; i < threads; i++)
            {
                threadArray[i] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        while (!queue.isEmpty())
                        {
                            WorldGen worldGen = queue.poll();
                            if (worldGen == null) continue; // Just in case

                            worldGen.run();

                            rootArray.add(worldGen.toJson());
                        }
                    }
                });
            }
        }
        else // We didn't get seeds via CLI, make some at random
        {
            // Don't start more threads than seeds we've asked for.
            threads = Math.min(threads, targetCount);

            // todo: evaluate so we actually only count good seeds
            final AtomicInteger goodCount = new AtomicInteger();

            // Make a bunch of worker threads
            for (int i = 0; i < threads; i++)
            {
                threadArray[i] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        while (targetCount < 0 || goodCount.get() < targetCount)
                        {
                            WorldGen worldGen = new WorldGen(null, treesAboveWater, rocksInWater, radius, chunkSize, maps);
                            worldGen.run();

                            goodCount.incrementAndGet();
                            rootArray.add(worldGen.toJson());
                        }
                    }
                });
            }
        }

        // Now actually start the threads
        for (int i = 0; i < threads; i++) threadArray[i].start();

        // Output routine, quick and dirty.
        while (true)
        {
            boolean done = true;
            for (int i = 0; i < threads; i++) if (threadArray[i].isAlive()) done = false;
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
