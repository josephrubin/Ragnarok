package box.gift.ragnarok.entity;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import box.gift.ragnarok.StatusEffect;
import box.gift.ragnarok.Team;
import box.gift.ragnarok.combat.weapon.Homer;
import box.gift.ragnarok.combat.weapon.Sword;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Direction;
import box.gift.ragnarok.Ragnarok;
import box.shoe.gameutils.Renderable;
import box.shoe.gameutils.Vector;

/**
 * Created by Joseph on 3/21/2018.
 */

public class Player extends Character implements Renderable
{
    public static final float WIDTH = 12;
    public static final float HEIGHT = 12;

    public static final float SPEED = 0.5f;

    private Vector joystickInput = Vector.ZERO;

    public Player(float x, float y)
    {
        super(new BoundingBox(x, y, WIDTH, HEIGHT), Team.AssignableTeam.FRIENDLY);
        this.health = 12;
        wieldOffense(new Sword());

        INTERPOLATABLE_SERVICE.addMember(this);
    }

    public void onJoystickInput(Vector joystickInput)
    {
        this.joystickInput = joystickInput;
    }

    public void onAttackInput(Direction attackDirection)
    {
        if (!hasStatusEffect(StatusEffect.STUN))
        {
            face(attackDirection);
            requestAttackOffense();
        }
    }

    @Override
    protected void updateAcceleration()
    {
        // Check to see if the input Vector is equal to Vector.ZERO first
        // because you cannot create a unit vector from Vector.ZERO.
        if (!joystickInput.equals(Vector.ZERO))
        {
            // Unless something is applying the STUN StatusEffect,
            // we can make the player move by applying a force based on the joystick input.
            if (!hasStatusEffect(StatusEffect.STUN))
            {
                //TODO: customize deadzone;
/*
                double joystickInputTheta = joystickInput.getTheta();
                int sixteenth = (int) (joystickInputTheta * 8 / Math.PI);
                Vector forceDirection;
                switch (sixteenth)
                {
                    case 0: default:
                        forceDirection = Vector.EAST;
                        break;
                    case -1: case -2:
                        forceDirection = Vector.NORTH_EAST;
                        break;
                    case -3: case -4:
                        forceDirection = Vector.NORTH;
                        break;
                    case -5: case -6:
                        forceDirection = Vector.NORTH_WEST;
                        break;
                    case -7: case 7:
                        forceDirection = Vector.WEST;
                        break;
                    case 6: case 5:
                        forceDirection = Vector.SOUTH_WEST;
                        break;
                    case 4: case 3:
                        forceDirection = Vector.SOUTH;
                        break;
                    case 2: case 1:
                        forceDirection = Vector.SOUTH_EAST;
                        break;
                }*/
                Vector force = joystickInput.unit().scale(SPEED);//forceDirection.scale(SPEED);
                if (joystickInput.getMagnitude() < Ragnarok.DEADZONE_PERCENT)
                {
                    force = Vector.ZERO;
                }
                else if (joystickInput.getMagnitude() < Ragnarok.SLOWZONE_PERCENT)
                {
                    force = force.scale(0.4f);
                }

                applyForce(force);

                facing = Direction.fromTheta(joystickInput.getTheta());
            }
        }

        joystickInput = Vector.ZERO;

        super.updateAcceleration();
    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {
        super.render(resources, canvas);
        Paint marker = new Paint(Paint.ANTI_ALIAS_FLAG);
        marker.setColor(Color.BLUE);
        canvas.drawRect(display.left + 5, display.top + 5, display.right - 5, display.bottom - 5, marker);
    }
}
