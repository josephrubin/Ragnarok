package box.gift.ragnarok.interact;

import android.graphics.RectF;

import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.AABB;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.Renderable;

/**
 * Created by Joseph on 3/22/2018.
 */

public abstract class InteractTile extends Entity implements Renderable
{
    public InteractTile(AABB body)
    {
        super(body);
    }

    public abstract void on(Character character);

    public abstract void touch(Character character);
}
