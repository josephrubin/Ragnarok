package box.gift.ragnarok.ai.condition;

import box.gift.ragnarok.entity.enemy.Enemy;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.ai.Condition;

public abstract class EnemyCondition implements Condition
{
    @Override
    public boolean check(Entity entity)
    {
        return check((Enemy) entity);
    }

    public abstract boolean check(Enemy enemy);
}
