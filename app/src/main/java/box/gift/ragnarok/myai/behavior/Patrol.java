package box.gift.ragnarok.myai.behavior;

import box.shoe.gameutils.Direction;
import box.gift.ragnarok.entity.enemy.Enemy;
import box.shoe.gameutils.CountDownScheduler;
import box.shoe.gameutils.Rng;
import box.shoe.gameutils.StateMachine;

public class Patrol extends EnemyBehavior
{
    private StateMachine state = new StateMachine("PATROL", "PAUSE", "PATROL");
    private boolean switchDirections = true;
    private Direction currentDirection = Direction.EAST;
    private CountDownScheduler scheduler = new CountDownScheduler();

    @Override
    public void enter(Enemy enemy)
    {
        scheduler.schedule(100, 0, new Runnable()
        {
            @Override
            public void run()
            {
                state.enterState("PAUSE");
                scheduler.schedule(Rng.intFrom(25, 40), 1, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        state.enterState("PATROL");
                        switchDirections = true;
                    }
                });
            }
        });
    }

    @Override
    public void behave(Enemy enemy)
    {
        scheduler.update();

        if (state.inState("PAUSE"))
        {
            return;
        }

        enemy.face(currentDirection);
        if (switchDirections)
        {
            currentDirection = Direction.fromTheta(currentDirection.VECTOR.rotateBy(Math.PI).getTheta());
            switchDirections = false;
        }
        enemy.moveForward();
    }

    @Override
    public void exit(Enemy enemy)
    {

    }
}
