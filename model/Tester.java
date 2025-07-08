package model;

public class Tester {
    public static void main(String[] args){
        Customer customer1 = new Customer("Rohit", "Saseendran", "saseendranrohit@gmail.com");
        System.out.println(customer1);

        Customer customer2 = new Customer("John", "Pork", "lol");
        System.out.println(customer2);
    }
}
