package net.dries007.tfc.seedmaker;

import com.beust.jcommander.JCommander;

public class Main
{
    public static void main(String[] args)
    {
        CommandLineInterface cli = new CommandLineInterface();
        JCommander jc = new JCommander(cli, args);
        if (cli.help || args.length == 0) jc.usage();
        else cli.run();
    }
}
