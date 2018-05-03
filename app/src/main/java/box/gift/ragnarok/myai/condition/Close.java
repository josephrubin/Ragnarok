package box.gift.ragnarok.myai.condition;

import box.gift.ragnarok.entity.enemy.Enemy;

public class Close extends EnemyCondition
{
    @Override
    public boolean check(Enemy enemy)
    {
        return enemy.vectorTo(enemy.getTarget()).getMagnitude() < 30;
    }
}
