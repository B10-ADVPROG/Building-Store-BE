package id.ac.ui.cs.advprog.buildingstore.authentication.model;

public enum UserRole {
    KASIR("Kasir"),
    ADMINISTRATOR("Administrator");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static UserRole fromString(String role) {
        switch (role.toLowerCase()) {
            case "kasir":
                return KASIR;
            case "administrator":
            case "admin":
                return ADMINISTRATOR;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

}
