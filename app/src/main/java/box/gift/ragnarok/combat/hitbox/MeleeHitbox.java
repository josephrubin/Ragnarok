package box.gift.ragnarok.combat.hitbox;

import box.gift.ragnarok.constant.HitboxDuration;
import box.gift.ragnarok.entity.Character;
import box.gift.ragnarok.interact.InteractTile;
import box.shoe.gameutils.BoundingBox;

public class MeleeHitbox extends AbstractDamagingHitbox
{
    public MeleeHitbox(BoundingBox body, Character source, int damage)
    {
        super(body, source.TEAM, HitboxDuration.MELEE_DEFAULT, UNLIMITED, damage);
    }

    @Override
    public void onInteractTile(InteractTile interactTile)
    {

    }

    @Override
    public void onCollision()
    {

    }
}
