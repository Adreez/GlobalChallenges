package sk.adr3ez.globalchallenges.api.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Entity
@Table(name = "players")
public class DBPlayer {

    @Id
    @Column(length = 36)
    private String uuid;

    @Column(name = "nick")
    private String nick;

    @Setter
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DBPlayerData> playerData = new ArrayList<>();

    public void addPlayerData(DBPlayerData playerData) {
        this.playerData.add(playerData);
        playerData.setPlayer(this);
    }


    public DBPlayer() {
    }

    public DBPlayer(UUID uuid, String nick) {
        this.uuid = uuid.toString();
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "Player{" +
                "uuid='" + uuid + '\'' +
                ", username='" + nick + '\'' +
                ", playerData=" + (playerData != null ? playerData.toString() : "[]") +
                '}';
    }

}
