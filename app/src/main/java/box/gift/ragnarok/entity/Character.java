package box.gift.ragnarok.entity;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.CallSuper;
import android.support.annotation.RestrictTo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import box.gift.ragnarok.Afflictable;
import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.Team;
import box.gift.ragnarok.combat.hitbox.TemporaryHitbox;
import box.gift.ragnarok.combat.weapon.AbstractWeapon;
import box.gift.ragnarok.constant.StatisticDefaultConstant;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Direction;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.Renderable;
import box.shoe.gameutils.Vector;

/**
 * Created by Joseph on 3/23/2018.
 */

public abstract class Character extends CollisionEntity implements Renderable
{
    private static final Paint defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // _______
    // COMBAT.

    private int invincibilityUpdates = StatisticDefaultConstant.INVINCIBILITY_UPDATES;
    private int currentInvincibilityUpdate = invincibilityUpdates;

    protected int health;

    private AbstractWeapon wieldingOffense; //todo: methods to wield weapons.
    private AbstractWeapon wieldingDefense; //todo: Attacker and Blocker instead of Weapon?

    private List<TemporaryHitbox> temporaryHitboxes;

    public final Team.AssignableTeam TEAM;

    // ________
    // EFFECTS.

    private Collection<Afflictable> afflictables;
    private Map<StatusEffect, Integer> statusEffects;

    // _______
    // STATUS.

    public Direction facing;
    private float sightRange;

    public float moveSpeed;

    //TODO: invince should work as follows: until invince updates run out, any source of damage is reduced by amount already taken (min 0).

    public Character(BoundingBox body, Team.AssignableTeam team)
    {
        super(body);

        this.TEAM = team;

        afflictables = new LinkedList<>(); //todo: should be a set, allowing each type to be applied only once?
        statusEffects = new HashMap<>();
        temporaryHitboxes = new LinkedList<>();

        // Arbitrary decision to make characters face south by default.
        //todo: make into DefaultConstant ?
        facing = Direction.SOUTH;

        this.health = StatisticDefaultConstant.CHARACTER_HEALTH;
        this.moveSpeed = StatisticDefaultConstant.MOVE_SPEED;
        this.sightRange = StatisticDefaultConstant.SIGHT_RANGE;

        //INTERPOLATABLE_SERVICE.addMember(this); //todo: remove member upon death.
    }

    public void wieldOffense(AbstractWeapon weapon)
    {
        wieldingOffense = weapon;
    }

    public float getComfortableAttackDistance()
    {
        return wieldingOffense.getComfortableAttackDistance();
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void requestAttackOffense()
    {
        Collection<TemporaryHitbox> additionalTemporaryHitboxes = wieldingOffense.requestAttack(this, facing);
        temporaryHitboxes.addAll(additionalTemporaryHitboxes);
    }

    public void moveAlongVector(Vector vector)
    {
        if (!hasStatusEffect(StatusEffect.STUN))
        {
            face(Direction.fromTheta(vector.getTheta()));
            applyForce(vector.unit().scale(moveSpeed * mass));
        }
    }

    public void moveForward()
    {
        moveAlongVector(facing.VECTOR);
    }

    public void face(Direction direction)
    {
        this.facing = direction;
    }

    public boolean seesEntity(Entity entity)
    {
        return false;
    }

    /**
     * Request that the Character takes damage.
     * @param damageAmount the amount of damage the Character should take.
     * @return true if the Character took any damage, and false otherwise.
     */
    public boolean requestDamage(int damageAmount, Team sourceTeam)
    {
        if (!isInvincible() && TEAM != sourceTeam)
        {
            // Actually take the damage.
            health -= damageAmount;
            // Health cannot be negative.
            if (health < 0)
            {
                health = 0;
            }
            currentInvincibilityUpdate = 0;
            return true;
        }
        return false;
    }

    public int getCurrentHealth()
    {
        return health;
    }

    public int getInvincibilityUpdates()
    {
        return invincibilityUpdates;
    }

    public void afflict(Afflictable afflictable)
    {
        afflict(afflictable, false);
    }

    public void afflict(Afflictable afflictable, boolean allowMultipleInstances)
    {
        if (allowMultipleInstances || !afflictables.contains(afflictable))
        {
            afflictables.add(afflictable);
        }
    }

    public void addStatusEffectForDuration(StatusEffect statusEffect, int updates)
    {
        // Apply the StatusEffect with the greater duration.
        if (!statusEffects.containsKey(statusEffect) || updates > statusEffects.get(statusEffect))
        {
            statusEffects.put(statusEffect, updates);
        }
    }

    public boolean hasStatusEffect(StatusEffect statusEffect)
    {
        // Any StatusEffect with 0 duration left has already been removed,
        // so if it is still here, it still affects the Character.
        return statusEffects.containsKey(statusEffect);
    }

    public void clearStatusEffects()
    {
        statusEffects.clear();
    }

    public boolean isInvincible()
    {
        return currentInvincibilityUpdate < invincibilityUpdates;
    }

    @CallSuper
    public Collection<TemporaryHitbox> getHitboxes()
    {
        return temporaryHitboxes;
    }

    @Override
    public void update()
    {
        super.update();

        // Update all temporary hitboxes and remove the finished ones.
        Iterator<TemporaryHitbox> iterator = getHitboxes().iterator();
        while (iterator.hasNext())
        {
            TemporaryHitbox hitbox = iterator.next();
            // Remove finished Hitboxes.
            if (hitbox.isFinished())
            {
                iterator.remove();
            }
            hitbox.update();
        }

        // Afflict this Character.
        Iterator<Afflictable> afflictableIterator = afflictables.iterator();
        while (afflictableIterator.hasNext())
        {
            Afflictable afflictable = afflictableIterator.next();
            // Apply the Afflictable, and remove it if it indicated that it is done.
            if (!afflictable.applyTo(this))
            {
                afflictableIterator.remove();
            }
        }

        // Count down and end Status Effects.
        Iterator<Map.Entry<StatusEffect, Integer>> entryIterator = statusEffects.entrySet().iterator();
        while (entryIterator.hasNext())
        {
            Map.Entry<StatusEffect, Integer> entry = entryIterator.next();
            // Reduce the duration by 1.
            entry.setValue(entry.getValue() - 1);
            // If the duration is gone, remove the StatusEffect.
            if (entry.getValue() <= 0)
            {
                entryIterator.remove();
            }
        }

        // Count invincibility updates.
        currentInvincibilityUpdate += 1;
        if (currentInvincibilityUpdate > invincibilityUpdates)
        {
            currentInvincibilityUpdate = invincibilityUpdates;
        }
    }

    @Override
    protected void updateVelocity()
    {
        super.updateVelocity();

        // Total velocity is lowered if we are slowed.
        if (hasStatusEffect(StatusEffect.SLOWED))
        {
            velocity = velocity.scale(StatusEffect.SLOWED_FACTOR);
        }
    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {
        // Render body.
        defaultPaint.setColor(Color.BLACK);
        if (isInvincible())
        {
            defaultPaint.setColor(Color.GREEN);
        }
        canvas.drawRect(display, defaultPaint);

        // Render hitboxes.
        for (AbstractHitbox hitbox : getHitboxes())
        {
            hitbox.render(resources, canvas);
        }

        // Facing direction.
        defaultPaint.setColor(Color.BLUE);
        canvas.drawCircle(display.centerX() + facing.VECTOR.scale(display.width() / 2).getX(), display.centerY() + facing.VECTOR.scale(display.width() / 2).getY(), 1.75f, defaultPaint);
    }
}
