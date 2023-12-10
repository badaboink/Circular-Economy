package com.circular.Circular.economy.dto;

import com.circular.Circular.economy.entity.ResourceType;
import lombok.Getter;

@Getter
public class ResourceTypeDTO {
    private String name;
    private String description;

    public ResourceTypeDTO(ResourceType resourceType) {
        this.name = resourceType.name();
        this.description = resourceType.getDescription();
    }

}