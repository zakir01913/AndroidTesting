package com.zakir.androidtesting.utils;

import com.zakir.androidtesting.persistence.User;

/**
 * Created by Zakir Hossain on 7/28/18.
 */
public class UserTestUtils {

    public static boolean compareUser(User user1, User user2) {
        boolean flag = true;
        flag &= user1.getEmail().equals(user2.getEmail());
        flag &= user1.getFirstName().equals(user2.getFirstName());
        flag &= user1.getLastName().equals(user2.getLastName());
        return flag;
    }

    public static User createUserWithNullFirstName() {
        return new User(null, "lastName", "email");
    }

    public static User createUserWithEmptyFirstName() {
        return new User("", "lastName", "email");
    }

    public static User createUserWithNullLastName() {
        return new User("FirstName", null, "email");
    }

    public static User createUserWithEmptyLastName() {
        return new User("FirstName", "", "email");
    }

    public static User createUserWithNullEmail() {
        return new User("FirstName", "LastName", null);
    }

    public static User createUserWithInvalidEmail() {
        return new User("FirstName", "LastName", "email");
    }

    public static User createValidUser() {
        return new User("FirstName", "LastName", "email@example.com");
    }
}
