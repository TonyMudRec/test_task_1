package org.example.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PositionCardDAO extends BaseDAO {

    private Connection connection;

    public PositionCardDAO(Connection conn) {
        connection = conn;
    }

    public Optional<String> getName(Long groupId) throws SQLException {
        String sql = """
                SELECT pc.name
                FROM position_card pc
                WHERE pc.groupId = ?
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, groupId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getString("name"));
            }
        }
        return Optional.empty();
    }
}
