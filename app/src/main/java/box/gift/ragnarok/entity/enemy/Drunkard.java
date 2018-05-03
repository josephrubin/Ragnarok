package box.gift.ragnarok.entity.enemy;

import android.content.res.AssetManager;

import java.io.IOException;

import box.gift.ragnarok.myai.EnemyAI;
import box.shoe.gameutils.AABB;
import box.shoe.gameutils.ai.AILoader;

public class Drunkard extends Enemy
{
    public Drunkard(float centerX, float centerY, AssetManager assets) throws IOException
    {
        super(AABB.fromCenter(centerX, centerY, 10),
                new EnemyAI(AILoader.fromXml(assets, "ai/drunkard.xml")));
    }
}
