import java.util.*;

class Team {
    final String name; final int points; final int goalDiff;
    Team(String name,int points,int goalDiff){this.name=name;this.points=points;this.goalDiff=goalDiff;}
    @Override public String toString(){return name+"("+points+","+goalDiff+")";}
}
class Leaderboard<T> {
    private final List<T> items=new ArrayList<>();
    private final Comparator<? super T> order;
    Leaderboard(Comparator<? super T> order){this.order=order;}
    void add(T item){items.add(item);}
    List<T> ranking(){List<T> copy=new ArrayList<>(items);copy.sort(order);return copy;}
    void addAll(List<? extends T> values){items.addAll(values);}
}
public class LeaderboardDemo {
    public static void main(String[] args) {
        Comparator<Team> byRank=Comparator
            .comparingInt((Team t)->t.points).reversed()
            .thenComparing(Comparator.comparingInt((Team t)->t.goalDiff).reversed())
            .thenComparing(t->t.name);

        Leaderboard<Team> board=new Leaderboard<>(byRank);
        board.add(new Team("Panthers",9,4));
        board.add(new Team("Eagles",9,6));
        board.add(new Team("Cobras",6,1));
        board.add(new Team("Bisons",9,4));
        board.add(new Team("Falcons",6,1));
        board.add(new Team("Ants",6,1));
        System.out.println("Comparator ranking: "+board.ranking());

        // Alternative: Comparable defines a natural order inside Team-like class.
        // Alternative stable sorts: least important key first, most important last.
        List<Team> stable=new ArrayList<>(Arrays.asList(
            new Team("Panthers",9,4),new Team("Eagles",9,6),
            new Team("Cobras",6,1),new Team("Bisons",9,4)));
        stable.sort(Comparator.comparing(t->t.name));
        stable.sort(Comparator.comparingInt((Team t)->t.goalDiff).reversed());
        stable.sort(Comparator.comparingInt((Team t)->t.points).reversed());
        System.out.println("Three stable sorts: "+stable);

        // Deliberately faulty comparator: ignores goal difference and name.
        Comparator<Team> faulty=(a,b)->b.points-a.points;
        System.out.println("Faulty comparator treats same-points teams as equal: "+(faulty.compare(
            new Team("A",9,1),new Team("Z",9,99))==0));
        // comparingInt avoids subtraction overflow; thenComparing makes tie-breaks explicit.
    }
}
