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
    }
}
