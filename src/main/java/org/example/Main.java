package org.example;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        AppUser newUser = new AppUser(0, "example@email.com", LocalDate.of(2000, 1, 1), 0);
        newUser.addUserToDB();

        List<AppUser> allUsers = AppUser.getAllUsers();
        for (AppUser user : allUsers) {
            System.out.println("User: " + user.getEmail() + ", Age: " + user.getAge());
        }

        String userEmailToFind = "example@email.com";
        Optional<AppUser> foundUser = AppUser.findUserByEmail(userEmailToFind);

        if (foundUser.isPresent()) {
            System.out.println("User found: " + foundUser.get().getEmail());

            foundUser.get().deleteUserFromDB();
            System.out.println("User deleted: " + foundUser.get().getEmail());
        } else {
            System.out.println("User not found with email: " + userEmailToFind);
        }
    }
}
