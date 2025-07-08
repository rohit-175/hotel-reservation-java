package Service;

import model.Customer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private static final CustomerService INSTANCE = new CustomerService();
    private final Map<String, Customer> customerMap = new HashMap<>();

    private CustomerService(){}

    public static CustomerService getInstance(){
        return INSTANCE;
    }

    public boolean addCustomer(String email, String firstName, String lastName) {
        if (customerMap.containsKey(email)) {
            System.out.println("Account already exists for email: " + email);
            return false;
        }
        customerMap.put(email, new Customer(firstName, lastName, email));
        return true;
    }


    public Customer getCustomer(String customerEmail){
        return customerMap.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers(){
        return customerMap.values();
    }

}
