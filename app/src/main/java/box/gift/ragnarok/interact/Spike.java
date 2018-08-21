package box.gift.ragnarok.interact;

import android.content.res.Resources;
import android.graphics.Canvas;

import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.Team;
import box.gift.ragnarok.constant.ForceConstant;
import box.gift.ragnarok.constant.StunDuration;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Vector;

/**
 * Created by Joseph on 3/23/2018.
 */

public class Spike extends InteractTile
{
    private static final int DAMAGE_AMOUNT = 1;

    public Spike(BoundingBox body)
    {
        super(body);
    }

    @Override
    public void on(Character character)
    {

    }

    @Override
    public void touch(Character character)
    {
        if (character.requestDamage(DAMAGE_AMOUNT, Team.UNALIGNED))
        {
            Vector force = Vector.ZERO;
            if (body.leftTouchesRightOf(character.body))
            {
                force = Vector.WEST;
            }
            else if (body.rightTouchesLeftOf(character.body))
            {
                force = Vector.EAST;
            }
            else if (body.topTouchesBottomOf(character.body))
            {
                force = Vector.NORTH;
            }
            else if (body.bottomTouchesTopOf(character.body))
            {
                force = Vector.SOUTH;
            }
            character.applyForce(force.scale(ForceConstant.DAMAGE_KNOCKBACK_DEFAULT));
            character.addStatusEffectForDuration(StatusEffect.STUN, StunDuration.DAMAGE_TAKEN_DEFAULT);
        }
    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {

    }
}
