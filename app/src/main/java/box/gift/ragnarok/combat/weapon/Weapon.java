package box.gift.ragnarok.combat.weapon;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import box.gift.ragnarok.Team;
import box.gift.ragnarok.combat.attack.Attack;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.CountDownScheduler;
import box.shoe.gameutils.Direction;
import box.shoe.gameutils.StateMachine;
import box.shoe.gameutils.Updatable;

public abstract class Weapon implements Updatable
{
    public final int COOLDOWN_UPDATES;

    protected StateMachine state = new StateMachine("IDLE", "IDLE", "COOLDOWN");
    private CountDownScheduler scheduler;

    private List<Attack> attacks;

    public Weapon(int cooldown)
    {
        attacks = new LinkedList<>();
        scheduler = new CountDownScheduler();
        COOLDOWN_UPDATES = cooldown;
    }

    public final boolean requestAttack(@NonNull Character sourceCharacter, Direction direction)
    {
        if (state.inState("IDLE"))
        {
            sourceCharacter.face(direction);
            attack(sourceCharacter, direction);
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
            return true;
        }
        return false;
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected abstract void attack(Character sourceCharacter, Direction direction);

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void addAttack(Attack attack)
    {
        attacks.add(attack);
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    private void removeAttack(Attack attack)
    {
        attacks.remove(attack);
    }

    public Collection<Attack> getAttacks()
    {
        return attacks;
    }

    public abstract float getComfortableAttackDistance();

    @Override
    public void update()
    {
        scheduler.update();

        Iterator<Attack> iterator = getAttacks().iterator();
        while (iterator.hasNext())
        {
            Attack attack = iterator.next();
            // Remove finished Attacks.
            if (attack.isFinished())
            {
                iterator.remove();
            }
            attack.update();
        }
    }
}
