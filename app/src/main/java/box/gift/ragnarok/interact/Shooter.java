package box.gift.ragnarok.interact;

import android.content.res.Resources;
import android.graphics.Canvas;

import box.gift.ragnarok.Ragnarok;
import box.gift.ragnarok.Team;
import box.gift.ragnarok.combat.attack.Attack;
import box.gift.ragnarok.combat.hitbox.OldHitbox;
import box.gift.ragnarok.combat.hitbox.RangeHitbox;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Direction;

public class Shooter extends InteractTile
{
    public Shooter(final BoundingBox body)
    {
        super(body);
    }

    private void fire()
    {
        Attack attack = new Attack();
        OldHitbox bullet = new RangeHitbox(new BoundingBox(body), Team.AssignableTeam.HOSTILE, 1);
        bullet.velocity = /*Rng.nextDirection()*/Direction.WEST.VECTOR.scale(3);
        attack.addHitbox(bullet);
        addAttack(attack);
    }

    @Override
    public void on(Character character)
    {

    }

    @Override
    public void touch(Character character)
    {

    }

    @Override
    public void update()
    {
        super.update();
        
        if (body.canSee(Ragnarok.stage.PLAYER.body, 160))
        {
            fire();
        }
    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {
        // Attacks.
        for (Attack attack : getAttacks())
        {
            if (attack != null)
            {
                attack.render(resources, canvas);
            }
        }
    }
}
