package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PositionCard {
    String id;
    boolean isShowInApp;
    String name;
    String groupId;

    public PositionCard(String id, String name, String groupId) {
        this.id = id;
        this.isShowInApp = false;
        this.name = name;
        this.groupId = groupId;
    }
}
