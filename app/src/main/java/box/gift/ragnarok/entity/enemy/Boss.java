package box.gift.ragnarok.entity.enemy;

import android.content.res.AssetManager;

import java.io.IOException;

import box.gift.ragnarok.ai.EnemyAI;
import box.gift.ragnarok.combat.weapon.Shotgun;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.ai.AILoader;

public class Boss extends Enemy
{
    public Boss(float centerX, float centerY, AssetManager assets) throws IOException
    {
        super(BoundingBox.fromCenter(centerX, centerY, 28),
                new EnemyAI(AILoader.fromXml(assets, "ai/boss.xml")));
        health = 16;
        moveSpeed = 0.5f;
        mass = 3;
        wieldOffense(new Shotgun());
    }
}
