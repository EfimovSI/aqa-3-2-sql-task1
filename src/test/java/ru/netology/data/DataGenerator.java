package ru.netology.data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class DataGenerator {

    private static final Faker faker = new Faker(new Locale("ru"));

    public User generateUser() {
        String login = faker.name().username();
        return new User(login, "qwerty123");
        // TODO: 16.05.2022 Integrate to DbUtils & Tests
    }
}
