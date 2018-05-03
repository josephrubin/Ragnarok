package box.gift.ragnarok.interact;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;

import box.gift.ragnarok.Afflictable;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.AABB;

/**
 * Created by Joseph on 3/23/2018.
 */

public class Poison extends InteractTile
{
    public Poison(AABB body)
    {
        super(body);
    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {

    }

    @Override
    public void on(Character character)
    {
        character.afflict(Afflictable.POISON);
    }

    @Override
    public void touch(Character character)
    {

    }
}