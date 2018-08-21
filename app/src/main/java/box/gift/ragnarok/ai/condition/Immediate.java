package box.gift.ragnarok.ai.condition;

import box.gift.ragnarok.entity.enemy.Enemy;

public class Immediate extends EnemyCondition
{
    public Immediate() {}

    @Override
    public boolean check(Enemy enemy)
    {
        return true;
    }
}
