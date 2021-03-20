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
        Scanner scanner = new Scanner(System.in);
        while (!isMafiaWin && !isJokerWin && !isVillagerWin){
            String command = scanner.next();
            switch (command){
                case "create_game":
                    String names = scanner.nextLine();
                    splits = names.split(" ");
                    numberOfPlayers = splits.length;
                    players = new Player[numberOfPlayers];
                    break;
                case "assign_role":
                    String thisName = scanner.next();
                    for(String name : splits){
                        if (name.equals(thisName)){
                            String thisRole = scanner.next();
                            Player foundRole = findRole(name,thisRole);
                            if (thisRole == null)
                            {
                                System.out.println("Nayaftam rolo");
                                continue;
                            }
                            players[playersIndicator++] = foundRole;
                        }
                    }
                    break;
                case "start_game":
                    if (isGameCreated) {
                        if (!isGameStarted)
                            isGameStarted = true;
                        else System.out.println("bazi shoroo shode ghablan!");
                    }else System.out.println("bazi sakhte !shode ghablan!");
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
}
