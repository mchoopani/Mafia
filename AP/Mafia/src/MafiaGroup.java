public class MafiaGroup extends Player{
    public MafiaGroup(String name) {
        super(name);
    }
    // mafia usually vote on night, but silencer also silent anyone.
    @Override
    public void playRoleOnNight(Player targetPlayer) {
        this.setLastVotee(targetPlayer);
    }
}
