package sk.adr3ez.globalchallenges.api.database.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "games")
public class DBGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private DBServer server;

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

    public DBGame(String gameKey, String gameDescription, DBServer server, LocalDateTime startTime) {
        this.gameKey = gameKey;
        this.gameDescription = gameDescription;
        this.server = server;
        this.startTime = startTime;
    }
}
