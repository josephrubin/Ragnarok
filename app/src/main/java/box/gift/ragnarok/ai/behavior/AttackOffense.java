package box.gift.ragnarok.ai.behavior;

import box.gift.ragnarok.entity.enemy.Enemy;

public class AttackOffense extends EnemyBehavior
{
    @Override
    public void enter(Enemy enemy)
    {

    }

    @Override
    public void behave(Enemy enemy)
    {
        enemy.requestAttackOffense();
    }

    @Override
    public void exit(Enemy enemy)
    {

    }
}
