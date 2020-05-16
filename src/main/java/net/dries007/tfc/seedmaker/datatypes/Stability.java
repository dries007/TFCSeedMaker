package net.dries007.tfc.seedmaker.datatypes;

/**
 * @author Dries007
 */
public enum Stability
{
    SEISMIC_STABLE(0, false),
    SEISMIC_UNSTABLE(1, true);

    public final int id;
    public final boolean value;

    Stability(final int id, final boolean value)
    {
        this.id = id;
        this.value = value;
    }
}
