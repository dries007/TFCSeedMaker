package net.dries007.tfc.seedmaker.datatypes;

public enum RockType
{
    IGNEOUS_INTRUSIVE(true, true, true),
    IGNEOUS_EXTRUSIVE(true, true, true),
    SEDIMENTARY(true, false, false),
    METAMORPHIC(true, true, false);

    public final boolean l0;
    public final boolean l1;
    public final boolean l2;

    RockType(boolean l1, boolean l2, boolean l3)
    {
        this.l0 = l1;
        this.l1 = l2;
        this.l2 = l3;
    }
}
