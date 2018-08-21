package box.gift.ragnarok.combat.hitbox;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.RestrictTo;

import box.gift.ragnarok.Interactable;
import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.Team;
import box.gift.ragnarok.constant.DamageConstant;
import box.gift.ragnarok.constant.ForceConstant;
import box.gift.ragnarok.constant.HitboxDuration;
import box.gift.ragnarok.constant.StunDuration;
import box.gift.ragnarok.interact.InteractTile;
import box.shoe.gameutils.BoundingBox;
import box.gift.ragnarok.entity.Character;
import box.shoe.gameutils.Direction;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.Renderable;

public abstract class Hitbox extends Entity implements Interactable, Renderable
{
    private static final Paint DEFAULT_HITBOX_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    static
    {
        DEFAULT_HITBOX_PAINT.setColor(Color.RED);
    }

    public static final int ETERNAL = -1;

    private int damage;
    private int targetsLeft;
    private int updatesLeft;

    private Team sourceTeam;

    private boolean isFinished;

    private Entity knockbackFrom;
    private float knockbackAmount;

    // use ETERNAL for never dying hitbox
    public Hitbox(BoundingBox body, Team sourceTeam, int damage)
    {
        super(body);

        isFinished = false;

        this.sourceTeam = sourceTeam;
        this.damage = damage;

        // This is an abstract class so subclasses should set these, but in case not, provide sensible, general defaults.
        setUpdatesLeft(HitboxDuration.DEFAULT);
        setTargetsLeft(Integer.MAX_VALUE);
        setKnockback(0);
    }

    public void setUpdatesLeft(int updatesLeft)
    {
        if (updatesLeft <= 0)
        {
            throw new IllegalArgumentException("updatesLeft must be > 0");
        }
        this.updatesLeft = updatesLeft;
    }

    public void setTargetsLeft(int targetsLeft)
    {
        if (targetsLeft <= 0)
        {
            throw new IllegalArgumentException("targetsLeft must be > 0");
        }
        this.targetsLeft = targetsLeft;
    }

    public void setKnockback(float amount)
    {
        setKnockback(this, amount);
    }

    public void setKnockback(Entity from, float amount)
    {
        this.knockbackFrom = from;
        this.knockbackAmount = amount;
    }

    public boolean isFinished()
    {
        return isFinished;
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void finish()
    {
        isFinished = true;
    }

    @Override
    public void update()
    {
        super.update();
        if (updatesLeft != ETERNAL)
        {
            if (updatesLeft <= 0)
            {
                finish();
            }
            updatesLeft -= 1;
        }
    }

    @Override
    public final void onCharacter(Character character)
    {
        // Deal the damage.
        if (character.requestDamage(damage, sourceTeam))
        {
            targetsLeft -= 1;
            if (targetsLeft <= 0)
            {
                finish();
            }
            harmCharacter(character);
        }
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void harmCharacter(Character character)
    {
        // Do the knockback.
        //character.applyForce(knockbackFrom.vectorTo(character).unit().scale(knockbackAmount));
        character.applyForce(Direction.fromTheta(knockbackFrom.vectorTo(character).getTheta()).VECTOR.scale(knockbackAmount));
        character.addStatusEffectForDuration(StatusEffect.STUN, StunDuration.DAMAGE_TAKEN_DEFAULT);
    }

    @Override
    public void hitInteractTile(InteractTile interactTile)
    {
        hitCollision();
    }

    @Override
    public void hitCollision()
    {
        finish();
    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {
        canvas.drawRect(body, DEFAULT_HITBOX_PAINT);
    }
}
