package sk.adr3ez.globalchallenges.api.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
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

    @Setter
    @OneToMany(mappedBy = "player")
    private Set<DBPlayerData> playerData;

    public void addPlayerData(DBPlayerData playerData) {
        if (this.playerData == null) {
            this.playerData = new HashSet<>();
        }
        this.playerData.add(playerData);
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
                '}';
    }

}
