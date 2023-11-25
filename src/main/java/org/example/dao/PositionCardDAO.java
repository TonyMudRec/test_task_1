package org.example.dao;


import org.example.model.PositionCard;

import java.sql.*;

import static org.example.App.logger;


public class PositionCardDAO extends BaseDAO {

    private Connection connection;

    public PositionCardDAO(Connection conn) {
        connection = conn;
    }

    public void save(PositionCard card) throws SQLException {
        String sql = "INSERT INTO position_card (id, isShowInApp, name, groupId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.getId());
            stmt.setBoolean(2, card.isShowInApp());
            stmt.setString(3, card.getName());
            stmt.setString(4, card.getGroupId());
            stmt.executeUpdate();
            logger.info("The entity "
                    + PositionCard.class.getSimpleName()
                    + " with id "
                    + card.getId()
                    + " was successfully saved");
        }
    }
}
