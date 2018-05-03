package box.gift.ragnarok.myai.behavior;

import box.gift.ragnarok.entity.enemy.Enemy;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.ai.Behavior;

public abstract class EnemyBehavior implements Behavior
{
    @Override
    public final void enter(Entity entity)
    {
        enter((Enemy) entity);
    }

    @Override
    public final void behave(Entity entity)
    {
        behave((Enemy) entity);
    }

    @Override
    public final void exit(Entity entity)
    {
        exit((Enemy) entity);
    }

    public abstract void enter(Enemy enemy);
    public abstract void behave(Enemy enemy);
    public abstract void exit(Enemy enemy);
}
