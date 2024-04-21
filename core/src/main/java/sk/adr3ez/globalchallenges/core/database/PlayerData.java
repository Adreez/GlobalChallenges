package sk.adr3ez.globalchallenges.core.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Setter
@Getter
@Entity
public class PlayerData {

    @Id
    private UUID uuid;
    private String username;

    public PlayerData() {
    }

    public PlayerData(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

}
