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
import box.shoe.gameutils.CountDownScheduler;

public class Gunner extends InteractTile
{
    private CountDownScheduler scheduler;

    public Gunner(BoundingBox body)
    {
        super(body);
        scheduler = new CountDownScheduler();
        scheduler.schedule(70, 0, new Runnable()
        {
            @Override
            public void run()
            {
                fire();
            }
        });
    }

    @Override
    public void on(Character character)
    {

    }

    @Override
    public void touch(Character character)
    {

    }

    private void fire()
    {
        Attack attack = new Attack();
        BoundingBox bulletBody = new BoundingBox(body);
        bulletBody.inset(6, 6);
        OldHitbox bullet = new RangeHitbox(bulletBody, Team.AssignableTeam.HOSTILE, 1);
        bullet.velocity = vectorTo(Ragnarok.stage.PLAYER).unit().scale(3.5);
        attack.addHitbox(bullet);
        addAttack(attack);
    }

    @Override
    public void update()
    {
        super.update();

        scheduler.update();
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
