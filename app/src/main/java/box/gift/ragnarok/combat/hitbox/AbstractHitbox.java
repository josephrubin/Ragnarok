package box.gift.ragnarok.combat.hitbox;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.RestrictTo;

import box.gift.ragnarok.Team;
import box.gift.ragnarok.entity.Character;
import box.gift.ragnarok.interact.InteractTile;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Entity;
import box.shoe.gameutils.Renderable;

public abstract class AbstractHitbox extends Entity implements Renderable, Finishable
{
    public final Team team;

    public static final Integer UNLIMITED = -1;

    private boolean finishRequested;
    
    private int updatesLeft;
    private int targetsLeft;

    private final boolean unlimitedUpdates;
    private final boolean unlimitedTargets;

    private static final Paint DEFAULT_HITBOX_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    static
    {
        DEFAULT_HITBOX_PAINT.setColor(Color.RED);
    }

    public AbstractHitbox(BoundingBox body, Team sourceTeam, int updatesLeft, int targetsLeft)
    {
        super(body);

        team = sourceTeam;

        finishRequested = false;

        unlimitedUpdates = (updatesLeft == UNLIMITED);
        unlimitedTargets = (targetsLeft == UNLIMITED);

        this.updatesLeft = updatesLeft;
        this.targetsLeft = targetsLeft;
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void targetHit()
    {
        targetsLeft--;
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    protected void requestFinish()
    {
        finishRequested = true;
    }

    @Override
    public boolean isFinished()
    {
        return finishRequested || (!unlimitedUpdates && updatesLeft <= 0) || (!unlimitedTargets && targetsLeft <= 0);
    }

    public abstract void onCharacter(Character character);
    public abstract void onInteractTile(InteractTile interactTile);
    public abstract void onCollision();

    @Override
    public void update()
    {
        super.update();
        updatesLeft--;
    }

    @Override
    public void render(Resources resources, Canvas canvas)
    {
        canvas.drawRect(body, DEFAULT_HITBOX_PAINT);
    }
}
