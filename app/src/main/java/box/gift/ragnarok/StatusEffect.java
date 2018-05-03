package box.gift.ragnarok;

import android.support.annotation.NonNull;

// A status effect is an application which affects a Character's behavior in some forced way.
// These are generally applied for a certain period of time.
public class StatusEffect
{
    public static final StatusEffect STUN = new StatusEffect("stun");
    public static final StatusEffect SLOWED = new StatusEffect("slowed");
    public static final StatusEffect SLIPPERY = new StatusEffect("slippery");

    public static final float SLOWED_FACTOR = 0.53f;

    private final String NAME;

    private StatusEffect(@NonNull String name)
    {
        NAME = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        StatusEffect that = (StatusEffect) o;

        return NAME.equals(that.NAME);
    }

    @Override
    public int hashCode()
    {
        return NAME.hashCode();
    }

    @Override
    public String toString()
    {
        return "StatusEffect{" +
                "NAME='" + NAME + '\'' +
                '}';
    }
}