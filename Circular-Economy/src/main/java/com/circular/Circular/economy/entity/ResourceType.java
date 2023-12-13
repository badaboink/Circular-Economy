package com.circular.Circular.economy.entity;

public enum ResourceType {
    FINISHING("Finishing materials"),
    BULK("Bulk building materials"),
    DOORS("Doors"),
    ELECTRICAL("Electrical installation"),
    FLOOR("Floor coverings"),
    STAIRS("Stairs"),
    WINDOWS("Windows"),
    WOOD("Wood and wood products"),
    METAL("Metal products"),
    BRICKS("Bricks, blocks"),
    PLUMBING("Plumbing"),
    ROOF("Roof coverings"),
    HEATING("Heating equipment"),
    INSULATION("Insulation materials");

    private String description;

    ResourceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ResourceType findByDescription(String description) {
        for (ResourceType type : values()) {
            if (type.getDescription().equalsIgnoreCase(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No ResourceType found with description: " + description);
    }
}

/* FULL LIST
Finishing materials
Bulk building materials
Doors
Electrical installation
Floor coverings
Stairs
Windows
Wood and wood products
Metal products
Bricks, blocks
Plumbing
Roof coverings
Heating equipment
Insulation materials
 */