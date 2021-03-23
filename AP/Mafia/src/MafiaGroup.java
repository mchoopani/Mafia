public class MafiaGroup extends Player{
    public MafiaGroup(String name) {
        super(name);
    }

    @Override
    public void playRoleOnNight(Player targetPlayer) {
        this.giveVote(targetPlayer);
    }
}
