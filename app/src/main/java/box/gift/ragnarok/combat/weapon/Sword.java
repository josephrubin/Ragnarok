package box.gift.ragnarok.combat.weapon;

import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.constant.DamageConstant;
import box.gift.ragnarok.hitbox.Hitbox;
import box.shoe.gameutils.AABB;
import box.shoe.gameutils.Direction;
import box.gift.ragnarok.entity.Character;
import box.gift.ragnarok.entity.Player;
import box.shoe.gameutils.Vector;

public class Sword extends Weapon
{
    public static final int ATTACK_UPDATES = 8;

    public Sword()
    {
        super(12);
    }

    @Override
    protected void attack(Character source, Direction direction)
    {
        final Attack attack = new Attack();
        float hitboxWidth;
        float hitboxHeight;
        if (direction == Direction.EAST || direction == Direction.WEST)
        {
            hitboxWidth = Player.WIDTH * 1.2f;
            hitboxHeight = Player.HEIGHT * 2.1f;
        }
        else
        {
            hitboxWidth = Player.WIDTH * 2.1f;
            hitboxHeight = Player.HEIGHT * 1.2f;
        }
        Hitbox damageBox = new Hitbox(new AABB(0, 0, hitboxWidth, hitboxHeight), source, DamageConstant.SWORD, ATTACK_UPDATES);
        damageBox.body.offsetCenterTo(source.body.centerX(), source.body.centerY());

        Vector directionVector = direction.VECTOR.scale(Player.WIDTH);
        damageBox.body.offset(directionVector.getX(), directionVector.getY());
        attack.addHitbox(damageBox);

        addAttack(attack);

        source.addStatusEffectForDuration(StatusEffect.STUN, ATTACK_UPDATES);
    }
}
