package box.gift.ragnarok.myai;

import box.gift.ragnarok.entity.enemy.Enemy;
import box.shoe.gameutils.Updatable;
import box.shoe.gameutils.ai.AI;

public class EnemyAI implements Updatable
{
    private AI ai;

    public EnemyAI(AI ai)
    {
        this.ai = ai;
    }

    public void controlEnemy(Enemy enemy)
    {
        ai.control(enemy);
    }

    @Override
    public void update()
    {
        ai.update();
    }
}
