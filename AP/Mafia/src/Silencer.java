public class Silencer extends MafiaGroup{
    public Silencer(String name) {
        super(name);
    }
    public void silent(Player player){
        player.isSilent = true;
    }
}
