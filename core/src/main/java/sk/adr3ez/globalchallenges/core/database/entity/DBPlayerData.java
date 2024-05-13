package sk.adr3ez.globalchallenges.core.database.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "player_data")
public class DBPlayerData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private DBGame game;

    @ManyToOne
    @JoinColumn(referencedColumnName = "uuid", nullable = false)
    private DBPlayer player;

    @Column(name = "finished")
    private boolean finished;

    @Column(name = "time_joined", columnDefinition = "DATETIME")
    private LocalDateTime timeJoined;

    @Column(name = "time_finished", columnDefinition = "DATETIME")
    private LocalDateTime timeFinished;

    @Column(name = "position")
    private int position;

    public DBPlayerData() {
    }

    public DBPlayerData(DBGame DBGame, DBPlayer player) {
        this.game = DBGame;
        this.player = player;
    }
}
