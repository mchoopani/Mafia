import java.util.*;

public class Movie {
    public final int id;
    public final String title;
    public final int releaseYear;
    public final String IMDbURL;
    public final List<String> genres;
    public int boughtTicketsCount;

    public Movie(int id, String title, int releaseYear, String IMDbURL, List<String> genres, int boughtTicketsCount) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.IMDbURL = IMDbURL;
        this.genres = genres;
        this.boughtTicketsCount = boughtTicketsCount;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Movie)) {
            return false;
        }
        Movie m = (Movie) o;
        return this.id == m.id
                && this.title.equals(m.title)
                && this.releaseYear == m.releaseYear
                && this.IMDbURL.equals(m.IMDbURL)
                && this.genres.equals(m.genres)
                && this.boughtTicketsCount == m.boughtTicketsCount;
    }

    @Override
    public String toString() {
        String genres = "";
        int i = 0;
        for (String s : this.genres){
            if (this.genres.size() - i > 1) {
                genres += s;
                genres += "|";
            }
            if (this.genres.size() - i == 1)
                genres += s;
            i++;
        }
        return id + "\t" + title + "\t" + releaseYear + "\t" + IMDbURL + "\t" + genres + "\t" + boughtTicketsCount;
    }
}