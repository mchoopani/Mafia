public class Informer extends VillagerGroup{
    public Informer(String name) {
        super(name);
        hasRoleOnNight = false;
    }
    public String inform(Player[] players){
        int randomIndicator = (int) (Math.random() * 4);
        String output = null;
        switch (randomIndicator){
            case 0:
                for(Player player : players)
                    if (player instanceof MafiaGroup && !player.isKilled) {
                        output = "There is a mafia who’s name starts with " + player.getName().charAt(0);
                        break;
                    }
                if (output != null) return output;
            case 1:
                for(Player player : players) {
                    if (player == this) continue;
                    if (player instanceof VillagerGroup && player.voteNum != 0) {
                        output = player.getName() + " was voted to be killed";
                        break;
                    }
                }
                if (output != null) return output;
            case 2:
                for(Player player : players)
                    if (player instanceof Joker && !player.isKilled) {
                        output = "There is a joker who’s name starts with " + player.getName().charAt(0);
                        break;
                    }
                if (output != null) return output;

            case 3:
                int sum = 0;
                for(Player player : players)
                    if (player instanceof MafiaGroup && !player.isKilled) {
                        sum++;
                    }
                return "Number of alive mafia : " + sum;
        }
        return null;
    }
}
