package box.gift.ragnarok.combat.weapon;

import android.content.res.Resources;
import android.graphics.Canvas;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import box.gift.ragnarok.hitbox.Hitbox;
import box.shoe.gameutils.Renderable;
import box.shoe.gameutils.Updatable;

public class Attack implements Updatable, Renderable
{
    private Collection<Hitbox> hitboxes;
    private boolean isFinished;

    public Attack()
    {
        hitboxes = new LinkedList<>();
    }

    public void addHitbox(Hitbox hitbox)
    {
        hitboxes.add(hitbox);
    }

    public void removeHitbox(Hitbox hitbox)
    {
        hitboxes.remove(hitbox);
    }

    public void removeAllHitboxes()
    {
        hitboxes.clear();
    }

    public Collection<Hitbox> getHitboxes()
    {
        return hitboxes;
    }

    public boolean isFinished()
    {
        return isFinished;
    }

    /* pack */ void end()
    {
        isFinished = true;
    }

    @Override
    public void update()
    {
        Iterator<Hitbox> iterator = getHitboxes().iterator();
        while (iterator.hasNext())
        {
            Hitbox hitbox = iterator.next();
            // Remove finished Hitboxes.
            if (hitbox.isFinished())
            {
                iterator.remove();
            }
            hitbox.update();
        }
    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {
        for (Hitbox hitbox : hitboxes)
        {
            if (hitbox != null)
            {
                hitbox.render(resources, canvas);
            }
        }
    }
}
