public class BulletProof extends VillagerGroup{
    private int hearts = 1 ;
    public BulletProof(String name) {
        super(name);
        hasRoleOnNight = true;
    }

    public boolean haveEnoughHearts() {
        return hearts == 1;
    }

    public void reduceHearts(){
        hearts--;
    }

}
