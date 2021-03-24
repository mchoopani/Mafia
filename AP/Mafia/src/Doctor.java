public class Doctor extends VillagerGroup {
    public Doctor(String name) {
        super(name);
        hasRoleOnNight = true;
    }
    public void savePlayer(Player player){
        player.SavedByDoctor = true;

    }

    @Override
    public void playRoleOnNight(Player targetPlayer) {
        this.savePlayer(targetPlayer);
    }
}
