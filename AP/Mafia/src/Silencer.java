public class Silencer extends MafiaGroup{
    public Silencer(String name) {
        super(name);
        hasRoleOnNight = true;
    }
    public void silent(Player player){
        player.isSilent = true;
    }

    @Override
    public void playRoleOnNight(Player targetPlayer) {
        silent(targetPlayer);
    }
}
