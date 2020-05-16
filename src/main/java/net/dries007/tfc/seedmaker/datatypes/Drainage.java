package net.dries007.tfc.seedmaker.datatypes;

/**
 * @author Dries007
 */
public enum Drainage
{
    DRAINAGE_NONE(0),
    DRAINAGE_VERY_POOR(1),
    DRAINAGE_POOR(2),
    DRAINAGE_NORMAL(3),
    DRAINAGE_GOOD(4),
    DRAINAGE_VERY_GOOD(5);

    public static final int MIN = DRAINAGE_NONE.id;
    public static final int MAX = DRAINAGE_VERY_GOOD.id;

    public final int id;

    Drainage(final int id)
    {
        this.id = id;
    }
}
