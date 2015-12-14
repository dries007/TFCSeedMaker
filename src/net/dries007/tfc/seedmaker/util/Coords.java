package net.dries007.tfc.seedmaker.util;

import com.google.gson.JsonObject;

/**
 * @author Dries007
 */
public class Coords
{
    public int x, y;

    public Coords(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coords coords = (Coords) o;

        return x == coords.x && y == coords.y;

    }

    @Override
    public String toString()
    {
        return "[" + x + ';' + y + ']';
    }

    public JsonObject toJson()
    {
        JsonObject jsonObject =new JsonObject();
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        return jsonObject;
    }
}
