package box.gift.ragnarok.combat.weapon;

import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.constant.DamageConstant;
import box.gift.ragnarok.constant.StunDuration;
import box.gift.ragnarok.hitbox.Hitbox;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.AABB;
import box.shoe.gameutils.Direction;

public class Shotgun extends Weapon
{
    public static final int BULLET_SPEED = 14;
    public static final int BULLET_DURATION = 5;

    public Shotgun()
    {
        super(16);
    }

    @Override
    protected void attack(Character source, Direction direction)
    {
        Attack shot = new Attack();

        AABB bulletBox = AABB.fromCenter(source.body.centerX(), source.body.centerY(), 10);
        Hitbox bullet1 = new Hitbox(bulletBox, source, DamageConstant.SHOTGUN, BULLET_DURATION);
        bulletBox = new AABB(bulletBox);
        Hitbox bullet2 = new Hitbox(bulletBox, source, DamageConstant.SHOTGUN, BULLET_DURATION);
        bulletBox = new AABB(bulletBox);
        Hitbox bullet3 = new Hitbox(bulletBox, source, DamageConstant.SHOTGUN, BULLET_DURATION);

        bullet1.velocity = direction.VECTOR.scale(BULLET_SPEED);
        bullet2.velocity = direction.VECTOR.scale(BULLET_SPEED).rotateBy(Math.toRadians(16));
        bullet3.velocity = direction.VECTOR.scale(BULLET_SPEED).rotateBy(Math.toRadians(-16));

        shot.addHitbox(bullet1);
        shot.addHitbox(bullet2);
        shot.addHitbox(bullet3);

        addAttack(shot);

        source.applyForce(direction.VECTOR.scale(-8));
        source.addStatusEffectForDuration(StatusEffect.STUN, StunDuration.SHOTGUN_FIRED);
    }
}
