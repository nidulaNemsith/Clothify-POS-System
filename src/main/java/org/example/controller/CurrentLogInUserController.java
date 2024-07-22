package org.example.controller;

import lombok.Data;

@Data
public class CurrentLogInUserController {
    private String id;
    private String name;
    private String role;
    private String email;

    private CurrentLogInUserController(){}
    private static CurrentLogInUserController instance;

    public static CurrentLogInUserController getInstance(){
        if (instance == null) {
            instance= new CurrentLogInUserController();
        }
        return instance;
    }
}
