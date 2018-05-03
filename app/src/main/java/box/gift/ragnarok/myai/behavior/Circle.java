package box.gift.ragnarok.myai.behavior;

import box.gift.ragnarok.Ragnarok;
import box.gift.ragnarok.entity.enemy.Enemy;
import box.shoe.gameutils.Rng;
import box.shoe.gameutils.Vector;

public class Circle extends EnemyBehavior
{
    private boolean clockwise;

    @Override
    public void enter(Enemy enemy)
    {
        clockwise = Rng.nextBoolean();
    }

    @Override
    public void behave(Enemy enemy)
    {
        Vector toPlayer = enemy.vectorTo(Ragnarok.stage.PLAYER).unit();
        Vector circlePlayer = toPlayer.perpendicular().scale(clockwise ? -1 : 1).add(toPlayer.scale(0.65f));
        enemy.moveAlongVector(circlePlayer);
    }

    @Override
    public void exit(Enemy enemy)
    {

    }
}
