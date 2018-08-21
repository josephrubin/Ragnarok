package box.gift.ragnarok.ai.behavior;

import box.gift.ragnarok.Ragnarok;
import box.gift.ragnarok.entity.enemy.Enemy;

public class Charge extends EnemyBehavior
{
    @Override
    public void enter(Enemy enemy)
    {
        enemy.applyForce(enemy.vectorTo(Ragnarok.stage.PLAYER).unit().scale(10));
    }

    @Override
    public void behave(Enemy enemy)
    {

    }

    @Override
    public void exit(Enemy enemy)
    {

    }
}
