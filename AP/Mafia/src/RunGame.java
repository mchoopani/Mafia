import java.util.Scanner;

public class RunGame {
    static Scanner scanner = new Scanner(System.in);
    static int numberOfDay = 1;
    public static boolean isMafiaWon = false;
    public static boolean isVillagerWon = false;
    public static String triedToKill = null;
    public static String killed = null;
    public static String silenced = null;
    public static String firstSwapped = null;
    public static String secondSwapped = null;
    public static boolean isNight = false;
    public static void main(String[] args) {
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
                        String names = scanner.nextLine().replaceFirst(" ","");
                        splits = names.split(" ");
                        numberOfPlayers = splits.length;
                        players = new Player[numberOfPlayers];
                        playersHistory = new Player[numberOfPlayers];
                        assignedPlayers = new String[numberOfPlayers];
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
                    if (playersIndicator == players.length){
                        System.out.println("all players have role");
                        continue Outer;
                    }
                    String thisName = scanner.next();
                    for (int i = 0; i < playersIndicator; i++) {
                        if (thisName.equals(assignedPlayers[i])){
                            System.out.println("this player already has role");
                            continue Outer;
                        }
                    }
                    String foundName = null;
                    for(String name : splits)
                        if (name.equals(thisName))
                            foundName = name;
                    if (foundName == null){
                        System.out.println("user not found");
                        continue Outer;
                    }
                    String thisRole = scanner.next();
                    Player foundRole = findRole(foundName,thisRole);
                    if (foundRole == null)
                    {
                        System.out.println("role not found");
                        continue;
                    }
                    assignedPlayers[playersIndicator] = foundName;
                    playersHistory[playersIndicator] = foundRole;
                    players[playersIndicator++] = foundRole;

                    break;
                case "get_game_state":
                    censusPlayers(players, true);
                    break;
                case "start_game":
                    if (!isGameCreated){
                        System.out.println("no game created");
                        continue Outer;
                    }
                    if (isGameStarted){
                        System.out.println("game has already started");
                        continue Outer;
                    }
                    if (hasVoidElement(players)){
                        System.out.println("one or more player do not have a role");
                        continue Outer;
                    }
                    isGameStarted = true;
                    System.out.println();
                    System.out.println("Ready? Set! Go.");
                    while (true){
                        sunRise(players,playersHistory);
                        players = throwOutDead(players);
                        resetVotes(players);
                        if(censusPlayers(players,false))
                            break Outer;
                        sunSet(players,playersHistory);
                        players = throwOutDead(players);
                        resetVotes(players);
                        if(censusPlayers(players,false))
                            break Outer;
                    }
            }
        }
        if (isMafiaWon)
            System.out.println("Mafia Won!");
        else
            System.out.println("Villager Won!");
    }
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
    public static boolean hasVoidElement(Player[] players){
        return players[players.length-1] == null;
    }
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
    public static void sunRise(Player[] players, Player[] playersHistory){
        System.out.println("Day " + numberOfDay);
        if (numberOfDay != 1)
            System.out.println(dayStatus(triedToKill,killed,silenced));
        for(Player p : players){
            System.out.print(p.getName());
            System.out.println(": " + p.getClass().getName());
        }
        while (true){
            String voterName = scanner.next();
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
            String voteeName = scanner.next();
            Player voter = findPlayer(voterName,playersHistory);
            Player votee = findPlayer(voteeName,playersHistory);
            if (voter == null || votee == null){
                System.out.println("user not found");
                continue;
            }
            if (votee.isKilled){
                System.out.println("votee already dead");
                continue;
            }
            if (!voter.giveVote(votee)){
                System.out.println("voter is silenced");
            }
        }
        voteCounting(players);
        isNight = true;
        killed = null;
    }
    public static void resetVotes(Player[] players){
        for(Player player : players) {
            player.voteNum = 0;
            if (player instanceof Detective)
                ((Detective)player).getReportRecently = false;
        }
    }
    public static void voteCounting(Player[] players){
        Player targetPlayer = null;
        triedToKill = null;
        killed = null;
        int max = 0;
        for(Player player : players){
            if (player.voteNum >= max && !player.SavedByDoctor){
                max = player.voteNum;
                targetPlayer = player;
            }
        }
        int sum = 0;
        if (max != 0){
            triedToKill = "";
        }
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
        if (targetPlayer instanceof Joker){
            System.out.println("Joker won!");
            System.exit(0);
        }
        if (!isNight)
            System.out.println(targetPlayer.getName() + " died");
        else
        if (!targetPlayer.SavedByDoctor && !targetPlayer.haveEnoughHearts()) {
            killed = targetPlayer.getName();
            if (targetPlayer instanceof Informer)
                killed += "*-*:)";
        }
        if (targetPlayer.haveEnoughHearts())
            triedToKill = null;
        if (isNight)
            targetPlayer.kill(players);
        else
            targetPlayer.kill();
    }
    public static void sunSet(Player[] players, Player[] playersHistory){
        System.out.println("Night " + numberOfDay++);
        int roleIndicator = 0;
        for(Player player : players) {
            if (player.hasRoleOnNight)
                System.out.println(player.getName() + ": " + player.getClass().getName());
            player.isSilent = false;
        }
        while (true){
            String firstPlayerName = scanner.next();
            if (firstPlayerName.equals("end_night")) {
                scanner.next();
                String firstPlayerName0 = scanner.next();
                String secondPlayerName0 = scanner.next();
                Player firstPlayer0 = findPlayer(firstPlayerName0,playersHistory);
                Player secondPlayer0 = findPlayer(secondPlayerName0,playersHistory);
                if (firstPlayer0.isKilled || secondPlayer0.isKilled){
                    System.out.println("user is dead");
                    return;
                }
                String temp = firstPlayer0.getName();
                firstPlayer0.setName(secondPlayer0.getName());
                secondPlayer0.setName(temp);
                firstSwapped = firstPlayerName0;
                secondSwapped = secondPlayerName0;
                break;
            }
            if (firstPlayerName.equals("get_game_state")){
                censusPlayers(players, true);
                continue;
            }
            if (firstPlayerName.equals("swap_character")){
                System.out.println("can’t swap before end of night");
                scanner.nextLine();
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
            firstPlayer.playRoleOnNight(secondPlayer);
        }
        for(Player player : players){
            if (player.lastVotee != null) {
                player.giveVote(player.lastVotee);
                player.lastVotee = null;
            }
        }
        voteCounting(players);
        isNight = false;
    }
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
    public static String dayStatus(String triedToKill, String killed, String silenced){
        String output = "";
        boolean isInformer = killed.contains("*-*:)");
        killed = killed.replace("*-*:)","");
        output += triedToKill != null ? "mafia tried to kill " + triedToKill + "\n" : "";
        output += killed != null ? killed + " was killed\n" + (isInformer? killed + " was an informer\n":"") : "";
        output += silenced != null ? "Silenced " + silenced + "\n" : "";
        output += firstSwapped + " swapped characters with " + secondSwapped;
        return output;
    }
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
