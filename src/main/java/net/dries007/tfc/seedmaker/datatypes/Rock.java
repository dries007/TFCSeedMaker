package net.dries007.tfc.seedmaker.datatypes;

import net.dries007.tfc.seedmaker.util.IDataType;
import net.dries007.tfc.seedmaker.util.WorldGen;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Dries007
 */
public enum Rock implements IDataType
{
    GRANITE(0, RockType.IGNEOUS_INTRUSIVE, new Color(0xff8080)),
    DIORITE(1, RockType.IGNEOUS_INTRUSIVE, new Color(0xae80ff)),
    GABBRO(2, RockType.IGNEOUS_INTRUSIVE, new Color(0x80ffdc)),
    SHALE(3, RockType.SEDIMENTARY, new Color(0xf3ff80)),
    CLAYSTONE(4, RockType.SEDIMENTARY, new Color(0xff80c5)),
    ROCKSALT(5, RockType.SEDIMENTARY, new Color(0x8097ff)),
    LIMESTONE(6, RockType.SEDIMENTARY, new Color(0x80ff97)),
    CONGLOMERATE(7, RockType.SEDIMENTARY, new Color(0xffc580)),
    DOLOMITE(8, RockType.SEDIMENTARY, new Color(0xf380ff)),
    CHERT(9, RockType.SEDIMENTARY, new Color(0x80dcff)),
    CHALK(10, RockType.SEDIMENTARY, new Color(0xaeff80)),
    RHYOLITE(11, RockType.IGNEOUS_EXTRUSIVE, new Color(0xff0000)),
    BASALT(12, RockType.IGNEOUS_EXTRUSIVE, new Color(0x5d00ff)),
    ANDESITE(13, RockType.IGNEOUS_EXTRUSIVE, new Color(0x00ffb9)),
    DACITE(14, RockType.IGNEOUS_EXTRUSIVE, new Color(0xe8ff00)),
    QUARTZITE(15, RockType.METAMORPHIC, new Color(0xff008b)),
    SLATE(16, RockType.METAMORPHIC, new Color(0x002eff)),
    PHYLLITE(17, RockType.METAMORPHIC, new Color(0x00ff2e)),
    SCHIST(18, RockType.METAMORPHIC, new Color(0xff8b00)),
    GNEISS(19, RockType.METAMORPHIC, new Color(0xe800ff)),
    MARBLE(20, RockType.METAMORPHIC, new Color(0x00b9ff));

    public static final Rock[] TOP = Arrays.stream(Rock.values()).filter(x -> x.type.l0).toArray(Rock[]::new);
    public static final Rock[] MIDDLE = Arrays.stream(Rock.values()).filter(x -> x.type.l1).toArray(Rock[]::new);
    public static final Rock[] BOTTOM = Arrays.stream(Rock.values()).filter(x -> x.type.l2).toArray(Rock[]::new);
    public static final Rock LIST[] = new Rock[256];

    static
    {
        HashSet<Integer> pool = new HashSet<>();
        for (Rock rock : values())
        {
            if (!pool.add(rock.id)) throw new RuntimeException("Duplicate Biome");
            LIST[rock.id] = rock;
            WorldGen.COLORS[rock.id] = rock.color.getRGB();
        }
    }

    public final int id;
    public final RockType type;
    public final Color color;

    Rock(int id, RockType type, Color color)
    {
        this.id = id;
        this.type = type;
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
