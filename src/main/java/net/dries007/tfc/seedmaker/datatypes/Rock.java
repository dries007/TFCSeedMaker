package net.dries007.tfc.seedmaker.datatypes;

import java.awt.*;
import java.util.Arrays;

/**
 * @author Dries007
 */
public enum Rock
{
    GRANITE(0, RockType.IGNEOUS_INTRUSIVE, new Color(0xd3d3d3)),
    DIORITE(1, RockType.IGNEOUS_INTRUSIVE, new Color(0x8b0000)),
    GABBRO(2, RockType.IGNEOUS_INTRUSIVE, new Color(0x808000)),
    SHALE(3, RockType.SEDIMENTARY, new Color(0x008000)),
    CLAYSTONE(4, RockType.SEDIMENTARY, new Color(0x008080)),
    ROCKSALT(5, RockType.SEDIMENTARY, new Color(0x7f007f)),
    LIMESTONE(6, RockType.SEDIMENTARY, new Color(0xff4500)),
    CONGLOMERATE(7, RockType.SEDIMENTARY, new Color(0xffa500)),
    DOLOMITE(8, RockType.SEDIMENTARY, new Color(0xffff00)),
    CHERT(9, RockType.SEDIMENTARY, new Color(0x0000cd)),
    CHALK(10, RockType.SEDIMENTARY, new Color(0x7fff00)),
    RHYOLITE(11, RockType.IGNEOUS_EXTRUSIVE, new Color(0x00ff7f)),
    BASALT(12, RockType.IGNEOUS_EXTRUSIVE, new Color(0x4169e1)),
    ANDESITE(13, RockType.IGNEOUS_EXTRUSIVE, new Color(0x00bfff)),
    DACITE(14, RockType.IGNEOUS_EXTRUSIVE, new Color(0xff00ff)),
    QUARTZITE(15, RockType.METAMORPHIC, new Color(0xdb7093)),
    SLATE(16, RockType.METAMORPHIC, new Color(0xf0e68c)),
    PHYLLITE(17, RockType.METAMORPHIC, new Color(0xff1493)),
    SCHIST(18, RockType.METAMORPHIC, new Color(0xffa07a)),
    GNEISS(19, RockType.METAMORPHIC, new Color(0xee82ee)),
    MARBLE(20, RockType.METAMORPHIC, new Color(0x7fffd4));

    public static final Rock[] TOP = Arrays.stream(Rock.values()).filter(x -> x.type.l0).toArray(Rock[]::new);
    public static final Rock[] MIDDLE = Arrays.stream(Rock.values()).filter(x -> x.type.l1).toArray(Rock[]::new);
    public static final Rock[] BOTTOM = Arrays.stream(Rock.values()).filter(x -> x.type.l2).toArray(Rock[]::new);

    public static final int[] COLORS = new int[values().length];

    static
    {
        for (Rock rock : values()) COLORS[rock.id] = rock.color.getRGB();
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
}
