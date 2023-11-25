package org.example.dao;

import org.example.model.SkuGroup;

import java.sql.*;

import static org.example.App.logger;

public class SkuGroupDAO {
    private Connection connection;

    public SkuGroupDAO(Connection conn) {
        connection = conn;
    }

    public String getCardName(String groupId) throws SQLException {
        String sql = """
                WITH RECURSIVE tmp AS(
                    SELECT groupId, parentId FROM sku_group
                    WHERE groupId = ?
                    
                    UNION
                    
                    SELECT sg.groupId, sg.parentId FROM sku_group sg
                    JOIN tmp t ON sg.groupId = t.parentId
                ) SELECT pc.name FROM tmp
                JOIN position_card pc ON pc.groupId = tmp.groupId
                WHERE pc.name IS NOT NULL
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, groupId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        }
        return null;
    }

    public void save(SkuGroup group) throws SQLException {
        String sql = "INSERT INTO sku_group VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, group.getGroupId());
                if (group.getParentId() == null) {
                    stmt.setNull(2, Types.VARCHAR);
                } else {
                    stmt.setString(2, group.getParentId());
                }
                stmt.setString(3, group.getName());
                stmt.executeUpdate();
            }
        logger.info("The entity " + group.getName() + " was successfully saved");
    }
}
