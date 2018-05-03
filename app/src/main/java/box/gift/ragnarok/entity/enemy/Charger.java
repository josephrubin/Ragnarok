package box.gift.ragnarok.entity.enemy;

import android.content.res.AssetManager;
import android.graphics.RectF;

import java.io.IOException;

import box.gift.ragnarok.constant.StatisticDefaultConstant;
import box.gift.ragnarok.entity.Player;
import box.gift.ragnarok.myai.EnemyAI;
import box.shoe.gameutils.AABB;
import box.shoe.gameutils.ai.AILoader;

public class Charger extends Enemy
{
    public Charger(float centerX, float centerY, AssetManager assets) throws IOException
    {
        super(AABB.fromCenter(centerX, centerY, StatisticDefaultConstant.CHARACTER_EDGE_LENGTH),
                new EnemyAI(AILoader.fromXml(assets, "ai/charger.xml"))); //todo: constants file for storing ai paths
        moveSpeed = 0.2f;
        slip = 2.5f;
    }
}