package sk.sicak.totoro.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String firstPlayer;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String secondPlayer;

    @JsonProperty
    private String result;

    public Result(final Match match) {
        this.firstPlayer = match.getFirstPlayer().getName();
        this.secondPlayer = match.getSecondPlayer().getName();
        this.result = match.getResult();

    }

    public Result() {
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    @Override
    public String toString() {
        return "Result{" +
                "firstPlayer='" + firstPlayer + '\'' +
                ", secondPlayer='" + secondPlayer + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

}
