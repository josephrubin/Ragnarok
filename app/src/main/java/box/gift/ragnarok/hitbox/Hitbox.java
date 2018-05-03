package box.gift.ragnarok.hitbox;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.RestrictTo;

import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.constant.ForceConstant;
import box.gift.ragnarok.constant.StunDuration;
import box.shoe.gameutils.AABB;
import box.shoe.gameutils.Direction;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.Renderable;

public class Hitbox extends Entity implements Renderable
{
    public static final float KNOCKBACK = ForceConstant.DAMAGE_DEFAULT;
    private static final Paint DEFAULT_HITBOX_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    static
    {
        DEFAULT_HITBOX_PAINT.setColor(Color.RED);
    }

    public static final int ETERNAL = -1;

    private int damage;
    private int updates;
    private Character source;

    private boolean isFinished;

    // use ETERNAL for never dying hitbox
    public Hitbox(AABB body, Character source, int damage, int updates)
    {
        super(body);

        isFinished = false;

        this.damage = damage;
        this.updates = updates;
        this.source = source;
    }

    // Feel free to override in specialized hitboxes to apply other effects.
    public void hit(Character character)
    {
        // Deal the damage.
        if (character.requestDamage(damage))
        {
            // Do the knockback.
            character.applyForce(Direction.fromTheta(source.vectorTo(character).getTheta()).VECTOR.scale(KNOCKBACK));
            character.addStatusEffectForDuration(StatusEffect.STUN, StunDuration.DAMAGE_TAKEN_DEFAULT);
        }
    }

    public boolean isSource(Character character)
    {
        return source != null && character.equals(source);
    }

    public boolean isFinished()
    {
        return isFinished;
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void end()
    {
        isFinished = true;
    }

    @Override
    public void update()
    {
        super.update();
        if (updates != ETERNAL)
        {
            if (updates <= 0)
            {
                end();
            }
            updates -= 1;
        }
    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {
        canvas.drawRect(body, DEFAULT_HITBOX_PAINT);
    }
}
