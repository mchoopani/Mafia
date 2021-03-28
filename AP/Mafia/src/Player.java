public abstract class Player {
    // there isn't necessary description, va gar hast degar hosele i nest :)
    private String name;
    public boolean isKilled = false;
    public boolean isSilent = false;
    public boolean SavedByDoctor = false;
    public boolean hasRoleOnNight;
    public int voteNum = 0;
    protected int hearts = 0 ;
    public Player lastVotee = null;

    public void setLastVotee(Player lastVotee) {
        this.lastVotee = lastVotee;
    }

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
    public void kill(Player[] players){
        if (!SavedByDoctor)
            if (!haveEnoughHearts()) {
                isKilled = true;
                if (this instanceof Informer)
                    System.out.println(((Informer) this).inform(players));
            }
            else
                hearts--;
    }
    public void kill(){
        if (!SavedByDoctor)
            if (!haveEnoughHearts()) {
                isKilled = true;
            }
            else
                hearts--;
    }
    public abstract void playRoleOnNight(Player targetPlayer);
    public boolean haveEnoughHearts(){
        return hearts==1;
    }
}
