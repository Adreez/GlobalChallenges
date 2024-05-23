package sk.adr3ez.globalchallenges.api.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;

import java.time.LocalDateTime;


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

    @Column(name = "game_start_time", columnDefinition = "DATETIME")
    private LocalDateTime startTime;

    @Column(name = "game_end_time", columnDefinition = "DATETIME")
    private LocalDateTime endTime;

    public DBGame() {
    }

    public DBGame(String gameKey, String gameDescription, LocalDateTime startTime) {
        this.gameKey = gameKey;
        this.gameDescription = gameDescription;
        this.startTime = startTime;

        //Fetch server name from config
        this.server = GlobalChallengesProvider.get().getConfiguration().getString(ConfigRoutes.SERVER_ID.getRoute());
    }
}
