package sk.adr3ez.globalchallenges.api.database.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;
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

    @OneToMany(mappedBy = "player")
    private Set<DBPlayerData> playerData;


    public DBPlayer() {
    }

    public DBPlayer(UUID uuid, String nick) {
        this.uuid = uuid.toString();
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "uuid='" + uuid + '\'' +
                ", username='" + nick + '\'' +
                '}';
    }

}
