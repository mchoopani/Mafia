public class Joker extends Player{
    public int x = 0;
    public Joker(String name) {
        super(name);
        hasRoleOnNight = false;
    }

    @Override
    public void playRoleOnNight(Player targetPlayer) {

    }
}
