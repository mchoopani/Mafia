public class BulletProof extends VillagerGroup{
    private int hearts = 1 ;
    public BulletProof(String name) {
        super(name);
    }

    public boolean haveEnoughHearts() {
        return hearts == 1;
    }

    public void reduceHearts(){
        hearts--;
    }

}
