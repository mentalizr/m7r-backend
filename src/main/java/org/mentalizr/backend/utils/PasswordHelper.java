package org.mentalizr.backend.utils;

import org.mentalizr.backend.exceptions.IllegalServiceInputException;

public class PasswordHelper {

    public static void checkPasswordSanity(char[] password) throws IllegalServiceInputException {
        if (password.length == 0) {
            throw new IllegalServiceInputException("Empty password. Not accepted.");
        }
        if (password.length > 100) {
            throw new IllegalServiceInputException("Password length exceeded. Not accepted");
        }
    }


}
