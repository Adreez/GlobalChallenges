package sk.adr3ez.globalchallenges.api.database.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "servers")
public class DBServer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_id")
    private Long id;

    @Column(name = "server_name")
    private String name;

    public DBServer() {
    }

    public DBServer(String name) {
        this.name = name;
    }

}
