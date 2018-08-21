package box.gift.ragnarok.ai.condition;

import box.gift.ragnarok.entity.enemy.Enemy;

public class WithinAttackDistance extends EnemyCondition
{
    @Override
    public boolean check(Enemy enemy)
    {
        // Approximate. Checks if we are close enough to hit the target if it were a circle.
        return enemy.vectorTo(enemy.getTarget()).getMagnitude() - enemy.getTarget().body.width() / 2 < enemy.getComfortableAttackDistance();
    }
}
