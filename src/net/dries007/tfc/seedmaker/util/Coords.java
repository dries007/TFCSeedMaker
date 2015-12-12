package net.dries007.tfc.seedmaker.util;

/**
 * @author Dries007
 */
public class Coords
{
    public int x, z;

    public Coords(int x, int z)
    {
        this.x = x;
        this.z = z;
    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + z;
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coords coords = (Coords) o;

        return x == coords.x && z == coords.z;

    }

    @Override
    public String toString()
    {
        return "[" + x + ';' + z + ']';
    }
}
