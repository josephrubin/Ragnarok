package box.gift.ragnarok.ai.behavior;

import box.gift.ragnarok.entity.enemy.Enemy;

public class Chase extends EnemyBehavior
{
    @Override
    public void enter(Enemy enemy)
    {

    }

    @Override
    public void behave(Enemy enemy)
    {
        enemy.moveTowardTarget();
    }

    @Override
    public void exit(Enemy enemy)
    {

    }
}
