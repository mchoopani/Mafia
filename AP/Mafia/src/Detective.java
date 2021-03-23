public class Detective extends VillagerGroup{
    public Detective(String name) {
        super(name);
        hasRoleOnNight = true;
    }
    public boolean isMafia(Player player){
        if (player instanceof GodFather)
            return false;
        return player instanceof MafiaGroup;
    }
    @Override
    public void playRoleOnNight(Player targetPlayer) {
        if (isMafia(targetPlayer)){
            System.out.println("YES");
            return;
        }
        System.out.println("NO");
    }
}
