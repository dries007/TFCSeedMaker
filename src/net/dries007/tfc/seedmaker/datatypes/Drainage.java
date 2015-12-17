package net.dries007.tfc.seedmaker.datatypes;

/**
 * @author Dries007
 */
public enum Drainage
{
    DRAINAGE_NONE(120, 0),
    DRAINAGE_VERY_POOR(121, 1),
    DRAINAGE_POOR(122, 2),
    DRAINAGE_NORMAL(123, 3),
    DRAINAGE_GOOD(124, 4),
    DRAINAGE_VERY_GOOD(125, 5);

    public static final int MIN = DRAINAGE_NONE.id;
    public static final int MAX = DRAINAGE_VERY_GOOD.id;

    public final int id;
    public final int value;

    Drainage(final int id, final int value)
    {
        this.id = id;
        this.value = value;
    }
}
