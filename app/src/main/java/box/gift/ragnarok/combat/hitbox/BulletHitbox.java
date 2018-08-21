package box.gift.ragnarok.combat.hitbox;

import box.gift.ragnarok.Team;
import box.gift.ragnarok.constant.HitboxDuration;
import box.gift.ragnarok.interact.InteractTile;
import box.shoe.gameutils.BoundingBox;

public class BulletHitbox extends AbstractDamagingHitbox
{
    public BulletHitbox(BoundingBox body, Team sourceTeam, int damage)
    {
        super(body, sourceTeam, HitboxDuration.RANGE_DEFAULT, 1, damage);
    }

    @Override
    public void onInteractTile(InteractTile interactTile)
    {
        onCollision();
    }

    @Override
    public void onCollision()
    {
        requestFinish();
    }
}
