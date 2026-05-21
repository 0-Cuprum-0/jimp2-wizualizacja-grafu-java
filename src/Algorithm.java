public enum Algorithm {
    FRUCHTERMAN_REINGOLD("Fruchterman-Reingold", "FRE"),
    TRIANGULATION("Triangulacja", "TRI");

    private final String displayName; // Pełna nazwa
    private final String abbreviation; // Skrót

    Algorithm(String displayName, String abbreviation) {
        this.displayName = displayName;
        this.abbreviation = abbreviation;
    }

    // Pełna nazwa
    public String getDisplayName() {
        return displayName;
    }

    // Skrót
    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public String toString() {
        return displayName;
    }
}