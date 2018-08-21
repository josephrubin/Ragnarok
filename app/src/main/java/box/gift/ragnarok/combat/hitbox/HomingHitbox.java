package box.gift.ragnarok.combat.hitbox;

import box.gift.ragnarok.Team;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Direction;
import box.shoe.gameutils.Vector;

/**
 * @deprecated not yet working
 */
public class HomingHitbox// extends RangeHitbox
{/*
    private Character target;
    private float speed;

    public HomingHitbox(BoundingBox body, Team sourceTeam, int damage, int durationUpdates)
    {
        super(body, sourceTeam, damage, durationUpdates);
    }

    public HomingHitbox(BoundingBox body, Team sourceTeam, int damage, int durationUpdates, int targetCount)
    {
        super(body, sourceTeam, damage, durationUpdates, targetCount);
    }

    public void home(Character target, float speed, Direction startingDirection)
    {
        this.target = target;
        this.speed = speed;
        velocity = startingDirection.VECTOR.scale(speed);
    }

    @Override
    public void update()
    {
        if (target != null)
        {
            Vector toTarget = vectorTo(target);
            velocity = velocity.add(toTarget.unit().scale(0.25f)).unit().scale(speed);
        }
        super.update();
    }*/
}
