package box.gift.ragnarok.combat.weapon;

import java.util.Collection;

import box.gift.ragnarok.combat.hitbox.AbstractHitbox;
import box.gift.ragnarok.constant.DamageConstant;
import box.shoe.gameutils.Direction;
import box.gift.ragnarok.entity.Character;

public class Sword extends MeleeWeapon
{
    private static final int ATTACK_UPDATES = 4;

    public Sword()
    {
        super(12, 24);
    }

    @Override
    protected Collection<AbstractHitbox> attack(Character sourceCharacter, Direction direction)
    {
        return createArcingAttack(sourceCharacter, direction, DamageConstant.SWORD, ATTACK_UPDATES);

        //todo: how to add stun to source?
        //source.addStatusEffectForDuration(StatusEffect.STUN, ATTACK_UPDATES);
        return null;
    }
}
