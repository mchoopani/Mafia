public class VillagerGroup extends Player{
    public VillagerGroup(String name) {
        super(name);
    }
    // villagers don't wake up at night except some roles.
    @Override
    public void playRoleOnNight(Player targetPlayer) {
        /* do nothing */
    }
}
