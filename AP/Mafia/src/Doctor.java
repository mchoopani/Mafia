public class Doctor extends VillagerGroup {
    public Doctor(String name) {
        super(name);
    }
    public void savePlayer(Player player){
        player.isKilled = false;
    }
}
