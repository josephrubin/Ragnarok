package box.gift.ragnarok.combat.weapon;

import android.support.annotation.RestrictTo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

    public final boolean requestAttack(Character source, Direction direction)
    {
        if (state.inState("IDLE"))
        {
            attack(source, direction);
            // Only go into cooldown if there is any amount of updates to wait.
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
    protected abstract void attack(Character source, Direction direction);

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

    public List<Attack> getAttacks()
    {
        return attacks;
    }

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
