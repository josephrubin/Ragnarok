package box.gift.ragnarok.entity;

import android.support.annotation.CallSuper;
import android.support.annotation.RestrictTo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.Vector;

/**
 * Created by Joseph on 3/16/2018.
 */
public abstract class PhysicsEntity extends Entity //todo: Make physics package in gameutils?
{
    protected List<Vector> forces; //todo: any reason to have Force class which contains a vector, and maybe a source (or Ambient if no source)?
    private Set<Object> forceKeys;

    protected float mass;
    protected float slip;

    public PhysicsEntity(BoundingBox body)
    {
        this(body, Vector.ZERO, Vector.ZERO);
    }

    public PhysicsEntity(BoundingBox body, Vector initialVelocity)
    {
        this(body, initialVelocity, Vector.ZERO);
    }

    public PhysicsEntity(BoundingBox body, Vector initialVelocity, Vector initialAcceleration)
    {
        super(body, initialVelocity, initialAcceleration);
        forces = new ArrayList<>();
        forceKeys = new HashSet<>();
        mass = 1;
        slip = 1;
    }

    public float getMass()
    {
        return mass;
    }

    public float getSlip()
    {
        return slip;
    }

    public void applyForce(Vector force)
    {
        forces.add(force);
    }

    public void applyUniqueForce(Vector force, Object key)
    {
        if (!forceKeys.contains(key))
        {
            forces.add(force);
            forceKeys.add(key);
        }
    }

    public void clearForces()
    {
        forces.clear();
        forceKeys.clear();
    }

    @Override
    public void update()
    {
        updateAcceleration();
        super.update();
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @CallSuper
    protected void updateAcceleration()
    {
        // We recalculate the acceleration each update based on the forces.
        acceleration = Vector.ZERO;
        for (Vector force : forces)
        {
            acceleration = acceleration.add(force);
        }

        // As per the formula: acceleration = force / mass.
        acceleration = acceleration.scale(1 / mass);

        // Forces must be reapplied each update.
        clearForces();
    }
}
