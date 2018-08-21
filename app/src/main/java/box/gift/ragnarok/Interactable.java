package box.gift.ragnarok;

import box.gift.ragnarok.entity.Character;
import box.gift.ragnarok.interact.InteractTile;

public interface Interactable
{
    void onCharacter(Character character);
    void hitInteractTile(InteractTile interactTile);
    void hitCollision();
}
