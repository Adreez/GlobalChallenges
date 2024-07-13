package sk.adr3ez.globalchallenges.api.util;

import lombok.Getter;

@Getter
public enum StorageMethod {

    MYSQL("org.hibernate.dialect.MySQLDialect"),
    SQLITE("org.hibernate.community.dialect.SQLiteDialect"),
    POSTGRE("org.hibernate.dialect.PostgreSQLDialect"),
    //H2("org.hibernate.dialect.H2Dialect"),
    ;

    private final String driverClassName;

    StorageMethod(String driverClassName) {
        this.driverClassName = driverClassName;
    }


}
