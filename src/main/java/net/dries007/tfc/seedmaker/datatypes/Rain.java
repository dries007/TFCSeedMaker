package net.dries007.tfc.seedmaker.datatypes;

import net.dries007.tfc.seedmaker.util.IDataType;
import net.dries007.tfc.seedmaker.util.WorldGen;

import java.awt.*;

/**
 * @author Dries007
 */
public enum Rain implements IDataType
{
    RAIN_62_5(90, 62.5f),
    RAIN_125(91, 125f),
    RAIN_250(92, 250f),
    RAIN_500(93, 500f),
    RAIN_1000(94, 1000f),
    RAIN_2000(95, 2000f),
    RAIN_4000(96, 4000f),
    RAIN_8000(97, 8000f);

    public static final int WET = RAIN_4000.id;
    public static final int DRY = RAIN_125.id;

    static
    {
        final int mul = 255 / values().length;
        for (Rain x : values())
        {
            final int id = x.ordinal() * mul;
            WorldGen.COLORS[x.id] = (id << 16) + (id << 8) + id;
        }
    }

    public final int id;
    public final float value;

    Rain(final int id, final float value)
    {
        this.id = id;
        this.value = value;
    }

    @Override
    public int getId()
    {
        return this.id;
    }

    @Override
    public Color getColor()
    {
        return new Color(WorldGen.COLORS[this.id]);
    }
}
