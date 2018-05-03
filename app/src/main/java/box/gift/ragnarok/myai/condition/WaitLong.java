package box.gift.ragnarok.myai.condition;

import box.gift.ragnarok.entity.enemy.Enemy;
import box.shoe.gameutils.CountDownScheduler;
import box.shoe.gameutils.Rng;

public class WaitLong extends EnemyCondition
{
    private CountDownScheduler timer;
    private boolean timerElapsed;

    public WaitLong()
    {
        timer = new CountDownScheduler();
        timer.schedule(80 + Rng.intFrom(0, 5), 0, new Runnable()
        {
            @Override
            public void run()
            {
                timerElapsed = true;
            }
        });
    }

    @Override
    public boolean check(Enemy enemy)
    {
        timer.update();
        if (timerElapsed)
        {
            timerElapsed = false;
            return true;
        }
        return false;
    }
}
