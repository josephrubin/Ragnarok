package box.gift.ragnarok.combat.hitbox;

import box.gift.ragnarok.Team;
import box.gift.ragnarok.constant.ForceConstant;
import box.gift.ragnarok.constant.HitboxDuration;
import box.shoe.gameutils.BoundingBox;

public class RangeHitbox extends Hitbox
{
    public RangeHitbox(BoundingBox body, Team sourceTeam, int damage)
    {
        super(body, sourceTeam, damage);

        setUpdatesLeft(HitboxDuration.RANGE_DEFAULT);
        setTargetsLeft(1);
        setKnockback(ForceConstant.DAMAGE_KNOCKBACK_DEFAULT);
    }
}
