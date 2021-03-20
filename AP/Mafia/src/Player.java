public class Player {
    private String name;
    public boolean isKilled = false;
    public boolean isSilent = false;
    public Player(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
