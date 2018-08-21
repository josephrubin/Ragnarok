package box.gift.ragnarok.ai.behavior;

import box.gift.ragnarok.entity.enemy.Enemy;
import box.shoe.gameutils.Vector;

public class Beeline extends EnemyBehavior
{
    private Vector toTarget;

    @Override
    public void enter(Enemy enemy)
    {
        toTarget = enemy.vectorTo(enemy.getTarget());
    }

    @Override
    public void behave(Enemy enemy)
    {
        enemy.moveAlongVector(toTarget);
    }

    @Override
    public void exit(Enemy enemy)
    {

    }
}
