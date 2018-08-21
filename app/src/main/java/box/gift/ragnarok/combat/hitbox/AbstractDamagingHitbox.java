package box.gift.ragnarok.combat.hitbox;

import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.Team;
import box.gift.ragnarok.constant.StunDuration;
import box.gift.ragnarok.entity.Character;
import box.gift.ragnarok.interact.InteractTile;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Direction;

public abstract class AbstractDamagingHitbox extends AbstractHitbox
{
    private final int damage;

    public AbstractDamagingHitbox(BoundingBox body, Team sourceTeam, int updatesLeft, int targetsLeft, int damage)
    {
        super(body, sourceTeam, updatesLeft, targetsLeft);

        this.damage = damage;
    }

    @Override
    public void onCharacter(Character character)
    {
        // Deal the damage.
        if (character.requestDamage(damage, team))
        {
            targetHit();

            // Do the knockback.
            //character.applyForce(knockbackFrom.vectorTo(character).unit().scale(knockbackAmount));
            //character.applyForce(Direction.fromTheta(knockbackFrom.vectorTo(character).getTheta()).VECTOR.scale(knockbackAmount));
            //character.addStatusEffectForDuration(StatusEffect.STUN, StunDuration.DAMAGE_TAKEN_DEFAULT);
        }
    }
}
