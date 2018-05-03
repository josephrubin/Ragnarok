package box.gift.ragnarok.entity;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.support.annotation.CallSuper;
import android.support.annotation.RestrictTo;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import box.gift.ragnarok.Ragnarok;
import box.shoe.gameutils.AABB;
import box.shoe.gameutils.Axis;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.Vector;
import box.shoe.gameutils.map.TileMapLoader;

/**
 * Created by Joseph on 3/16/2018.
 */
//todo: separate forces and phys properties into PhysicsEntity, and perhaps collisions stay here. Make physics package in gameutils
public class CollisionEntity extends Entity
{
    private static AABB tileBounds = new AABB();

    public static SparseArray<RectF> collisionRectangles;
    public static int TILE_WIDTH_PX;
    public static int TILE_HEIGHT_PX;

    private List<Vector> forces; //todo: any reason to have Force class which contains a vector, and maybe a source (or Ambient if no source)?
    private Set<Object> forceKeys;

    public float mass;
    public float slip;

    // To prevent very fast CollisionEntities from clipping through others, we divide movement into a number of steps.
    // On each step, the CollisionEntity is moved by a portion of its velocity, and collision is calculated.
    // The higher the number, the less likely that clipping happens, but the slower collision code runs.
    private static int NUMBER_OF_MOVEMENT_STEPS = 6;

    public CollisionEntity(AABB body)
    {
        this(body, Vector.ZERO, Vector.ZERO);
    }

    public CollisionEntity(AABB body, Vector initialVelocity)
    {
        this(body, initialVelocity, Vector.ZERO);
    }

    public CollisionEntity(AABB body, Vector initialVelocity, Vector initialAcceleration)
    {
        super(body, initialVelocity, initialAcceleration);
        forces = new ArrayList<>();
        forceKeys = new HashSet<>();
        mass = 1;
        slip = 1;
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

    @SuppressLint("MissingSuperCall")
    @Override
    protected void updatePosition()
    {
        boolean collided = false;

        // X (horizontal) direction.
        if (updatePositionAxis(Axis.X))
        {
            collided = true;
        }

        // Y (vertical) direction.
        if (updatePositionAxis(Axis.Y))
        {
            collided = true;
        }

        if (collided)
        {
            onCollide();
        }
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected boolean updatePositionAxis(Axis axis)
    {
        boolean collided = false;

        Vector axisVelocity = velocity.projectOnto(axis.POSITIVE_DIRECTION.VECTOR);
        Vector axisVelocityStep = axisVelocity.scale(1f / NUMBER_OF_MOVEMENT_STEPS);
        for (int k = 0; k < NUMBER_OF_MOVEMENT_STEPS; k++)
        {
            // Move according to our velocity step...
            body.offset(axisVelocityStep);

            // ...then check collisions.
            // Check for collision with collision tiles.
            for (int i = 0; i < Ragnarok.stage.COLLISION.length; i++)
            {
                for (int j = 0; j < Ragnarok.stage.COLLISION[i].length; j++)
                {
                    int type = Ragnarok.stage.COLLISION[i][j];

                    if (type != TileMapLoader.EMPTY_TILE)
                    {
                        setTileBounds(type, i, j);

                        if (body.intersects(tileBounds))
                        {
                            collided = true;
                            resolveCollisionWith(tileBounds, axis);
                        }
                    }
                }
            }

            // Check for collision with other Characters.
            //TODO: should they push each other?
            for (Character character : Ragnarok.stage.CHARACTERS)
            {
                if (this == character)
                {
                    continue;
                }

                if (body.intersects(character.body))
                {
                    collided = true;
                    resolveCollisionWith(character.body, axis);
                }
            }
        }

        return collided;
    }

    private void resolveCollisionWith(RectF bounds, Axis axis)
    {
        // Clear velocity/acceleration/forces acting in the collision direction.
        velocity = velocity.projectOnto(axis.POSITIVE_DIRECTION.VECTOR.perpendicular());
        acceleration = acceleration.projectOnto(axis.POSITIVE_DIRECTION.VECTOR.perpendicular());
        ListIterator<Vector> iterator = forces.listIterator();
        while (iterator.hasNext())
        {
            iterator.set(iterator.next().projectOnto(axis.POSITIVE_DIRECTION.VECTOR.perpendicular()));
        }

        // Shift so we are no longer colliding.
        if (axis == Axis.X)
        {
            if (body.centerX() > bounds.centerX())
            {
                body.offsetLeftTo(bounds.right);
            }
            else
            {
                body.offsetRightTo(bounds.left);
            }
        }
        else if (axis == Axis.Y)
        {
            if (body.centerY() > bounds.centerY())
            {
                body.offsetTopTo(bounds.bottom);
            }
            else
            {
                body.offsetBottomTo(bounds.top);
            }
        }
    }

    private static void setTileBounds(int type, int row, int column)
    {
        tileBounds = new AABB(collisionRectangles.get(type));
        tileBounds.offset(row * TILE_WIDTH_PX, column * TILE_HEIGHT_PX);
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void onCollide() {}

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void onBottomOn() {}
}
