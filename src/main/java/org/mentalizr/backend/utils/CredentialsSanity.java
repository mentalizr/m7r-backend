package org.mentalizr.backend.utils;

public class CredentialsSanity {

    public static void checkPasswordSanity(char[] password) throws BadCredentialsException {
        if (password.length == 0) {
            throw new BadCredentialsException("Empty password. Not accepted.");
        }
        if (password.length > 100) {
            throw new BadCredentialsException("Password length exceeded. Not accepted");
        }
    }

    public static void checkUsernameSanity(String username) throws BadCredentialsException {

        if (username.equals("")) {
            throw new BadCredentialsException("Login rejected due to empty username.");
        }

        if (username.length() > 50) {
            throw new BadCredentialsException("Login rejected. Username length exceeded.");
        }

        // TODO
        // Further sanity checks: illegal chars ... prevent sql injection
    }


    public static class BadCredentialsException extends Exception {
        public BadCredentialsException(String message) {
            super(message);
        }
    }

}
