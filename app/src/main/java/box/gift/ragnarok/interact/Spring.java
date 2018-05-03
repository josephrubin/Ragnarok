package box.gift.ragnarok.interact;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;

import box.gift.ragnarok.constant.ForceConstant;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.AABB;
import box.shoe.gameutils.Vector;

/**
 * Created by Joseph on 3/22/2018.
 */

public class Spring extends InteractTile
{
    public static final float BOUNCE_AMOUNT = ForceConstant.SPRING;

    public Spring(AABB body)
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
        character.applyForce(force.scale(BOUNCE_AMOUNT));
    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {

    }
}
