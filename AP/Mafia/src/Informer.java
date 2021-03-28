public class Informer extends VillagerGroup{
    public Informer(String name) {
        super(name);
        hasRoleOnNight = false;
    }
    public String inform(Player[] players){
        // make random int to choose one operation between 4.
        int randomIndicator = (int) (Math.random() * 4);
        String output = null;
        switch (randomIndicator){
            // cases 0,1,2 have not break; because we should do at least one.
            case 0:
                // show first character of a mafia's name
                for(Player player : players)
                    if (player instanceof MafiaGroup && !player.isKilled) {
                        output = "There is a mafia who’s name starts with " + player.getName().charAt(0);
                        break;
                    }
                if (output != null) return output;
            case 1:
                // show a votee that not killed last night
                for(Player player : players) {
                    if (player == this) continue;
                    if (player instanceof VillagerGroup && player.voteNum != 0) {
                        output = player.getName() + " was voted to be killed";
                        break;
                    }
                }
                if (output != null) return output;
            case 2:
                // show first character of joker's name
                for(Player player : players)
                    if (player instanceof Joker && !player.isKilled) {
                        output = "There is a joker who’s name starts with " + player.getName().charAt(0);
                        break;
                    }
                if (output != null) return output;

            case 3:
                // this case always have output so placed on last case
                // count alive mafias
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
