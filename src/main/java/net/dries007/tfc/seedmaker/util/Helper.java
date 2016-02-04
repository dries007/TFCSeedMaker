package net.dries007.tfc.seedmaker.util;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.PngWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Map;
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

    public static <K, V> JsonElement toJson(Map<K, V> biomeMap)
    {
        JsonObject object = new JsonObject();
        for (Map.Entry<K, V> e : biomeMap.entrySet()) object.addProperty(e.getKey().toString(), e.getValue().toString());
        return object;
    }

    public static PngWriter[] prepareGraphics(String[] filenames, int size, File folder, boolean[] maps)
    {
        PngWriter[] out = new PngWriter[filenames.length];
        for (int i = 0; i < out.length; i++) if (maps[i]) out[i] = new PngWriter(new File(folder, filenames[i] + ".png"), new ImageInfo(size, size, 8, false), true);
        return out;
    }
}
