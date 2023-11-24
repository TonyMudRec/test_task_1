package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PositionCard {
    Long id;// VARCHAR(200) PRIMARY KEY,
    boolean isShowInApp;// BOOLEAN DEFAULT FALSE,
    String name;// VARCHAR(200),
    Long groupId;// VARCHAR(200) REFERENCES sku_group(groupId) NOT NULL

    public PositionCard(String name, Long groupId) {
        this.isShowInApp = false;
        this.name = name;
        this.groupId = groupId;
    }
}
