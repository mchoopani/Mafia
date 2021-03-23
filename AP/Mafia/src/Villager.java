public class Villager extends VillagerGroup{
    public Villager(String name) {
        super(name);
        hasRoleOnNight = false;
    }

    @Override
    public void playRoleOnNight(Player targetPlayer) {
        super.playRoleOnNight(targetPlayer);
    }
}
