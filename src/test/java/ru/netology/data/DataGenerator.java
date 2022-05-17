package ru.netology.data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class DataGenerator {

    private static final Faker faker = new Faker(new Locale("ru"));

    public static User generateUser() {
        String id = faker.lorem().characters(36,true, true);
        String login = faker.name().username();
        return new User(id, login, "$2a$10$ki.mGkhoxzrWAMNWxvpI7OE94DSVmi4TV8gUHXYvsMEp9i9kkxPg2");
        // TODO: 16.05.2022 Integrate to DbUtils & Tests
    }
}
