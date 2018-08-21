package box.gift.ragnarok.combat.weapon;

import box.gift.ragnarok.Ragnarok;
import box.gift.ragnarok.Team;
import box.gift.ragnarok.combat.attack.Attack;
import box.gift.ragnarok.combat.hitbox.HomingHitbox;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Direction;

/**
 * @deprecated not yet ready for use
 */
public class Homer// extends RangeWeapon
{/*
    public static final int HOMER_SPEED = 7;

    public Homer(int cooldown)
    {
        super(cooldown);
    }

    @Override
    protected void attack(float xPos, float yPos, Team sourceTeam, Direction direction)
    {
        Attack shot = new Attack();
        HomingHitbox homing = new HomingHitbox(BoundingBox.fromCenter(xPos, yPos, 16), sourceTeam,1, 55);

        float lowestDistance = 1000;
        Character target = null;
        for (Character character : Ragnarok.stage.CHARACTERS)
        {
            //todo: how to not target the source. maybe just skip all whose team is the source from.
            *//*if (source == character)
            {
                continue;
            }*//*
            float distance = homing.vectorTo(character).getMagnitude();
            if (distance < lowestDistance)
            {
                lowestDistance = distance;
                target = character;
            }
        }

        homing.home(target, HOMER_SPEED, direction);
        shot.addHitbox(homing);
        addAttack(shot);
    }

    @Override
    public float getComfortableAttackDistance()
    {
        return 100; //fixme:
    }*/
}
