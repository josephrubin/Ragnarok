package box.gift.ragnarok.interact;

import android.support.annotation.RestrictTo;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import box.gift.ragnarok.combat.hitbox.AbstractHitbox;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.Renderable;

/**
 * Created by Joseph on 3/22/2018.
 */

public abstract class InteractTile extends Entity implements Renderable
{
    private List<AbstractHitbox> hitboxes;

    public InteractTile(BoundingBox body)
    {
        super(body);
        hitboxes = new LinkedList<>();
    }

    //public abstract void acted(Character character);

    public abstract void on(Character character);

    public abstract void touch(Character character);

    @Override
    public void update()
    {
        super.update();

        // Update all temporary hitboxes and remove the finished ones.
        Iterator<AbstractHitbox> iterator = getHitboxes().iterator();
        while (iterator.hasNext())
        {
            AbstractHitbox hitbox = iterator.next();
            // Remove finished Hitboxes.
            if (hitbox.isFinished())
            {
                iterator.remove();
            }
            hitbox.update();
        }
    }

    public Collection<AbstractHitbox> getHitboxes()
    {
        return hitboxes;
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void addHitbox(AbstractHitbox hitbox)
    {
        hitboxes.add(hitbox);
    }
}
