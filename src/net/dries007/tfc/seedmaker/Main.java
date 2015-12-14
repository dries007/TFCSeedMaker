package net.dries007.tfc.seedmaker;

import com.beust.jcommander.JCommander;

import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        if (args.length > 0 || GraphicsEnvironment.isHeadless())
        {
            CommandLineInterface cli = new CommandLineInterface();
            new JCommander(cli, args);
            cli.run();
        }
//
//
//
////        if (true) return;
//
//        final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
//
//        long[] seeds = {1}; //, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        for (long seed : seeds) queue.add(new WorldGen(seed));
//
//        for (int i = 0; i < 4; i++)
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    while (!queue.isEmpty())
//                    {
//                        Runnable runnable = queue.poll();
//                        if (runnable != null) runnable.run();
//                    }
//                }
//            }).start();
//        }
    }
}
