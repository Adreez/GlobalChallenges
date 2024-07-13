package sk.adr3ez.globalchallenges.api.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;

import java.sql.Timestamp;


@Getter
@Entity
@Table(name = "games")
public class DBGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    private String server;

    @Column(name = "game_key")
    private String gameKey;

    @Column(name = "game_description")
    private String gameDescription;

    @Column(name = "game_start_time", columnDefinition = "TIMESTAMP")
    private Timestamp startTime;

    @Column(name = "game_end_time", columnDefinition = "TIMESTAMP")
    @Setter
    private Timestamp endTime;

    @Column(name = "players_joined")
    @Setter
    private int playersJoined = 0;

    @Column(name = "players_finished")
    @Setter
    private int playersFinished = 0;

    public DBGame() {
    }

    public DBGame(String gameKey, String gameDescription, Timestamp startTime) {
        this.gameKey = gameKey;
        this.gameDescription = gameDescription;
        this.startTime = startTime;

        //Fetch server name from config
        this.server = GlobalChallengesProvider.get().getConfiguration().getString(ConfigRoutes.SERVER_ID.getRoute());
    }

    @Override
    public String toString() {
        return "DBGame{" +
                "id=" + id +
                ", server='" + server + '\'' +
                ", gameKey='" + gameKey + '\'' +
                ", gameDescription='" + gameDescription + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", playersJoined=" + playersJoined +
                ", playersFinished=" + playersFinished +
                '}';
    }
}
