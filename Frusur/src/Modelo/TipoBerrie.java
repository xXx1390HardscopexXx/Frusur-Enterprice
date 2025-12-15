package Modelo;

public enum TipoBerrie {
    ARANDANO,
    FRUTILLA,
    FRAMBUESA,
    MORA;

    public static String[] getNombres() {
        TipoBerrie[] state = values();
        String[] name = new String[state.length];
        for (int i = 0; i < state.length; i++) {
            name[i] = state[i].name();
        }
        return name;
    }
}