package si.fri.rso.badmintonappmatches.models.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "badminton_matches")
@NamedQueries(value =
        {
                @NamedQuery(name = "MatchesEntity.getAll",
                        query = "SELECT oe FROM MatchesEntity oe")
        })
public class MatchesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id1")
    private int userId1;

    @Column(name = "user_id2")
    private int userId2;

    @Column(name = "date_played")
    private Date date;

    @Column(name = "score")
    private String score;

    @Column(name = "winner")
    private int winner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId1() {
        return userId1;
    }

    public void setUserId1(int userId1) {
        this.userId1 = userId1;
    }

    public int getUserId2() {
        return userId2;
    }

    public void setUserId2(int userId2) {
        this.userId2 = userId2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}
