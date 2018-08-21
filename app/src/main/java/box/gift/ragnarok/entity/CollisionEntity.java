package box.gift.ragnarok.entity;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.support.annotation.RestrictTo;
import android.util.SparseArray;

import java.util.ListIterator;

import box.gift.ragnarok.Ragnarok;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Axis;
import box.shoe.gameutils.Vector;
import box.shoe.gameutils.map.TileMapLoader;

/**
 * Created by Joseph on 3/16/2018.
 */

public abstract class CollisionEntity extends PhysicsEntity
{
    private static BoundingBox tileBounds = new BoundingBox();

    //todo: these should not stay here if at all possible.
    public static SparseArray<RectF> collisionRectangles;
    public static int TILE_WIDTH_PX;
    public static int TILE_HEIGHT_PX;

    // To prevent very fast CollisionEntities from clipping through others, we divide movement into a number of steps.
    // On each step, the CollisionEntity is moved by a portion of its velocity, and collision is calculated.
    // The higher the number, the less likely that clipping happens, but the slower collision code runs.
    // For your game, you should run tests and decide on a  number empirically (for games with fast-moving
    // Entities or thin walls you will likely want a higher value.)
    private static int NUMBER_OF_MOVEMENT_STEPS = 6;

    public CollisionEntity(BoundingBox body)
    {
        this(body, Vector.ZERO, Vector.ZERO);
    }

    public CollisionEntity(BoundingBox body, Vector initialVelocity)
    {
        this(body, initialVelocity, Vector.ZERO);
    }

    public CollisionEntity(BoundingBox body, Vector initialVelocity, Vector initialAcceleration)
    {
        super(body, initialVelocity, initialAcceleration);
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
            for (Character character : Ragnarok.stage.CHARACTERS)
            {
                // Do not collide with ourselves.
                if (this == character)
                {
                    continue;
                }

                if (body.intersects(character.body))
                {
                    collided = true;

                    // To calculate finalVelocity later we will need the current velocity.
                    // It will be changed after the collision resolution so we save a copy now.
                    Vector v1 = velocity;
                    Vector v2 = character.velocity;
                    resolveCollisionWith(character.body, axis);

                    float m1 = getMass();
                    float m2 = character.getMass();
                    Vector momentum1 = v1.scale(m1);
                    Vector momentum2 = v2.scale(m2);
                    float massSum = m1 + m2;

                    // As per the sticky collision formula which conserves momentum:
                    // m1*v1 + m2*v2 = (m1 + m2)*vf
                    Vector finalVelocity = momentum1.add(momentum2).scale(1 / massSum);
                    velocity = finalVelocity;
                    character.velocity = finalVelocity;
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
        tileBounds = new BoundingBox(collisionRectangles.get(type));
        tileBounds.offset(row * TILE_WIDTH_PX, column * TILE_HEIGHT_PX);
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void onCollide() {}

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void onBottomOn() {}
}
