package box.gift.ragnarok.combat.weapon;

import android.support.annotation.RestrictTo;

import java.util.ArrayList;
import java.util.Collection;

import box.gift.ragnarok.combat.hitbox.AbstractDamagingHitbox;
import box.gift.ragnarok.combat.hitbox.AbstractHitbox;
import box.gift.ragnarok.combat.hitbox.MeleeHitbox;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Direction;

public abstract class MeleeWeapon extends AbstractWeapon
{
    public MeleeWeapon(int cooldown)
    {
        super(cooldown);
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected Collection<AbstractHitbox> createArcingAttack(Character sourceCharacter, Direction direction, int damage, int durationUpdates, float arcRadius)
    {
        float xPos = sourceCharacter.body.centerX();
        float yPos = sourceCharacter.body.centerY();

        Collection<AbstractHitbox> arcingAttack = new ArrayList<>();
        float trianglePartDistance = (float) (arcRadius * Math.cos(Math.PI / 4));

        final float sliceWidth = 2;
        final float sliceCount = arcRadius / sliceWidth;
        for (int i = 0; i < sliceCount; i++)
        {
            float left = xPos + i * sliceWidth;
            float right = left + sliceWidth;

            // Using middle (0.5f).
            float x = (i + 0.5f) * sliceWidth;

            // There are really two parts in the sector - the triangle (y = x) and the circle (y = sqrt(r^2 - x^2)).
            float y;
            if (x < trianglePartDistance)
            {
                y = x;
            }
            else
            {
                y = (float) Math.sqrt((arcRadius * arcRadius) - (x * x));
            }

            float bottom = yPos + y;
            float top = yPos - y;

            AbstractDamagingHitbox hitbox = new MeleeHitbox(new BoundingBox(left, top, right, bottom), sourceCharacter, damage);
            hitbox.body.rotate(Direction.EAST, direction, xPos, yPos);
            arcingAttack.add(hitbox);
        }

        return arcingAttack;
    }
}
