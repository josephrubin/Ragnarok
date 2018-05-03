package box.gift.ragnarok.interact;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;

import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.AABB;

/**
 * Created by Joseph on 3/23/2018.
 */

public class Goo extends InteractTile
{
    public Goo(AABB body)
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
        character.addStatusEffectForDuration(StatusEffect.SLOWED, 1);
    }

    @Override
    public void touch(Character character)
    {

    }
}
