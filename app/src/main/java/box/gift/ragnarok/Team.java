package box.gift.ragnarok;

/**
 * A tag which is applied to damage sources
 * to determine which Characters will be effected.
 */

public class Team
{
    public static final Team UNALIGNED = new Team();

    /**
     * A tag which is applied to damage sources
     * to determine which Characters will be effected,
     * and which is applied to Characters which determines
     * which sources of damage they will be affected by.
     */
    public static class AssignableTeam extends Team
    {
        public static final AssignableTeam FRIENDLY = new AssignableTeam();
        public static final AssignableTeam HOSTILE = new AssignableTeam();
    }
}
