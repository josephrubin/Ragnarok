package box.gift.ragnarok.combat.weapon;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import java.util.Collection;
import java.util.Collections;

import box.gift.ragnarok.combat.hitbox.AbstractHitbox;
import box.gift.ragnarok.combat.hitbox.TemporaryHitbox;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.CountDownScheduler;
import box.shoe.gameutils.Direction;
import box.shoe.gameutils.StateMachine;
import box.shoe.gameutils.Updatable;

public abstract class AbstractWeapon implements Updatable
{
    public final int COOLDOWN_UPDATES;

    protected StateMachine state = new StateMachine("IDLE", "IDLE", "COOLDOWN");
    private CountDownScheduler scheduler;

    public AbstractWeapon(int cooldown)
    {
        scheduler = new CountDownScheduler();
        COOLDOWN_UPDATES = cooldown;
    }

    public final Collection<TemporaryHitbox> requestAttack(@NonNull Character sourceCharacter, Direction direction)
    {
        if (state.inState("IDLE"))
        {
            sourceCharacter.face(direction);
            Collection<TemporaryHitbox> hitboxes = attack(sourceCharacter, direction);
            // Only on into cooldown if there is any amount of updates to wait.
            if (COOLDOWN_UPDATES > 0)
            {
                state.enterState("COOLDOWN");
                scheduler.schedule(COOLDOWN_UPDATES, 1, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        state.enterState("IDLE");
                    }
                });
            }
            else
            {
                state.enterState("IDLE");
            }
            return hitboxes;
        }
        return Collections.<TemporaryHitbox>emptyList();
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected abstract Collection<AbstractHitbox> attack(Character sourceCharacter, Direction direction);

    @Override
    public void update()
    {
        scheduler.update();
    }
}
