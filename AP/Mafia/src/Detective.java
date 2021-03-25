public class Detective extends VillagerGroup{
    boolean getReportRecently = false;
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
        if (targetPlayer.isKilled){
            System.out.println("suspect is dead");
            return;
        }
        if (!getReportRecently) {
            getReportRecently = true;
            if (isMafia(targetPlayer)){
                System.out.println("YES");
                return;
            }
            System.out.println("NO");
        }
        else
            System.out.println("detective has already asked");
    }
}
