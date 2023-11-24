package org.example.dao;

import org.example.model.SkuGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkuGroupDAO {
    private Connection connection;

    public SkuGroupDAO(Connection conn) {
        connection = conn;
    }

    public String getCardName(Long groupId) throws SQLException {
        String sql = """
                SELECT pc.name
                FROM (WITH RECURSIVE cte AS (
                    SELECT groupId, parentId, name
                    FROM sku_group
                    WHERE groupId = ?
                    
                    UNION ALL
                    
                    SELECT sg.groupId, sg.parentId, sg.name
                    FROM sku_group sg
                    JOIN cte c ON c.groupId = sg.parentId
                ))
                JOIN position_card pc ON c.groupId = pc.groupId
                WHERE pc.groupId <> 0
                LIMIT 1
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, groupId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        }
        return null;
    }

    public boolean save(SkuGroup group) throws SQLException {
        String sql = "INSERT INTO sku_group VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, group.getGroupId());
            stmt.setLong(2, group.getParentId());
            stmt.setString(3, group.getName());
            stmt.executeUpdate();
            return true;
        }
    }
}
