package box.gift.ragnarok;

import java.util.List;

import box.gift.ragnarok.entity.Character;
import box.gift.ragnarok.entity.Player;
import box.gift.ragnarok.interact.InteractTile;

public class Stage //TODO: scene, world, episode, arena, stage ?
{
    public Player PLAYER;

    public List<Character> CHARACTERS;
    public List<InteractTile> INTERACT_TILES;

    public byte[][] COLLISION;
}
