import java.util.Scanner;

public class RunGame {
    static Scanner scanner = new Scanner(System.in);

    static int numberOfDay = 1;

    public static boolean isMafiaWon = false;
    public static boolean isVillagerWon = false;
    public static boolean isNight = false;

    public static String triedToKill = null;
    public static String killed = null;
    public static String silenced = null;
    public static String firstSwapped = null;
    public static String secondSwapped = null;

    public static void main(String[] args) {
        // first initialization...
        boolean isGameStarted = false;
        boolean isGameCreated = false;
        int numberOfPlayers = 0;
        Player[] players = null;
        Player[] playersHistory = null;
        int playersIndicator = 0;
        String[] splits = null;
        String[] assignedPlayers = null;
        Outer: while (true){
            String command = scanner.next();
            switch (command){
                case "create_game":
                    if (!isGameCreated){
                        // getting player names and waste additional spaces
                        String names = scanner.nextLine().replaceFirst(" ","");
                        splits = names.split(" ");
                        numberOfPlayers = splits.length;
                        players = new Player[numberOfPlayers];
                        playersHistory = new Player[numberOfPlayers];
                        assignedPlayers = new String[numberOfPlayers];
                        // setting isGameCreated, true. to avoid recreate the game and avoid start game without creating game (for seeing handle this error see lines 43 and 49)
                        isGameCreated = true;
                    }
                    else {
                        System.out.println("game has created before");
                        continue;
                    }
                    break;
                case "assign_role":
                    if (!isGameCreated){
                        System.out.println("no game created");
                        continue Outer;
                    }
                    // stop assigning roles at the right time (after having all player roles)
                    if (playersIndicator == players.length){
                        System.out.println("all players have role");
                        continue Outer;
                    }
                    String thisName = scanner.next();
                    // avoid assigning two and more role to one player
                    for (int i = 0; i < playersIndicator; i++) {
                        if (thisName.equals(assignedPlayers[i])){
                            System.out.println("this player already has role");
                            continue Outer;
                        }
                    }
                    // search "player", in player names array. if player does not exist show error!
                    String foundName = null;
                    for(String name : splits)
                        if (name.equals(thisName))
                            foundName = name;
                    if (foundName == null){
                        System.out.println("user not found");
                        continue Outer;
                    }
                    // find right role in children of Player Class. if doesn't mach entered role show error and wait to correct command and arguments
                    String thisRole = scanner.next();
                    Player foundRole = findRole(foundName,thisRole);
                    if (foundRole == null)
                    {
                        System.out.println("role not found");
                        continue;
                    }
                    // add valid player to player arrays
                    assignedPlayers[playersIndicator] = foundName;
                    playersHistory[playersIndicator] = foundRole;
                    players[playersIndicator++] = foundRole;
                    break;
                case "get_game_state":
                    censusPlayers(players, true);
                    break;
                case "start_game":
                    //handle wrong command such as
                    // : start game without creating it!
                    if (!isGameCreated){
                        System.out.println("no game created");
                        continue Outer;
                    }
                    // : start a game that had started before!
                    if (isGameStarted){
                        System.out.println("game has already started");
                        continue Outer;
                    }
                    // : start game without assign role to one player minimum!
                    if (hasVoidElement(players)){
                        System.out.println("one or more player do not have a role");
                        continue Outer;
                    }
                    isGameStarted = true;
                    System.out.println();
                    System.out.println("Ready? Set! Go.");
                    // while loop whose each iteration is 24 hours of a day
                    while (true){
                        // the day is coming ...
                        sunRise(players,playersHistory);
                        // deport killed player to waiting room
                        players = throwOutDead(players);
                        // all players have vote = 0 after this calling
                        resetVotes(players);
                        // end game if mafia or villager achieve to condition of win
                        if(censusPlayers(players,false))
                            break Outer;
                        // the night is coming ...
                        sunSet(players,playersHistory);
                        // deport killed player to waiting room
                        players = throwOutDead(players);
                        // all players have vote = 0 after this calling
                        resetVotes(players);
                        // end game if mafia or villager achieve to condition of win
                        if(censusPlayers(players,false))
                            break Outer;
                    }
            }
        }
        // this is the END
        if (isMafiaWon)
            System.out.println("Mafia Won!");
        else
            System.out.println("Villager Won!");
    }
    // this method return correct role (an object from subclasses of Player class ) base of input String "role"
    public static Player findRole(String name , String role){
        switch (role){
            case "Joker": return new Joker(name);
            case "villager": return new Villager(name);
            case "detective": return new Detective(name);
            case "doctor": return new Doctor(name);
            case "bulletproof": return new BulletProof(name);
            case "mafia": return new Mafia(name);
            case "godfather": return new GodFather(name);
            case "silencer": return new Silencer(name);
            case "informer": return new Informer(name);
        }
        return null;
    }
    // if last element of any array is null, this array is not completed :D
    public static boolean hasVoidElement(Player[] players){
        return players[players.length-1] == null;
    }
    // find "name" in players.name array
    public static Player findPlayer(String name,Player[] players){
        Player output = null;
        for(Player player : players){
            if (player.getName().equals(name)) {
                output = player;
                break;
            }
        }
        return output;
    }
    // open eyes please...
    public static void sunRise(Player[] players, Player[] playersHistory){
        // welcome message at start of day
        System.out.println("Day " + numberOfDay);
        if (numberOfDay != 1)
            System.out.println(dayStatus(triedToKill,killed,silenced));
        for(Player p : players){
            System.out.print(p.getName());
            System.out.println(": " + p.getClass().getName());
        }
        // voting loop
        while (true){
            String voterName = scanner.next();
            // handle repetitious errors and commands
            if (voterName.equals("end_vote"))
                break;
            if (voterName.equals("get_game_state")){
                censusPlayers(players, true);
                continue;
            }
            if (voterName.equals("swap_character")){
                System.out.println("voting in progress");
                scanner.nextLine();
                continue;
            }
            if (voterName.equals("start_game")){
                System.out.println("the game has already began");
                continue;
            }
            String voteeName = scanner.next();
            // find players (to get much data see findPlayer() docs)
            Player voter = findPlayer(voterName,playersHistory);
            Player votee = findPlayer(voteeName,playersHistory);
            // if each of players doesn't find, show error
            if (voter == null || votee == null){
                System.out.println("user not found");
                continue;
            }
            // if each of players is died, show error
            if (votee.isKilled){
                System.out.println("votee already dead");
                continue;
            }
            // if voter is silenced last night by silencer, avoid voting and show error
            if (!voter.giveVote(votee)){
                System.out.println("voter is silenced");
            }
        }
        // count this day votes
        voteCounting(players);
        // the night come after the day :\
        isNight = true;
        // set killed = null to proper operation method dayStatus()
        killed = null;
    }
    // sleep... the night is coming
    public static void sunSet(Player[] players, Player[] playersHistory){
        // welcome day messages
        System.out.println("Night " + numberOfDay++);
        int roleIndicator = 0;
        // print night roles and volume up silenced player
        for(Player player : players) {
            if (player.hasRoleOnNight)
                //print night roles
                System.out.println(player.getName() + ": " + player.getClass().getName());
            // volume up silenced player
            player.isSilent = false;
        }
        // night operation loop
        while (true){
            String firstPlayerName = scanner.next();
            // end night command
            if (firstPlayerName.equals("end_night")) {
                // next command 100% will be swap_character unless proven otherwise :$
                scanner.next();
                String firstPlayerName0 = scanner.next();
                String secondPlayerName0 = scanner.next();
                Player firstPlayer0 = findPlayer(firstPlayerName0,playersHistory);
                Player secondPlayer0 = findPlayer(secondPlayerName0,playersHistory);
                // we can't swap died players 8)
                if (firstPlayer0.isKilled || secondPlayer0.isKilled){
                    System.out.println("user is dead");
                    return;
                }
                // instead of swapping characters, swap their names because we are very smart :)))))
                String temp = firstPlayer0.getName();
                firstPlayer0.setName(secondPlayer0.getName());
                secondPlayer0.setName(temp);
                firstSwapped = firstPlayerName0;
                secondSwapped = secondPlayerName0;
                break;
            }
            // get state of game with calling censusPlayers() method
            if (firstPlayerName.equals("get_game_state")){
                censusPlayers(players, true);
                continue;
            }
            // handle errors :|
            if (firstPlayerName.equals("swap_character")){
                System.out.println("can’t swap before end of night");
                scanner.nextLine();
                continue;
            }
            if (firstPlayerName.equals("start_game")){
                System.out.println("the game has already began");
                continue;
            }
            String secondPlayerName = scanner.next();
            Player firstPlayer = findPlayer(firstPlayerName,playersHistory);
            Player secondPlayer = findPlayer(secondPlayerName,playersHistory);
            if (firstPlayer == null || secondPlayer == null){
                System.out.println("user not joined");
                continue;
            }
            if (firstPlayer.isKilled || secondPlayer.isKilled){
                System.out.println("user is dead");
                continue;
            }
            if (!firstPlayer.hasRoleOnNight){
                System.out.println("user can not wake up during night");
                continue;
            }
            // silencer has 2D role on night
            if (firstPlayer instanceof Silencer){
                if (roleIndicator != 0){
                    firstPlayer.setLastVotee(secondPlayer);
                }
                if (roleIndicator++ == 0) {
                    ((Silencer) firstPlayer).silent(secondPlayer);
                    silenced = secondPlayerName;
                }
                continue;
            }
            // play correct role of night with overridden methods in Player and its subclasses.
            firstPlayer.playRoleOnNight(secondPlayer);
        }
        // mafias are very khafan. they can vote n times. but the last vote will be applied :)
        for(Player player : players){
            if (player.lastVotee != null) {
                // give vote to last vote of voter
                player.giveVote(player.lastVotee);
                // reset vote of voter to tomorrow night
                player.lastVotee = null;
            }
        }
        // start counting
        voteCounting(players);
        // after a dark night, come a light day
        isNight = false;
    }
    public static void voteCounting(Player[] players){
        Player targetPlayer = null;
        triedToKill = null;
        killed = null;
        int max = 0;
        // find the most votes
        for(Player player : players){
            if (player.voteNum >= max && !player.SavedByDoctor){
                max = player.voteNum;
                targetPlayer = player;
            }
        }
        int sum = 0;
        // if max != 0, there is one player that has >1 voteCount so set a "" value to avoid null pointer exception (for much data see algorithm of dayStatus() method)
        if (max != 0){
            triedToKill = "";
        }
        // if sum != 1 => there is a player with max vote also not saved by doctor, so nobody will die.
        for(Player player : players)
            if (player.voteNum == max && !player.SavedByDoctor) {
                sum++;
                if (max != 0)
                    triedToKill += player.getName() + " ";
            }
        if (sum > 1){
            if (!isNight)
                System.out.println("nobody died");
            return;
        }
        // if  joker will be died, the game doesn't continue.
        if (targetPlayer instanceof Joker && !isNight){
            System.out.println(targetPlayer.getName() + " is died");
            System.out.println("he/she was JOKER :\\");
            System.out.println("Joker won!");
            System.exit(0);
        }
        // the method roles different behavior
        if (!isNight)
            System.out.println(targetPlayer.getName() + " died");
        else
        if (!targetPlayer.SavedByDoctor && !targetPlayer.haveEnoughHearts()) {
            killed = targetPlayer.getName();
            if (targetPlayer instanceof Informer)
                // add this pattern to identify smart and dangerous INFORMER
                killed += "*-*:)";
        }
        if (targetPlayer.haveEnoughHearts())
            triedToKill = null;
        // that different behavior
        if (isNight)
            targetPlayer.kill(players);
        else
            targetPlayer.kill();
    }
    // resting votes number after day
    public static void resetVotes(Player[] players){
        for(Player player : players) {
            player.resetVote();
            if (player instanceof Detective)
                ((Detective)player).getReportRecently = false;
        }
    }
    // census count of mafias and villagers
    // second parameter show we want output or not
    // if wantOutput == false means we want returned value to understand mafia or villager win or not?
    public static boolean censusPlayers(Player[] players,boolean wantOutput){
        int sumOfMafia = 0;
        int sumOfVillager = 0;
        for (Player player : players) {
            if (player instanceof MafiaGroup)
                sumOfMafia++;
            else if (player instanceof VillagerGroup)
                sumOfVillager++;
        }
        if (wantOutput){
            System.out.println("Mafia = " + sumOfMafia);
            System.out.println("Villager = " + sumOfVillager);
        }
        // win conditions
        if (sumOfMafia == 0){
            isVillagerWon = true;
            return true;
        }
        else if (sumOfVillager <= sumOfMafia) {
            isMafiaWon = true;
            return true;
        }
        return false;
    }
    // this method returned status of last day operations, if each parameter is null, means that role didn't play last night
    // example) if killed == null -> no one died last night
    public static String dayStatus(String triedToKill, String killed, String silenced){
        String output = "";
        boolean isInformer = killed != null && killed.contains("*-*:)");
        if (killed != null)
            killed = killed.replace("*-*:)","");
        output += triedToKill != null ? "mafia tried to kill " + triedToKill + "\n" : "";
        output += killed != null ? killed + " was killed\n" + (isInformer? killed + " was an informer\n":"") : "";
        output += silenced != null ? "Silenced " + silenced + "\n" : "";
        if (firstSwapped != null)
            output += firstSwapped + " swapped characters with " + secondSwapped;
        firstSwapped = null;
        secondSwapped = null;
        return output;
    }
    // deport killed player
    public static Player[] throwOutDead(Player[] players){
        int newSize = 0;
        for(Player player : players){
            if (!player.isKilled)
                newSize++;
            player.SavedByDoctor = false;
        }
        Player[] newPlayers = new Player[newSize];
        int i = 0;
        for (Player player: players){
            if (player.isKilled)
                continue;
            newPlayers[i++] = player;
        }
        return newPlayers;
    }
}
