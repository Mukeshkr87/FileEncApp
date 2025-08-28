package Views;

import dao.UserDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;


public class Welcome {
    public void welcomeScreen() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the file management System");
        System.out.println("Press 1 to Login");
        System.out.println("Press 2 to SignUp");
        System.out.println("Press 0 to exit the application");
        int choice = 0;
        try{
            choice = Integer.parseInt(br.readLine());
        }catch (IOException ex){
            ex.printStackTrace();
        }
        switch (choice) {
            case 1 -> login();
            case 2 -> signUp();
            case 0 -> System.exit(0);
        }
    }
    private void login() {
        System.out.println("Enter your email");
        Scanner sc = new Scanner(System.in);
        String email = sc.nextLine();
        try{
            if(UserDAO.isExist(email)){
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email,genOTP);
                System.out.println("Enter the OTP");
                String otp = sc.nextLine();
                if(otp.equals(genOTP)){
                    new UserView(email).home();
                }else{
                    System.out.println("Wrong OTP");
                }
            }else{
                System.out.println("User doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void signUp() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your email");
        String email = sc.nextLine();
        try {
            if (UserDAO.isExist(email)) {
                System.out.println("User already exists, redirecting to login...");
                login(); // directly redirect to login
                return;  // exit signup early
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error while checking user.");
            return;
        }
        System.out.println("Enter your name");
        String name = sc.nextLine();
        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email,genOTP);
        System.out.println("Enter the OTP");
        String otp = sc.nextLine();
        if(otp.equals(genOTP)){
            User user = new User(name,email);
            int response = UserService.saveUser(user);
            switch (response) {
                case 1 -> System.out.println("User Registered Successfully");
                case 0 -> System.out.println("User already exists");
                case -1 -> System.out.println("Error saving user. Please try again.");
                default -> System.out.println("Unexpected response from service.");
            }
        }else{
            System.out.println("Wrong OTP");
        }
    }
}
