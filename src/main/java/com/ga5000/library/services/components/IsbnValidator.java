package com.ga5000.library.services.components;

import org.springframework.stereotype.Component;

@Component
public class IsbnValidator {
    public boolean isValidIsbn(String isbn) {
        isbn = isbn.replaceAll("-", "");

        if (isbn.length() == 10) {
            return isValidIsbn10(isbn);
        } else if (isbn.length() == 13) {
            return isValidIsbn13(isbn);
        }

        return false;
    }

    private boolean isValidIsbn10(String isbn) {
        if (isbn == null || isbn.length() != 10) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            char c = isbn.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
            sum += (c - '0') * (10 - i);
        }

        char lastChar = isbn.charAt(9);
        int lastDigit = (lastChar == 'X' || lastChar == 'x') ? 10 : lastChar - '0';
        if (lastDigit < 0 || lastDigit > 10) {
            return false;
        }

        sum += lastDigit;

        return (sum % 11 == 0);
    }

    private boolean isValidIsbn13(String isbn) {
        if (isbn == null || isbn.length() != 13) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 13; i++) {
            char c = isbn.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
            int digit = c - '0';
            if (i % 2 == 0) {
                sum += digit;
            } else {
                sum += digit * 3;
            }
        }

        return (sum % 10 == 0);
    }
}
