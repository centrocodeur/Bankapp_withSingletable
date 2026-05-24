package com.marien.ebankingbackend.exceptions;

public class BalanceNotSufficientExeption extends Exception {
    public BalanceNotSufficientExeption(String message) {
        super(message);
    }
}
