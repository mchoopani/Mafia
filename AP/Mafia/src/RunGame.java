import java.util.Scanner;

public class RunGame {
    static Scanner scanner = new Scanner(System.in);
    static int numberOfDay = 1;
    public static boolean isMafiaWon = false;
    public static boolean isVillagerWon = false;
    public static void main(String[] args) {
        boolean isGameStarted = false;
        boolean isGameCreated = false;
        int numberOfPlayers = 0;
        Player[] players = null;
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
                    players[playersIndicator++] = foundRole;

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
                    for(Player p : players){
                        System.out.print(p.getName());
                        System.out.println(": " + p.getClass().getName());
                        isGameStarted = true;
                    }
                    System.out.println();
                    System.out.println("Ready? Set! Go.");
                    while (true){
                        sunRise(players);
                        if(censusPlayers(players,false))
                            break Outer;
                        sunSet(players);
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
    public static void sunRise(Player[] players){
        System.out.println("Day " + numberOfDay);
        while (true){
            String voterName = scanner.next();
            if (voterName.equals("end_vote")){
                break;
            }
            String voteeName = scanner.next();
            Player voter = findPlayer(voterName,players);
            Player votee = findPlayer(voteeName,players);
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
    }
    public static void resetVotes(Player[] players){
        for(Player player : players)
            player.voteNum = 0;
    }
    public static void voteCounting(Player[] players){
        Player targetPlayer = null;
        int max = 0;
        for(Player player : players){
            if (player.voteNum >= max){
                max = player.voteNum;
                targetPlayer = player;
            }
        }
        int sum = 0;
        for(Player player : players)
            if (player.voteNum == max)
                sum++;
        if (sum > 1){
            System.out.println("nobody died");
            return;
        }
        if (targetPlayer instanceof Joker){
            System.out.println("Joker won!");
            System.exit(0);
        }
        System.out.println(targetPlayer.getName() + " died");
        targetPlayer.kill();
        resetVotes(players);
    }
    public static void sunSet(Player[] players){
        System.out.println("Night " + numberOfDay++);
        for(Player player : players)
            if (player.hasRoleOnNight)
                System.out.println(player.getName() + ": " + player.getClass().getName());
        while (true){
            String firstPlayerName = scanner.next();
            if (firstPlayerName.equals("end_night"))
                break;
            String secondPlayerName = scanner.next();
            Player firstPlayer = findPlayer(firstPlayerName,players);
            Player secondPlayer = findPlayer(secondPlayerName,players);
            if (!firstPlayer.hasRoleOnNight){
                System.out.println("user can not wake up during night");
                continue;
            }
            if (firstPlayer == null || secondPlayer == null){
                System.out.println("user not joined");
                continue;
            }
            if (firstPlayer.isKilled || secondPlayer.isKilled){
                System.out.println("user is dead");
                continue;
            }
            int roleIndicator = 0;
            if (firstPlayer instanceof Silencer){
                if (roleIndicator == 1){
                    firstPlayer.giveVote(secondPlayer);
                }
                if (roleIndicator++ == 0)
                    ((Silencer) firstPlayer).silent(secondPlayer);
                continue;
            }
            firstPlayer.playRoleOnNight(secondPlayer);
        }
        voteCounting(players);
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
}
