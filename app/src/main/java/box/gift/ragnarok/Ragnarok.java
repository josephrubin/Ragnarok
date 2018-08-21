package box.gift.ragnarok;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.MotionEvent;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import box.gift.ragnarok.combat.hitbox.AbstractHitbox;
import box.gift.ragnarok.constant.DamageConstant;
import box.gift.ragnarok.constant.ForceConstant;
import box.gift.ragnarok.constant.LevelSources;
import box.gift.ragnarok.constant.StunDuration;
import box.gift.ragnarok.constant.tilemap.CustomProperty;
import box.gift.ragnarok.constant.tilemap.LayerConstant;
import box.gift.ragnarok.entity.enemy.Bee;
import box.gift.ragnarok.entity.enemy.Boss;
import box.gift.ragnarok.entity.enemy.Charger;
import box.gift.ragnarok.entity.enemy.Drunkard;
import box.gift.ragnarok.entity.enemy.Enemy;
import box.gift.ragnarok.entity.enemy.Rat;
import box.gift.ragnarok.interact.Conveyor;
import box.gift.ragnarok.interact.Gunner;
import box.gift.ragnarok.interact.Ice;
import box.gift.ragnarok.interact.Shooter;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.Direction;
import box.gift.ragnarok.entity.enemy.Knight;
import box.gift.ragnarok.entity.Character;
import box.gift.ragnarok.entity.CollisionEntity;
import box.gift.ragnarok.entity.Player;
import box.gift.ragnarok.interact.Goo;
import box.gift.ragnarok.interact.InteractTile;
import box.gift.ragnarok.interact.Poison;
import box.gift.ragnarok.interact.Spike;
import box.gift.ragnarok.interact.Spring;
import box.shoe.gameutils.CollectionUtils;
import box.shoe.gameutils.Vector;
import box.shoe.gameutils.camera.Camera;
import box.shoe.gameutils.camera.FollowCamera;
import box.shoe.gameutils.camera.SectionCamera;
import box.shoe.gameutils.input.HoldZone;
import box.shoe.gameutils.input.Joystick;
import box.shoe.gameutils.input.JoystickZone;
import box.shoe.gameutils.input.SwipeZone;
import box.shoe.gameutils.engine.Game;
import box.shoe.gameutils.input.VectorTouchable;
import box.shoe.gameutils.map.TileMap;
import box.shoe.gameutils.map.TileMapLoader;

/**
 * Created by Joseph on 3/21/2018.
 */

public class Ragnarok implements Game
{
    public static final int TILE_SIZE_PX = 16;

    // ________
    // DISPLAY.

    // Our main visual bounds will conform to this ratio.
    private final float ASPECT_RATIO = 16f / 9f;
    // In the center of the screen, the portion of the game that we would like to display is scaled to fit into these
    // visual bounds. Conforms to ASPECT_RATIO.
    private Rect mainVisualBounds;

    // Save the dimensions of the Screen.
    private int screenWidth;
    private int screenHeight;

    // The portion of the game that we will be showing. This should also conform to ASPECT_RATIO so that things are not
    // stretched in an uneven way when we fit this to mainVisualBounds.
    private float desiredFocusGamePortionHeight;
    private float desiredFocusGamePortionWidth;

    private Context applicationContext;

    // ________________
    // STAGE (Globals).
    public static Stage stage;

    // ______
    // INPUT.
    private boolean useStaticJoystick = true;

    private RectF attackControlBounds;
    private SwipeZone attackControl; //TODO: recycle
    private int attackControlVelocityUnits = 1;
    private float attackControlVelocityThreshold = 0.5f;

    private HoldZone defendControl;
    private int defendHoldUpdates = 16;

    private Paint joystickPaint;
    public static final float DEADZONE_PERCENT = 0.25f;
    public static final float SLOWZONE_PERCENT = 0.4f;

    private VectorTouchable moveControl;
    private BoundingBox moveControlBounds;
    private float joystickRadius;

    private List<InteractTile> interactTiles;
    private List<Character> characters;

    // ______
    // PAINT.
    private Paint tilePaint;

    // Player.
    private Player player;

    // Camera.
    private Camera camera;

    private Bitmap collisionBackground;
    private Bitmap interactBackground;

    public Ragnarok(Context applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    // ______
    // SETUP.

    @Override
    public void onStart(int screenWidth, int screenHeight)
    {
        mainVisualBounds = new Rect();
        onScreenSizeChanged(screenWidth, screenHeight, 0, 0);

        SparseArray<RectF> collisionRectangles = new SparseArray<>();
        collisionRectangles.append(4, new RectF(0, 0, 16, 16));

        collisionRectangles.append(5, new RectF(0, 0, 8, 16));
        collisionRectangles.append(7, new RectF(0, 0, 16, 8));
        collisionRectangles.append(3, new RectF(8, 0, 16, 16));
        collisionRectangles.append(1, new RectF(0, 8, 16, 16));

        collisionRectangles.append(8, new RectF(0, 0, 8, 8));
        collisionRectangles.append(6, new RectF(8, 0, 16, 8));
        collisionRectangles.append(2, new RectF(0, 8, 8, 16));
        collisionRectangles.append(0, new RectF(8, 8, 16, 16));

        CollisionEntity.collisionRectangles = collisionRectangles;
        CollisionEntity.TILE_WIDTH_PX = 16;
        CollisionEntity.TILE_HEIGHT_PX = 16;

        // We draw our tiles with no special scaling effects for a clean pixel look.
        tilePaint = new Paint();
        tilePaint.setDither(false);
        tilePaint.setAntiAlias(false);

        joystickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        player = new Player(0, 0);
        player.body.offset(30, 30);

        interactTiles = new LinkedList<>();
        characters = new LinkedList<>();

        characters.add(player);

        startMap(loadTileMap(LevelSources.STARTING_LEVEL));
    }

    @Override
    public void onScreenSizeChanged(int newScreenWidth, int newScreenHeight, int oldScreenWidth, int oldScreenHeight)
    {
        //TODO: Take density into account when deciding how much to zoom in.
        DisplayMetrics displayMetrics = applicationContext.getResources().getDisplayMetrics();
        float xDensity = displayMetrics.xdpi;
        float yDensity = displayMetrics.ydpi;

        screenWidth = newScreenWidth;
        screenHeight = newScreenHeight;

        fitAspectRatio(ASPECT_RATIO, newScreenWidth, newScreenHeight, mainVisualBounds);

        desiredFocusGamePortionHeight = 9 * TILE_SIZE_PX;
        if (mainVisualBounds.height() > desiredFocusGamePortionHeight * 2)
        {
            //todo: maybe based on overall inches (include res) not just on px
            //todo: idea is each tile should be displayed at at least 2 scale (2px for each px, maybe 2dip)
            //todo: but since we have more room then scale using some of it, and show more tiles using some of it.
        }
        desiredFocusGamePortionWidth = ASPECT_RATIO * desiredFocusGamePortionHeight;

        attackControlBounds =
                new RectF(screenWidth / 2, 0, screenWidth, screenHeight);
        attackControl = new SwipeZone(attackControlBounds, attackControlVelocityUnits);

        defendControl = new HoldZone(attackControlBounds);

        if (useStaticJoystick)
        {
            joystickRadius = mainVisualBounds.height() / 4.8f;
            moveControlBounds
                    = new BoundingBox(0, 0, 2 * joystickRadius, 2 * joystickRadius);
            moveControlBounds.offsetLeftTo(mainVisualBounds.height() / 10);
            moveControlBounds.offsetBottomTo(9 * mainVisualBounds.height() / 10);
            moveControl = new Joystick(moveControlBounds);
        }
        else
        {
            joystickRadius = mainVisualBounds.height() / 7.5f;//4.8f;
            moveControlBounds
                    = new BoundingBox(0, 0, screenWidth / 2, screenHeight);
            moveControl = new JoystickZone(moveControlBounds, joystickRadius * 2, joystickRadius * 2);
        }
    }

    // _______
    // UPDATE.

    @Override
    public void update()
    {
        handleInput();

        // Kill Characters.
        Iterator<Character> iterator = characters.iterator();
        while (iterator.hasNext())
        {
            Character character = iterator.next();
            // A Character dies if its health drops to 0 or lower.
            if (character.getCurrentHealth() <= 0)
            {
                character.cleanup();
                iterator.remove();
            }
        }

        // Movement and collisions.
        CollectionUtils.updateAll(characters);
        CollectionUtils.updateAll(interactTiles);

        // Collect Hitboxes.
        Collection<AbstractHitbox> hitboxes = new LinkedList<>();
        for (Character character : characters)
        {
            // Each Character can have Hitboxes.
            hitboxes.addAll(character.getHitboxes());
        }
        for (InteractTile interactTile : interactTiles)
        {
            // Each InteractTile can have Hitboxes.
            hitboxes.addAll(interactTile.getHitboxes());
        }

        // Call Hitbox onCharacter(Character).
        for (Character character : characters)
        {
            for (AbstractHitbox hitbox : hitboxes)
            {
                if (character.body.intersects(hitbox.body))
                {
                    hitbox.onCharacter(character);
                }
            }
        }

        // Inter Character interactions.
        //TODO: fori loops inefficient for LinkedList
        for (int i = 0; i < characters.size(); i++)
        {
            Character char1 = characters.get(i);
            for (int j = i; j < characters.size(); j++)
            {
                Character char2 = characters.get(j);
                if (char1.equals(char2))
                {
                    continue;
                }

                if (char1.body.touches(char2.body))
                {
                    Character playerChar = null;
                    Character otherChar = null;
                    if (player.equals(char1))
                    {
                        playerChar = char1;
                        otherChar = char2;
                    }
                    else if (player.equals(char2))
                    {
                        playerChar = char2;
                        otherChar = char1;
                    }

                    if (playerChar != null)
                    {
                        if (playerChar.requestDamage(DamageConstant.ENEMY_BUMP, Team.UNALIGNED))
                        {
                            playerChar.applyForce(Direction.fromTheta(otherChar.vectorTo(playerChar).getTheta()).VECTOR.scale(ForceConstant.DAMAGE_KNOCKBACK_DEFAULT));
                            playerChar.addStatusEffectForDuration(StatusEffect.STUN, StunDuration.DAMAGE_TAKEN_DEFAULT);
                        }
                    }
                }
            }
        }

        doInteracts();

        applyFriction();
    }

    private void handleInput()
    {
        if (moveControl.isActive())
        {
            Vector moveVector = moveControl.getActiveTouchVector();

            if (!useStaticJoystick)
            {
                // Move the joystick to keep the input finger inside of it.
                if (moveVector.getMagnitude() > 1)
                {
                    float touchOnRadiusX = (float) (Math.cos(moveVector.getTheta()) * joystickRadius);
                    float touchOnRadiusY = (float) (Math.sin(moveVector.getTheta()) * joystickRadius);

                    float touchX = (touchOnRadiusX * moveVector.getMagnitude());
                    float touchY = (touchOnRadiusY * moveVector.getMagnitude());

                    ((JoystickZone) moveControl).getCurrentJoystickBounds().offset(touchX - touchOnRadiusX, touchY - touchOnRadiusY);
                }

                // We cap the input at 1 for movement since input outside of the joystick is full (magnitude = 1) input.
                if (moveVector.getMagnitude() > 1)
                {
                    moveVector = moveVector.unit();
                }
            }
            player.onJoystickInput(moveVector);
        }
        defendControl.update();
        if (defendControl.isActive())
        {
            if (defendControl.getTouchDurationUpdates() > defendHoldUpdates)
            {
                System.out.println("DEFEND");
                defendControl.resetUntilPointersLeave();
            }
        }
        if (attackControl.isActive())
        {
            Vector swipeVelocity = attackControl.getActiveSwipeVelocity();
            // Only register an attack if it's above the threshold so we do not get accidental swipes.
            if (swipeVelocity.getMagnitude() > attackControlVelocityThreshold)
            {
                player.onAttackInput(Direction.fromTheta(swipeVelocity.getTheta()));
                // This swipe was used, so consume the event.
                attackControl.consumeLastSwipe();
            }
        }
    }

    private void applyFriction()
    {
        for (Character character : characters)
        {
            // Friction = mu * mass.
            float mu = ForceConstant.GLOBAL_FRICTION_COEFFICIENT * (1 / character.getSlip());
            character.applyForce(character.velocity.scale(mu * character.getMass()));
        }
    }

    private void doInteracts()
    {
        for (InteractTile interactTile : interactTiles)
        {
            for (Character character : characters)
            {
                if (interactTile.body.intersects(character.body))
                {
                    interactTile.on(character);
                }
                if (interactTile.body.touches(character.body))
                {
                    interactTile.touch(character);
                }
            }
        }
    }

    // _______
    // RENDER.

    @Override
    public void render(Resources resources, Canvas canvas)
    {
        // Background.
        canvas.drawColor(Color.WHITE);

        // Before rolling the camera, save the canvas state so we can restore it later before drawing the HUD.
        canvas.save();
        camera.roll(canvas);

        // ________________
        // CAMERA ROLL: ON.
        // Draw anything that moves relative to the game world.

        canvas.drawBitmap(collisionBackground, 0, 0, tilePaint);
        canvas.drawBitmap(interactBackground, 0, 0, tilePaint);

        CollectionUtils.renderAll(interactTiles, resources, canvas);
        CollectionUtils.renderAll(characters, resources, canvas);

        // Now we restore the canvas to unroll the camera.
        canvas.restore();

        // _________________
        // CAMERA ROLL: OFF.
        // Draw things which have a fixed space on the screen or move relative to the screen, like the HUD.

        // Joystick.
        if (moveControl.isActive() || useStaticJoystick)
        {
            RectF bounds;
            if (useStaticJoystick)
            {
                bounds = moveControlBounds;
            }
            else
            {
                bounds = ((JoystickZone) moveControl).getCurrentJoystickBounds();
            }
            // Bounds.
            joystickPaint.setColor(Color.argb(30, 0, 0, 0));
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), joystickRadius, joystickPaint);
            /*
            // Slow zone.
            joystickPaint.setColor(Color.YELLOW);
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), joystickRadius * SLOWZONE_PERCENT, joystickPaint);
            // Dead zone.
            joystickPaint.setColor(Color.RED);
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), joystickRadius * DEADZONE_PERCENT, joystickPaint);
            */
            // Thumb. //TODO: touch vector should be interpolated to slightly reduce jitter.
            joystickPaint.setColor(Color.argb(30, 0, 0, 0));
            if (moveControl.isActive())
            {
                Vector touchVector = moveControl.getActiveTouchVector();
                if (touchVector.getMagnitude() > 1)
                {
                    touchVector = touchVector.unit();
                }
                float inputOffsetX = joystickRadius * touchVector.getX();
                float inputOffsetY = joystickRadius * touchVector.getY();
                float thumbCenterX = bounds.centerX() + inputOffsetX;
                float thumbCenterY = bounds.centerY() + inputOffsetY;
                canvas.drawCircle(thumbCenterX, thumbCenterY, 163, joystickPaint);
                // Connector.
                joystickPaint.setStrokeWidth(20); //TODO: if permanent, should be based on screen size.
                canvas.drawLine(bounds.centerX(), bounds.centerY(), thumbCenterX, thumbCenterY, joystickPaint);
            }
        }

        // Health. Just a number, todo: temporary
        Paint temp = new Paint(Paint.ANTI_ALIAS_FLAG);
        temp.setTextSize(80);
        canvas.drawText(String.valueOf(player.getCurrentHealth()), 50, 100, temp);

        /*
        // Bars.
        temp.setColor(Color.BLACK);
        canvas.drawRect(0, 0, mainVisualBounds.left, screenHeight, temp);
        canvas.drawRect(mainVisualBounds.right, 0, screenWidth, screenHeight, temp);
        */
    }

    private TileMap loadTileMap(String path)
    {
        try
        {
            return TileMapLoader.fromXml(applicationContext.getAssets(), path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("Unable to load file at " + path);
        }
    }

    private void startMap(TileMap tileMap)
    {
        // Generate collision data.
        byte[][] collision = tileMap.generateLayerRelativeTileGidGrid(LayerConstant.COLLISION_LAYER_NAME);

        // Generate bitmaps.
        collisionBackground = tileMap.generateLayerBitmap(LayerConstant.COLLISION_LAYER_NAME);
        interactBackground = tileMap.generateLayerBitmap(LayerConstant.INTERACT_LAYER_NAME);

        // Create the InteractTiles from the interact layer.
        byte[][] interactTileRelativeGidGrid = tileMap.generateLayerRelativeTileGidGrid(LayerConstant.INTERACT_LAYER_NAME);
        for (int i = 0; i < tileMap.TILES_PER_ROW; i++)
        {
            for (int j = 0; j < tileMap.TILES_PER_COLUMN; j++)
            {
                byte gid = interactTileRelativeGidGrid[i][j];
                BoundingBox tileBounds = new BoundingBox(i * tileMap.TILE_WIDTH_PX, j * tileMap.TILE_HEIGHT_PX,
                        (i + 1) * tileMap.TILE_WIDTH_PX, (j + 1) * tileMap.TILE_HEIGHT_PX);
                switch (gid)
                {
                    //TODO: constants for this...
                    case 0:
                        interactTiles.add(new Spring(tileBounds));
                        break;

                    case 1:
                        interactTiles.add(new Spike(tileBounds));
                        break;

                    case 2:
                        interactTiles.add(new Goo(tileBounds));
                        break;

                    case 3:
                        interactTiles.add(new Shooter(tileBounds));
                        break;

                    case 4:
                        interactTiles.add(new Poison(tileBounds));
                        break;

                    case 5:
                        interactTiles.add(new Ice(tileBounds)); //TODO: ice effect doesnt do anything yet
                        break;

                    case 7:
                        interactTiles.add(new Gunner(tileBounds));
                        break;

                    case 8:
                        interactTiles.add(new Conveyor(tileBounds, Direction.WEST));
                        break;

                    case 9:
                        interactTiles.add(new Conveyor(tileBounds, Direction.NORTH));
                        break;

                    case 10:
                        interactTiles.add(new Conveyor(tileBounds, Direction.EAST));
                        break;

                    case 11:
                        interactTiles.add(new Conveyor(tileBounds, Direction.SOUTH));
                        break;
                }
            }
        }

        // Create the Enemies from the enemy layer.
        byte[][] enemyTileRelativeGidGrid = tileMap.generateLayerRelativeTileGidGrid(LayerConstant.ENEMY_LAYER_NAME);
        Enemy currentEnemy = null;
        for (int i = 0; i < tileMap.TILES_PER_ROW; i++)
        {
            for (int j = 0; j < tileMap.TILES_PER_COLUMN; j++)
            {
                byte gid = enemyTileRelativeGidGrid[i][j];
                if (gid == TileMapLoader.EMPTY_TILE)
                {
                    continue;
                }
                float tileCenterX = (i + 0.5f) * tileMap.TILE_WIDTH_PX;
                float tileCenterY = (j + 0.5f) * tileMap.TILE_HEIGHT_PX;
                switch (gid)
                {
                    //TODO: constants for this... in some sort of EnemyConstant file
                    case 0:
                        try
                        {
                            currentEnemy = new Knight(tileCenterX, tileCenterY, applicationContext.getAssets());
                        }catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try
                        {
                            currentEnemy = new Rat(tileCenterX, tileCenterY, applicationContext.getAssets());
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try
                        {
                            currentEnemy = new Charger(tileCenterX, tileCenterY, applicationContext.getAssets());
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        try
                        {
                            currentEnemy = new Drunkard(tileCenterX, tileCenterY, applicationContext.getAssets());
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        try
                        {
                            currentEnemy = new Boss(tileCenterX, tileCenterY, applicationContext.getAssets());
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        try
                        {
                            currentEnemy = new Bee(tileCenterX, tileCenterY, applicationContext.getAssets());
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                }
                if (currentEnemy == null)
                {
                    throw new IllegalArgumentException("No Enemy represented by this gid: " + gid);
                }
                else
                {
                    characters.add(currentEnemy);
                    currentEnemy.spawn(player);
                }
            }
        }

        // Make the target player camera with outer bounds so it can't on past the map edge.
        if (tileMap.hasProperty(CustomProperty.TILE_MAP_IS_DUNGEON))
        {
            camera = new SectionCamera(player, desiredFocusGamePortionWidth, desiredFocusGamePortionHeight, mainVisualBounds);
        }
        else
        {
            RectF outerBounds = new RectF(0, 0, tileMap.TOTAL_WIDTH_PX, tileMap.TOTAL_HEIGHT_PX);
            camera = new FollowCamera(player, desiredFocusGamePortionWidth, desiredFocusGamePortionHeight, outerBounds, mainVisualBounds);
        }

        // Setup globals.
        Stage currentStage = new Stage();
        currentStage.PLAYER = player;
        currentStage.CHARACTERS = characters;
        currentStage.INTERACT_TILES = interactTiles;
        currentStage.COLLISION = collision;
        stage = currentStage;
    }

    @Override
    public void onTouch(MotionEvent touchEvent)
    {
        moveControl.processTouch(touchEvent);
        defendControl.processTouch(touchEvent);
        attackControl.processTouch(touchEvent);
    }

    @Override
    public void onStop()
    {

    }

    @Override
    public int getTargetUpdatesPerSecond()
    {
        return 40;
    }

    private void fitAspectRatio(double aspectRatio, int maxWidth, int maxHeight, Rect bounds)
    {
        RectF floatBounds = new RectF();

        double gameWidth;
        double gameHeight;
        if ((float) maxWidth / maxHeight > aspectRatio)
        {
            gameHeight = maxHeight;
            gameWidth = aspectRatio * gameHeight;
            double left = (maxWidth / 2) - (gameWidth / 2);
            floatBounds.set((float) left, 0, (float) (left + gameWidth), (float) gameHeight);
        }
        else
        {
            gameWidth = maxWidth;
            gameHeight = gameWidth / aspectRatio;
            double top = (maxHeight / 2) - (gameHeight / 2);
            floatBounds.set(0, (float) top, (float) gameWidth, (float) (top + gameHeight));
        }

        floatBounds.round(bounds);
    }
}
