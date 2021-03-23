public abstract class Player {
    private String name;
    public boolean isKilled = false;
    public boolean isSilent = false;
    public boolean isSavedByDoctor = false;
    public boolean hasRoleOnNight;
    public int voteNum = 0;
    public Player(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean giveVote(Player player){
        if (!this.isSilent){
            player.getVote();
            return true;
        }
        return false;
    }
    public void getVote(){
        this.voteNum++;
    }
    public void resetVote(){
        this.voteNum = 0;
    }
    public void kill(){
        isKilled = true;
    }
    public abstract void playRoleOnNight(Player targetPlayer);
}
