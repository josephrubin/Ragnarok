package box.gift.ragnarok.entity.enemy;

import android.content.res.AssetManager;

import java.io.IOException;

import box.gift.ragnarok.combat.weapon.Sword;
import box.gift.ragnarok.constant.StatisticDefaultConstant;
import box.gift.ragnarok.ai.EnemyAI;
import box.shoe.gameutils.BoundingBox;
import box.shoe.gameutils.ai.AILoader;

public class Knight extends Enemy
{
    public Knight(float centerX, float centerY, AssetManager assets) throws IOException
    {
        super(BoundingBox.fromCenter(centerX, centerY, StatisticDefaultConstant.CHARACTER_EDGE_LENGTH),
                new EnemyAI(AILoader.fromXml(assets, "ai/knight.xml")));

        wieldOffense(new Sword());
    }
}
