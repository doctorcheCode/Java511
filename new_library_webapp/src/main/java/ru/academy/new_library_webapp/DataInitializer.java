package ru.academy.new_library_webapp;

import org.springframework.beans.factory.annotation.Autowired;
import ru.academy.new_library_webapp.entity.Book;
import ru.academy.new_library_webapp.entity.User;
import ru.academy.new_library_webapp.repository.BookRepository;
import ru.academy.new_library_webapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           BookRepository bookRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("Создан администратор: логин=admin, пароль=admin123");
        }

        if (bookRepository.count() == 0) {
            Book book1 = new Book();
            book1.setTitle("Web-разработка для умственно отсталых");
            book1.setAuthor("Криворучко Ж.Ж");
            book1.setPublisher("Ёклмн");
            book1.setPublicationYear(2026);
            book1.setGenre("Руководство");
            book1.setDescription("Это для меня");
            bookRepository.save(book1);

            Book book2 = new Book();
            book2.setTitle("Как управлять вселенной, не привлекая внимания санитаров");
            book2.setAuthor("Черемисин Г.В");
            book2.setPublisher("Ёпрст");
            book2.setPublicationYear(2000);
            book2.setGenre("Откровение");
            book2.setDescription("О мировом правительстве и теории заговора");
            bookRepository.save(book2);

            Book book3 = new Book();
            book3.setTitle("Снафф");
            book3.setAuthor("Пелевин");
            book3.setPublisher("АСТ");
            book3.setPublicationYear(2010);
            book3.setGenre("Фентези");
            book3.setDescription("Книга о том, как у женщины-андроида появилась душа и что из этого вышло...");
            bookRepository.save(book3);

            System.out.println("Добавлены примеры книг.");
        }
    }
}