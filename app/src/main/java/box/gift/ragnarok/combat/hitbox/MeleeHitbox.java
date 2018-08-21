package box.gift.ragnarok.combat.hitbox;

import box.gift.ragnarok.constant.DurationConstant;
import box.gift.ragnarok.constant.ForceConstant;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;

public class MeleeHitbox extends Hitbox
{
    public MeleeHitbox(BoundingBox body, Character source, int damage)
    {
        super(body, source.TEAM, damage);

        setUpdatesLeft(DurationConstant.MELEE_HITBOX_DEFAULT);
        setTargetsLeft(Integer.MAX_VALUE);
        setKnockback(source, ForceConstant.DAMAGE_KNOCKBACK_DEFAULT);
    }
}
