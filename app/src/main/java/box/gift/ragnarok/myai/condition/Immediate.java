package box.gift.ragnarok.myai.condition;

import box.gift.ragnarok.entity.enemy.Enemy;
import box.shoe.gameutils.CountDownScheduler;
import box.shoe.gameutils.Rng;

public class Immediate extends EnemyCondition
{
    public Immediate() {}

    @Override
    public boolean check(Enemy enemy)
    {
        return true;
    }
}
