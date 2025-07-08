package model;
import java.util.regex.Pattern;

public class Customer {
    String firstName;
    String lastName;
    String email;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public Customer(String firstName, String lastName, String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
    @Override
    public String toString() { return "First Name: " + firstName +
            " Last Name: " + lastName +
            " Email: " + email;}
}
