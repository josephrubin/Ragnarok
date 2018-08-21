package box.gift.ragnarok.combat.weapon;

import java.util.Collection;

import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.Team;
import box.gift.ragnarok.combat.attack.Attack;
import box.gift.ragnarok.combat.hitbox.AbstractHitbox;
import box.gift.ragnarok.combat.hitbox.RangeHitbox;
import box.gift.ragnarok.constant.DamageConstant;
import box.gift.ragnarok.constant.DurationConstant;
import box.gift.ragnarok.constant.ForceConstant;
import box.gift.ragnarok.constant.SpeedConstant;
import box.gift.ragnarok.constant.SpreadConstant;
import box.gift.ragnarok.constant.StunDuration;
import box.gift.ragnarok.combat.hitbox.OldHitbox;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Direction;

public class Shotgun extends RangeWeapon
{
    public Shotgun()
    {
        super(16);
    }

    @Override
    protected Collection<AbstractHitbox> attack(Character sourceCharacter, Direction direction)
    {
        Attack shot = new Attack();

        BoundingBox bulletBox = BoundingBox.fromCenter(sourceCharacter.body.centerX(), sourceCharacter.body.centerY(), 10);
        OldHitbox bullet1 = new ShotgunBulletHitbox(bulletBox, sourceCharacter.TEAM);
        bulletBox = new BoundingBox(bulletBox);
        OldHitbox bullet2 = new ShotgunBulletHitbox(bulletBox, sourceCharacter.TEAM);
        bulletBox = new BoundingBox(bulletBox);
        OldHitbox bullet3 = new ShotgunBulletHitbox(bulletBox, sourceCharacter.TEAM);

        bullet1.velocity = direction.VECTOR.scale(SpeedConstant.SHOTGUN_BULLET);
        bullet2.velocity = direction.VECTOR.scale(SpeedConstant.SHOTGUN_BULLET).rotateBy(Math.toRadians(SpreadConstant.SHOTGUN_DEGREES));
        bullet3.velocity = direction.VECTOR.scale(SpeedConstant.SHOTGUN_BULLET).rotateBy(Math.toRadians(-SpreadConstant.SHOTGUN_DEGREES));

        shot.addHitbox(bullet1);
        shot.addHitbox(bullet2);
        shot.addHitbox(bullet3);

        addAttack(shot);

        sourceCharacter.applyForce(direction.VECTOR.scale(-ForceConstant.SHOTGUN_KICKBACK));
        sourceCharacter.addStatusEffectForDuration(StatusEffect.STUN, StunDuration.SHOTGUN_FIRED);
    }

    @Override
    public float getComfortableAttackDistance()
    {
        return 100; //fixme:
    }

    private static class ShotgunBulletHitbox extends RangeHitbox
    {
        public ShotgunBulletHitbox(BoundingBox body, Team sourceTeam)
        {
            super(body, sourceTeam, DamageConstant.SHOTGUN);
            setTargetsLeft(1);
            setUpdatesLeft(DurationConstant.SHOTGUN_BULLET);
        }
    }
}
