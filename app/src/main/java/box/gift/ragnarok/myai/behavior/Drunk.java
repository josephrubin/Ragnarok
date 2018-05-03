package box.gift.ragnarok.myai.behavior;

import box.gift.ragnarok.entity.enemy.Enemy;
import box.shoe.gameutils.Rng;
import box.shoe.gameutils.Vector;

public class Drunk extends EnemyBehavior
{
    Vector currentVelocity;

    @Override
    public void enter(Enemy enemy)
    {
        switch (Rng.intFrom(0, 3))
        {
            case 0:
                currentVelocity = Vector.WEST;
                break;
            case 1:
                currentVelocity = Vector.NORTH;
                break;
            case 2:
                currentVelocity = Vector.EAST;
                break;
            case 3:
                currentVelocity = Vector.SOUTH;
                break;
        }
    }

    @Override
    public void behave(Enemy enemy)
    {
        enemy.moveAlongVector(currentVelocity);
        boolean adjustClockwise = Rng.nextBoolean();
        if (adjustClockwise)
        {
            currentVelocity = currentVelocity.rotateBy(Math.toRadians(30));
        }
        else
        {
            currentVelocity = currentVelocity.rotateBy(Math.toRadians(-30));
        }
    }

    @Override
    public void exit(Enemy enemy)
    {

    }
}
