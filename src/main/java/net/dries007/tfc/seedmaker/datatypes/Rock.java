package net.dries007.tfc.seedmaker.datatypes;

import net.dries007.tfc.seedmaker.util.WorldGen;

import java.awt.*;
import java.util.HashSet;

/**
 * @author Dries007
 */
public enum Rock
{
    GRANITE(0, new Color(0xff8080)),
    DIORITE(1, new Color(0xae80ff)),
    GABBRO(2, new Color(0x80ffdc)),
    SHALE(5, new Color(0xf3ff80)),
    CLAYSTONE(6, new Color(0xff80c5)),
    ROCKSALT(7, new Color(0x8097ff)),
    LIMESTONE(8, new Color(0x80ff97)),
    CONGLOMERATE(9, new Color(0xffc580)),
    DOLOMITE(10, new Color(0xf380ff)),
    CHERT(11, new Color(0x80dcff)),
    CHALK(12, new Color(0xaeff80)),
    RHYOLITE(13, new Color(0xff0000)),
    BASALT(14, new Color(0x5d00ff)),
    ANDESITE(15, new Color(0x00ffb9)),
    DACITE(16, new Color(0xe8ff00)),
    QUARTZITE(17, new Color(0xff008b)),
    SLATE(18, new Color(0x002eff)),
    PHYLLITE(19, new Color(0x00ff2e)),
    SCHIST(20, new Color(0xff8b00)),
    GNEISS(21, new Color(0xe800ff)),
    MARBLE(22, new Color(0x00b9ff));

    public static final Rock[] LAYER0 = {SHALE, CLAYSTONE, ROCKSALT, LIMESTONE, CONGLOMERATE, DOLOMITE, CHERT, CHALK, RHYOLITE, BASALT, ANDESITE, DACITE, QUARTZITE, SLATE, PHYLLITE, SCHIST, GNEISS, MARBLE, GRANITE, DIORITE, GABBRO};
    public static final Rock[] LAYER1 = {RHYOLITE, BASALT, ANDESITE, DACITE, QUARTZITE, SLATE, PHYLLITE, SCHIST, GNEISS, MARBLE, GRANITE, DIORITE, GABBRO};
    public static final Rock[] LAYER2 = {RHYOLITE, BASALT, ANDESITE, DACITE, GRANITE, DIORITE, GABBRO};
    public static final Rock[] LIST = new Rock[256];

    static
    {
        HashSet<Integer> pool = new HashSet<>();
        for (Rock rock : values())
        {
            if (!pool.add(rock.id)) throw new RuntimeException("Duplicate Rock");
            LIST[rock.id] = rock;
            WorldGen.COLORSROCK[rock.id] = rock.color.getRGB();
        }
    }

    public final int id;
    public final Color color;

    Rock(int id, Color color)
    {
        this.id = id;
        this.color = color;

        
    }
}
