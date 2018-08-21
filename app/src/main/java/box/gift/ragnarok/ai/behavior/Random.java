package box.gift.ragnarok.ai.behavior;

import box.shoe.gameutils.Direction;
import box.gift.ragnarok.entity.enemy.Enemy;
import box.shoe.gameutils.CountDownScheduler;
import box.shoe.gameutils.Rng;

public class Random extends EnemyBehavior
{
    private boolean switchDirections = true;
    private CountDownScheduler scheduler = new CountDownScheduler();

    @Override
    public void enter(Enemy enemy)
    {
        scheduler.schedule(20, 0, new Runnable()
        {
            @Override
            public void run()
            {
                switchDirections = true;
            }
        });
    }

    @Override
    public void behave(Enemy enemy)
    {
        scheduler.update();
        if (switchDirections)
        {
            enemy.face(Direction.fromTheta(Math.toRadians(Rng.intFrom(0, 360))));
            switchDirections = false;
        }
        enemy.moveForward();
    }

    @Override
    public void exit(Enemy enemy)
    {

    }
}
