package net.dries007.tfc.seedmaker.util;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.PngWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Dries007
 */
public class Helper
{
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public static long parseSeed(String seedString)
    {
        if (seedString == null || seedString.length() == 0) return ThreadLocalRandom.current().nextLong();
        try
        {
            long j = Long.parseLong(seedString);
            if (j == 0L) throw new IllegalArgumentException("Zero (0) isn't a valid seed.");
            return j;
        }
        catch (NumberFormatException numberformatexception)
        {
            return seedString.hashCode();
        }
    }

    public static <T extends Comparable<? super T>> JsonElement toSortedJson(Set<T> set)
    {
        JsonArray array = new JsonArray();
        List<T> list = new ArrayList<>();
        list.addAll(set);
        Collections.sort(list);
        for (T e : list) array.add(e.toString());
        return array;
    }

    public static PngWriter[] prepareGraphics(String[] filenames, int size, File folder)
    {
        PngWriter[] out = new PngWriter[filenames.length];
        for (int i = 0; i < out.length; i++) out[i] = new PngWriter(new File(folder, filenames[i] + ".png"), new ImageInfo(size, size, 8, false), true);
        return out;
    }

    public static void finishGraphics(PngWriter[] writers) throws IOException
    {
        for (PngWriter writer : writers) writer.close();
    }
}
