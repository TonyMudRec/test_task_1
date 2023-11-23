package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import org.example.dao.BaseDAO;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;


public class App {
    public static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start(8080);
    }

    public static Javalin getApp() throws IOException, SQLException {
        HikariDataSource dataSource = getHikariDataSource();

        java.net.URL url = App.class.getClassLoader().getResource("pg_dump.sql");
        if (url == null) {
            throw new FileNotFoundException();
        }
        File file = new File(url.getFile());
        String sql = Files.lines(file.toPath())
                .collect(Collectors.joining("\n"));

        logger.info("Trying to connecting database");

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseDAO.dataSource = dataSource;

        logger.info("Successful database connection");

        Javalin app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

//        addRoutes(app);
        app.get("/", ctx -> ctx.result("Hello World"));
        return app;
    }

    @NotNull
    private static HikariDataSource getHikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        String dbName = System.getenv().getOrDefault("JDBC_DATABASE_NAME", "testdb");
        String dbPort = System.getenv().getOrDefault("JDBC_DATABASE_PORT", "5432");
        String userName = System.getenv().getOrDefault("JDBC_USER_NAME", "admin");
        String userPassword = System.getenv().getOrDefault("JDBC_USER_PASSWORD", "testdb");
        String jdbc = String.format("jdbc:postgresql://localhost:%s/%s?user=%s&password=%s"
                , dbPort, dbName, userName, userPassword);
        hikariConfig.setJdbcUrl(jdbc);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

}
