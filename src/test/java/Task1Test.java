import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.example.App;
import org.example.dao.BaseDAO;
import org.example.dao.PositionCardDAO;
import org.example.dao.SkuGroupDAO;
import org.example.model.PositionCard;
import org.example.model.SkuGroup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class Task1Test {
    private static Javalin app;

    private static Connection connection;
//    private static final MockWebServer SERVER = new MockWebServer();

    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        app = App.getApp();
        connection = BaseDAO.dataSource.getConnection();
    }
//    @BeforeAll
//    public static void beforeAll() throws IOException {
//        SERVER.enqueue(new MockResponse().setResponseCode(200));
//        SERVER.enqueue(new MockResponse()
//                .addHeader("Content-Type", "application/json; charset=utf-8")
//                .addHeader("title", "application/json; charset=utf-8")
//                .setBody("{}"));
//    }

    @AfterAll
    public static void afterAll() throws IOException, SQLException {
        app.stop();
        connection.close();
//        SERVER.shutdown();
    }
    @Test
    void rootTest() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Hello World");
        });
    }

    @Test
    void saveSkuGroupTest() throws SQLException {
        var parentGroup = new SkuGroup("1", "Parent");
        var dao = new SkuGroupDAO(connection);
        dao.save(parentGroup);
        var sql = "SELECT * FROM sku_group sg WHERE sg.name = 'Parent'";
        try (var stmt = connection.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            assertThat(resultSet.next()).isTrue();
        }
    }

    @Test
    void findParentId() throws SQLException {
        var groupDao = new SkuGroupDAO(connection);
        var cardDao = new PositionCardDAO(connection);
        var group1 = new SkuGroup("2", "1", "parent1Child1");
        var group2 = new SkuGroup("3", "2", "parent1Child2");
        var group3 = new SkuGroup("4", "Parent2");
        var group4 = new SkuGroup("5", "4", "parent2Child1");
        var group5 = new SkuGroup("6", "5", "parent2Child2");
        var parentCard1 = new PositionCard("1", "Parent1Card", "1");
        var child1Card1 = new PositionCard("2", "Child1Card", "2");
        var parentCard2 = new PositionCard("3", "Parent2Card", "4");
        groupDao.save(group1);
        groupDao.save(group2);
        groupDao.save(group3);
        groupDao.save(group4);
        groupDao.save(group5);
        cardDao.save(parentCard1);
        cardDao.save(child1Card1);
        cardDao.save(parentCard2);
        assertThat(groupDao.getCardName("3")).isEqualTo("Child1Card");
        assertThat(groupDao.getCardName("6")).isEqualTo("Parent2Card");
    }
}
