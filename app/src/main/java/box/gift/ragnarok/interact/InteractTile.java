package box.gift.ragnarok.interact;

import android.support.annotation.RestrictTo;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import box.gift.ragnarok.combat.attack.Attack;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.CollectionUtils;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.Renderable;

/**
 * Created by Joseph on 3/22/2018.
 */

public abstract class InteractTile extends Entity implements Renderable
{
    private List<Attack> attacks;

    public InteractTile(BoundingBox body)
    {
        super(body);
        attacks = new LinkedList<>();
    }

    //public abstract void acted(Character character);

    public abstract void on(Character character);

    public abstract void touch(Character character);

    @Override
    public void update()
    {
        super.update();

        Iterator<Attack> iterator = getAttacks().iterator();
        while (iterator.hasNext())
        {
            Attack attack = iterator.next();
            // Remove finished Attacks.
            if (attack.isFinished())
            {
                iterator.remove();
            }
            attack.update();
        }
    }

    public Collection<Attack> getAttacks()
    {
        return attacks;
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void addAttack(Attack attack)
    {
        attacks.add(attack);
    }
}
