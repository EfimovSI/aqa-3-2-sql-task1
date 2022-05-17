package ru.netology.data;

import lombok.Value;

@Value
public class User {
    String id;
    String login;
    String password;
}
