import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

public class CinemaManager {
    String filePath;
    List<Movie> movie=new LinkedList<>();
    public CinemaManager(String filePath){
        this.filePath=filePath;
    }
    public List<Movie> getMovies() throws IOException {
        int counter=0;
        FileReader f=new FileReader(filePath);
        BufferedReader bf=new BufferedReader(f);
        String ans=bf.readLine();
        while (ans!=null){
            String[] parts;
            parts=ans.split("\t");
            List<String> g=new LinkedList<>();
            for(int i=0; i<parts.length; i++){
                parts[i]=parts[i].replace("\t", "");
            }
            String[] ge;
            ge=parts[4].split("\\|");
            for(int i=0; i<ge.length; i++){
                ge[i]=ge[i].replace("|", "");
            }
            for(int i=0; i<ge.length; i++){
                g.add(ge[i]);
            }
            Movie temp=new Movie(Integer.parseInt(parts[0]), parts[1], Integer.parseInt(parts[2]), parts[3], g, Integer.parseInt(parts[5]));
            if(getById(Integer.parseInt(parts[0]))==null){
                this.movie.add(counter, temp);
            }
            else{
                this.movie.set(counter, temp);
            }
            ans= bf.readLine();
            counter++;
        }
        return movie;
    }
    public Movie getById(int id){
        for(Movie m: movie){
            if(m.id==id){
                return m;
            }
        }
        return null;
    }
    public Movie getByTitle(String title){
        for(Movie m: movie){
            if(m.title.equals(title)){
                return m;
            }
        }
        return null;
    }
    public boolean buy(int id){
        for(Movie m: movie){
            if(m.id==id){
                m.boughtTicketsCount++;
                return true;
            }
        }
        return false;
    }
    public boolean add(Movie movie){
        for(Movie m: this.movie){
            if(m.id==movie.id){
                return false;
            }
        }
        this.movie.add(movie);
        return true;
    }
    public boolean delete(int id){
        for(Movie m: movie){
            if(m.id==id){
                movie.remove(m);
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        CinemaManager cinemaManager = new CinemaManager("D:\\College\\AP\\Quiz\\src\\Movies.txt");
        List<Movie> movies = cinemaManager.getMovies();
        System.out.println(movies.size()); // 60
        Movie movie = cinemaManager.getById(13);
        System.out.println(movie.title); // Mighty Aphrodite
        int boughtTicketsCount = movie.boughtTicketsCount;
        System.out.println(cinemaManager.buy(movie.id)); // true
        System.out.println(cinemaManager.buy(1000)); // false
        System.out.println(movie.boughtTicketsCount == boughtTicketsCount + 1); // true
        Movie modernTimes = new Movie(
                70,
                "Modern Times",
                1936,
                "https://www.imdb.com/title/tt0027977/",
                Arrays.asList("Comedy", "Classic"),
                103256
        );
        System.out.println(cinemaManager.add(modernTimes)); // true
        movies = cinemaManager.getMovies();
        /*for(int i=0; i<movies.size(); i++){
            System.out.println(movies.get(i).toString());
        }*/
        System.out.println(movies.size()); // 60
        System.out.println(movies.get(movies.size() - 1).title); // Modern Times
        System.out.println(cinemaManager.getByTitle("Modern Times").genres.toString()); // [Comedy, Classic]
        System.out.println(cinemaManager.getById(404)); // null
    }
}
