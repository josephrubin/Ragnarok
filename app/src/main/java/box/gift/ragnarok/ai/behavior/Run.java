package box.gift.ragnarok.ai.behavior;

import box.gift.ragnarok.entity.enemy.Enemy;

public class Run extends EnemyBehavior
{
    @Override
    public void enter(Enemy enemy)
    {

    }

    @Override
    public void behave(Enemy enemy)
    {
        enemy.moveAlongVector(enemy.getTarget().vectorTo(enemy));
    }

    @Override
    public void exit(Enemy enemy)
    {

    }
}
