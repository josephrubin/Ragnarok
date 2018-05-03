package box.gift.ragnarok.android;

import android.support.annotation.NonNull;

import box.gift.ragnarok.Ragnarok;
import box.shoe.gameutils.GameActivity;
import box.shoe.gameutils.engine.Game;

/**
 * Created by Joseph on 3/21/2018.
 */

public class ActivityRagnarok extends GameActivity
{
    @NonNull
    @Override
    protected Game provideNewGame()
    {
        return new Ragnarok(getApplicationContext());
    }
}
