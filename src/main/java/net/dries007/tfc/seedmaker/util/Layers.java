package net.dries007.tfc.seedmaker.util;

/**
 * @author Dries007
 */
public enum Layers
{
    COMBINED(false),
    BIOMES(false),
    ROCK_TOP(true),
    ROCK_MIDDLE(true),
    ROCK_BOTTOM(true),
//    TREE_0(false),
//    TREE_1(false),
//    TREE_2(false),
//    EVT(false),
//    RAIN(false),
    STABILITY(false),
    PH(false),
    DRAINAGE(false);

    public final boolean addToCombined;

    Layers(boolean addToCombined)
    {
        this.addToCombined = addToCombined;
    }
}
