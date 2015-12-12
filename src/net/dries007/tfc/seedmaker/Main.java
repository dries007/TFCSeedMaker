package net.dries007.tfc.seedmaker;

import net.dries007.tfc.seedmaker.util.WorldGen;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Main
{
    public static void main(String[] args)
    {
        final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        long[] seeds = {1}; //, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (long seed : seeds) queue.add(new WorldGen(seed));

        for (int i = 0; i < 4; i++)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    while (!queue.isEmpty())
                    {
                        Runnable runnable = queue.poll();
                        if (runnable != null) runnable.run();
                    }
                }
            }).start();
        }
    }
}
