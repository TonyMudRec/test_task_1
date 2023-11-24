package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SkuGroup {
    Long groupId;
    Long parentId;
    String name;

    public SkuGroup(Long groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }
}
