import java.util.Scanner;

public class RunGame {
    public static void main(String[] args) {
        boolean isMafiaWin = false;
        boolean isVillagerWin = false;
        boolean isJokerWin = false;
        boolean isGameStarted = false;
        boolean isGameCreated = false;
        int numberOfPlayers = 0;
        Player[] players = null;
        int playersIndicator = 0;
        String[] splits = null;
        String[] assignedPlayers = null;
        Scanner scanner = new Scanner(System.in);
        Outer: while (!isMafiaWin && !isJokerWin && !isVillagerWin){
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
                        System.out.println("aval create bad bazi!");
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
                    break;
            }
        }
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
}
