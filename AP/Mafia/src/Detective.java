public class Detective extends VillagerGroup{
    public Detective(String name) {
        super(name);
    }
    public boolean isMafia(Player player){
        if (player instanceof GodFather)
            return false;
        return player instanceof MafiaGroup;
    }
}
