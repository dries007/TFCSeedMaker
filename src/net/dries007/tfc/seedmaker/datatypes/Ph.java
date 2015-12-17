package net.dries007.tfc.seedmaker.datatypes;

/**
 * @author Dries007
 */
public enum Ph
{
    PH_ACID_HIGH(130, 0),
    PH_ACID_LOW(131, 1),
    PH_NEUTRAL(132, 2),
    PH_ALKALINE_LOW(133, 3),
    PH_ALKALINE_HIGH(134, 4);

    public static final int MIN = PH_ACID_HIGH.id;
    public static final int MAX = PH_ALKALINE_HIGH.id;

    public final int id;
    public final int value;

    Ph(final int id, final int value)
    {
        this.id = id;
        this.value = value;
    }
}
