package box.gift.ragnarok.interact;

import android.content.res.Resources;
import android.graphics.Canvas;

import box.gift.ragnarok.constant.ForceConstant;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.AABB;
import box.shoe.gameutils.Direction;

public class Conveyor extends InteractTile
{
    private Direction direction;

    public Conveyor(AABB body, Direction direction)
    {
        super(body);
        this.direction = direction;
    }

    @Override
    public void on(Character character)
    {
        character.applyUniqueForce(direction.VECTOR.scale(ForceConstant.CONVEYOR), direction);
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
