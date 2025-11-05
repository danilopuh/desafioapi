package br.com.desafioapi.utils;

import br.com.desafioapi.models.User;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TestDataGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);
    private static final Faker faker = new Faker(Locale.forLanguageTag("pt-BR"));
    
    public static User generateValidUser() {
        return generateValidUser(false);
    }
    
    public static User generateValidUser(boolean isAdmin) {
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 20);
        String administrador = isAdmin ? "true" : "false";
        
        User user = new User(name, email, password, administrador);
        
        logger.debug("Usuário gerado: {}", user.withoutPassword());
        return user;
    }
    
    public static User generateAdminUser() {
        return generateValidUser(true);
    }
    
    public static List<User> generateMultipleUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(generateValidUser());
        }
        logger.debug("Gerados {} usuários para teste", count);
        return users;
    }
    
    public static User generateUserWithInvalidEmail() {
        User user = generateValidUser();
        user.setEmail("email-invalido"); // Email sem formato válido
        return user;
    }
    
    public static User generateUserWithEmptyFields() {
        return new User("", "", "", "");
    }
    
    public static User generateUserWithNullFields() {
        return new User(null, null, null, null);
    }
    
    public static User generateUserWithLongName() {
        User user = generateValidUser();
        // Nome muito longo (mais de 255 caracteres)
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            longName.append("a");
        }
        user.setNome(longName.toString());
        return user;
    }
    
    public static User generateUserWithSpecialCharacters() {
        User user = generateValidUser();
        user.setNome("João da Silva @#$%^&*()");
        return user;
    }
    
    public static String generateRandomString(int length) {
        return faker.lorem().characters(length);
    }
    
    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }
    
    public static String generateRandomPassword() {
        return faker.internet().password(8, 20);
    }
    
    public static String generateWeakPassword() {
        return "123"; // Senha muito fraca
    }
    
    public static String generateStrongPassword() {
        return faker.internet().password(12, 20, true, true, true);
    }
}