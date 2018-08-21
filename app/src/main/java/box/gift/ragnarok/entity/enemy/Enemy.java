package box.gift.ragnarok.entity.enemy;

import box.gift.ragnarok.Team;
import box.gift.ragnarok.ai.EnemyAI;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Renderable;

/**
 * Created by Joseph on 3/23/2018.
 */

public abstract class Enemy extends Character implements Renderable
{
    // ___
    // AI.
    private Character target;
    private EnemyAI intelligence;

    private boolean spawned;

    // ________
    // WEAPONS.
    //private Weapon primaryWeapon;
    //private Weapon secondaryWeapon;

    public Enemy(BoundingBox body, EnemyAI intelligence)
    {
        super(body, Team.AssignableTeam.HOSTILE);
        spawned = false;
        this.intelligence = intelligence;
        intelligence.controlEnemy(this);
    }

    public void spawn(Character target)
    {
        if (spawned)
        {
            throw new IllegalStateException("This Enemy is already spawned! Use isSpawned() to check.");
        }
        this.target = target;
        spawned = true;
        INTERPOLATABLE_SERVICE.addMember(this);
    }

    public boolean isSpawned()
    {
        return spawned;
    }

    public Character getTarget()
    {
        return target;
    }

    public void moveTowardTarget()
    {
        moveAlongVector(vectorTo(getTarget()));
    }

    public boolean seesTarget()
    {
        return seesEntity(getTarget());
    }

    /*public void attackPrimary()
    {
        primaryWeapon.requestAttack(this);
    }

    public void attackSecondary()
    {
        secondaryWeapon.requestAttack(this);
    }*/

    @Override
    public boolean requestAttackOffense()
    {
        return super.requestAttackOffense();
    }

    @Override
    public void update()
    {
        if (spawned)
        {
            intelligence.update();
        }
        super.update();
    }
}
