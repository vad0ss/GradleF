package by.prilepishev.model;

public enum Type {
    ARMCHAIR("Кресло"),
    CHAIR("Стул"),
    SOFA("Диван");

    private final String displayName;

    Type(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Type fromDisplayName(String name) {
        for (Type t : values()) {
            if (t.displayName.equals(name)) return t;
        }
        throw new IllegalArgumentException("Unknown type: " + name);
    }


    @Override
    public String toString() {
        return this.displayName;
    }

}

