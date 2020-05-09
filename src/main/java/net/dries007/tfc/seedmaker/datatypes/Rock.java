package net.dries007.tfc.seedmaker.datatypes;

import net.dries007.tfc.seedmaker.util.IDataType;
import net.dries007.tfc.seedmaker.util.WorldGen;

import java.awt.*;
import java.nio.file.AccessDeniedException;
import java.util.HashSet;

/**
 * @author Dries007
 */
public enum Rock implements IDataType
{
    GRANITE(0, new Color(0xff8080)),
    DIORITE(1, new Color(0xae80ff)),
    GABBRO(2, new Color(0x80ffdc)),
    SHALE(3, new Color(0xf3ff80)),
    CLAYSTONE(4, new Color(0xff80c5)),
    ROCKSALT(5, new Color(0x8097ff)),
    LIMESTONE(6, new Color(0x80ff97)),
    CONGLOMERATE(7, new Color(0xffc580)),
    DOLOMITE(8, new Color(0xf380ff)),
    CHERT(9, new Color(0x80dcff)),
    CHALK(10, new Color(0xaeff80)),
    RHYOLITE(11, new Color(0xff0000)),
    BASALT(12, new Color(0x5d00ff)),
    ANDESITE(13, new Color(0x00ffb9)),
    DACITE(14, new Color(0xe8ff00)),
    QUARTZITE(15, new Color(0xff008b)),
    SLATE(16, new Color(0x002eff)),
    PHYLLITE(17, new Color(0x00ff2e)),
    SCHIST(18, new Color(0xff8b00)),
    GNEISS(19, new Color(0xe800ff)),
    MARBLE(20, new Color(0x00b9ff));

    public static final Rock[] LAYER0 = {GRANITE,DIORITE,GABBRO,SHALE,CLAYSTONE,ROCKSALT,LIMESTONE,CONGLOMERATE,DOLOMITE,CHERT,CHALK,RHYOLITE,BASALT,ANDESITE,DACITE,QUARTZITE,SLATE,PHYLLITE,SCHIST,GNEISS,MARBLE};
    public static final Rock[] LAYER1 = {GRANITE,DIORITE,GABBRO,RHYOLITE,BASALT,ANDESITE,DACITE,QUARTZITE,SLATE,PHYLLITE,SCHIST,GNEISS,MARBLE};
    public static final Rock[] LAYER2 = {GRANITE,DIORITE,GABBRO,RHYOLITE,BASALT, ANDESITE,DACITE};
    public static final Rock[] LIST = new Rock[256];

    static
    {
        HashSet<Integer> pool = new HashSet<>();
        for (Rock rock : values())
        {
            if (!pool.add(rock.id)) throw new RuntimeException("Duplicate Rock");
            LIST[rock.id] = rock;
            WorldGen.COLORS[rock.id] = rock.color.getRGB();
        }
    }

    public final int id;
    public final Color color;

    Rock(int id, Color color)
    {
        this.id = id;
        this.color = color;
    }

    @Override
    public int getId()
    {
        return this.id;
    }

    @Override
    public Color getColor()
    {
        return this.color;
    }
}
