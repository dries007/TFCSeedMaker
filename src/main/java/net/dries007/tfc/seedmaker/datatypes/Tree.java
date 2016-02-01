package net.dries007.tfc.seedmaker.datatypes;

import net.dries007.tfc.seedmaker.util.WorldGen;

import java.awt.*;
import java.util.HashSet;

/**
 * @author Dries007
 */
public enum Tree
{
    NO_TREE(29, new Color(0xffffff)),
    ASH(30, new Color(0xff0000)),
    ASPEN(31, new Color(0xffaa00)),
    BIRCH(32, new Color(0xaaff00)),
    CHESTNUT(33, new Color(0x00ff00)),
    DOUGLASFIR(34, new Color(0x00ffaa)),
    HICKORY(35, new Color(0x00aaff)),
    //    KOA(45),
    MAPLE(36, new Color(0x0000ff)),
    OAK(37, new Color(0xaa00ff)),
    PINE(38, new Color(0xff00aa)),
    REDWOOD(39, new Color(0xff8080)),
    SPRUCE(40, new Color(0xffd580)),
    SYCAMORE(41, new Color(0xd5ff80)),
    //    SAVANNAHACACIA(46),
    WHITECEDAR(42, new Color(0x80ff80)),
    WHITEELM(43, new Color(0x80ffd5)),
    WILLOW(44, new Color(0x80d4ff));

    public static final Tree[] TREE_ARRAY = {ASH, ASPEN, BIRCH, CHESTNUT, DOUGLASFIR, HICKORY, MAPLE, OAK, PINE, REDWOOD, PINE, SPRUCE, SYCAMORE, WHITECEDAR, WHITEELM, WILLOW, NO_TREE};
    public static final Tree LIST[] = new Tree[256];

    static
    {
        HashSet<Integer> pool = new HashSet<>();
        for (Tree tree : values())
        {
            if (!pool.add(tree.id)) throw new RuntimeException("Duplicate Tree");
            LIST[tree.id] = tree;
            WorldGen.COLORS[tree.id] = tree.color.getRGB();
        }
    }

    public final int id;
    public final Color color;

    Tree(final int id, final Color color)
    {
        this.id = id;
        this.color = color;
    }
}
