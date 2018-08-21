package box.gift.ragnarok.interact;

import android.content.res.Resources;
import android.graphics.Canvas;

import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;

public class Ice extends InteractTile
{
    public Ice(BoundingBox body)
    {
        super(body);
    }

    @Override
    public void on(Character character)
    {
        character.addStatusEffectForDuration(StatusEffect.SLIPPERY, 1);
    }

    @Override
    public void touch(Character character)
    {

    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {

    }
}
