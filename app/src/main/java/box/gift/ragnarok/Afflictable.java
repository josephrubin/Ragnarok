package box.gift.ragnarok;

import box.gift.ragnarok.constant.DamageConstant;
import box.gift.ragnarok.constant.StunDuration;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.Rng;

/**
 * Created by Joseph on 3/23/2018.
 */

public interface Afflictable
{
    /**
     * The supplied character is affected by this Afflictable.
     * @param character the character to affect.
     * @return true if the Character should be affected next update too, and false otherwise.
     */
    boolean applyTo(Character character);

    Afflictable POISON = new Afflictable()
    {
        @Override
        public boolean applyTo(Character character)
        {
            // Deal damage, and freeze for just a bit if damage was taken.
            if (character.requestDamage(DamageConstant.POISON, Team.UNALIGNED))
            {
                character.addStatusEffectForDuration(StatusEffect.STUN, StunDuration.DAMAGE_TAKEN_DEFAULT);
            }
            // Random chance to end the effect.
            return Rng.intFrom(0, 28) != 0;
        }
    };
}
