package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SkuGroup {
    String groupId;
    String parentId;
    String name;

    public SkuGroup(String groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }
}
