package nl.leonw.demo.springbootaws1;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

// Not a test, just a way to encode a password
public class EncodePasswordApp {

    public static void main(String[] args) {
        // Also look at: https://github.com/lwiddershoven/secured-actuator#hashing-the-password
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //
        // This is the nice way.
        // Unfortunately it cannot be run from Intellij as the System.console() API is
        // not easy to properly intercept. Which is not really surprising as it has a
        // getPassword method, but still a nuisance.
        //
        //        var console = System.console();
        //        if (console == null) {
        //            throw new NullPointerException("No console found");
        //        }
        //        var password = System.console().readPassword("Enter password to encode: ");
        //        if (password != null) {
        //            System.out.println(encoder.encode(new String(password)));
        //        } else {
        //            System.err.println("No password entered");
        //        }

        // So, the hardcoded way.
        // DO NOT CHECK IN YOUR SUPER SECRET PASSWORD
        System.out.println("Your encoded password: " + encoder.encode("my super secret password that I shall not commit to version control"));
    }
}
