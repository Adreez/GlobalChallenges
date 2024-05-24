package sk.adr3ez.globalchallenges.api.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "player_data")
public class DBPlayerData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private DBGame game;

    @ManyToOne
    @JoinColumn(referencedColumnName = "uuid", nullable = false)
    @Setter
    private DBPlayer player;

    @Column(name = "finished")
    @Setter
    private boolean finished;

    @Column(name = "time_joined", columnDefinition = "DATETIME")
    private LocalDateTime timeJoined;

    @Column(name = "time_finished", columnDefinition = "DATETIME")
    @Setter
    private LocalDateTime timeFinished;

    @Column(name = "position")
    @Setter
    private int position;

    public DBPlayerData() {
    }

    public DBPlayerData(DBGame dbGame, DBPlayer dbPlayer, LocalDateTime timeJoined) {
        this.game = dbGame;
        this.player = dbPlayer;
        this.timeJoined = timeJoined;

        this.finished = false;
    }

    public DBPlayerData(DBGame dbGame, LocalDateTime timeJoined) {
        this.game = dbGame;
        this.timeJoined = timeJoined;

        this.finished = false;
    }
}
