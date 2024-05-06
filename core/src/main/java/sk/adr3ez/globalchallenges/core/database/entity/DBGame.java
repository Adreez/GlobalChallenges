package sk.adr3ez.globalchallenges.core.database.entity;

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
}
