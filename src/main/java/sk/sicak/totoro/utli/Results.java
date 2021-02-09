package sk.sicak.totoro.utli;

public enum Results {

    TWO_ONE("2:1"),
    TWO_ZERO("2:0"),
    ZERO_TWO("0:2"),
    ONE_TWO("1:2");

    public final String value;

    Results(String value) {
        this.value = value;
    }

    public static boolean isValidValue(String value){
        for (Results result : Results.values()) {
            if(value.equals(result.value)){
                return true;
            }
        }
        return false;
    }
}
