package box.gift.ragnarok.interact;

import android.content.res.Resources;
import android.graphics.Canvas;

import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;

/**
 * Created by Joseph on 3/23/2018.
 */

public class Goo extends InteractTile
{
    public Goo(BoundingBox body)
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
