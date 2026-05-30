import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class PhoneBook {
    private static final String USERS_FILE = "users.txt";
    private static final String LOGGER_FILE = "logger.txt";
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static List<Contact> contacts = new ArrayList<>();

    static void main(String[] args) {

        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showAuthMenu() {
        System.out.println("МЕНЮ АВТОРИЗАЦИИ:");
        System.out.println("1. Вход");
        System.out.println("2. Регистрация");
        System.out.println("3. Выход");
        System.out.print("Выберите опцию: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            case "3":
                logAction("Программа завершена");
                System.out.println("До свидания!");
                System.exit(0);
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }

    private static void register() {
        System.out.println("РЕГИСТРАЦИЯ:");
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();

        if (isUserExists(username)) {
            System.out.println("Пользователь с таким логином уже существует!");
            return;
        }

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);
        saveUser(newUser);

        logAction("Зарегистрирован новый пользователь: " + username);
        System.out.println("Регистрация прошла успешно!");
    }

    private static void login() {
        System.out.println("ВХОД В СИСТЕМУ");
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if (authenticateUser(username, password)) {
            currentUser = new User(username, password);
            loadContactsForUser(username);
            logAction("Пользователь " + username + " вошел в систему");
            System.out.println("Добро пожаловать, " + username + "!");
        } else {
            System.out.println("Неверный логин или пароль!");
        }
    }

    private static boolean isUserExists(String username) {
        File file = new File(USERS_FILE);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла пользователей: " + e.getMessage());
        }
        return false;
    }

    private static void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(user.getUsername() + ":" + user.getPassword());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    private static boolean authenticateUser(String username, String password) {
        File file = new File(USERS_FILE);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла пользователей: " + e.getMessage());
        }
        return false;
    }

    private static void loadContactsForUser(String username) {
        contacts.clear();
        String filename = username + ".txt";
        File file = new File(filename);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Contact contact = Contact.fromString(line);
                if (contact != null) {
                    contacts.add(contact);
                }
            }
            logAction("Загружено " + contacts.size() + " контактов для пользователя " + username);
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке контактов: " + e.getMessage());
        }
    }

    private static void saveContactsForCurrentUser() {
        if (currentUser == null) return;

        String filename = currentUser.getUsername() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Contact contact : contacts) {
                writer.write(contact.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении контактов: " + e.getMessage());
        }
    }

    private static void logAction(String action) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOGGER_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + action);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Ошибка при записи в лог: " + e.getMessage());
        }
    }

    private static void showMainMenu() {
        System.out.println("ГЛАВНОЕ МЕНЮ - Пользователь: " + currentUser.getUsername());
        System.out.println("1. Контакты");
        System.out.println("2. Поиск");
        System.out.println("3. Фильтрация");
        System.out.println("4. Сортировка");
        System.out.println("5. Логгер");
        System.out.println("6. Выход");
        System.out.print("Выберите опцию: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                showContactsMenu();
                break;
            case "2":
                showSearchMenu();
                break;
            case "3":
                showFilterMenu();
                break;
            case "4":
                showSortMenu();
                break;
            case "5":
                showLogger();
                break;
            case "6":
                logout();
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }

    private static void showContactsMenu() {
        while (true) {
            System.out.println("МЕНЮ КОНТАКТОВ");
            System.out.println("1. Добавить контакт");
            System.out.println("2. Удалить контакт");
            System.out.println("3. Редактировать контакт");
            System.out.println("4. Отобразить все контакты");
            System.out.println("5. Назад");
            System.out.print("Выберите опцию: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addContact();
                    break;
                case "2":
                    deleteContact();
                    break;
                case "3":
                    editContact();
                    break;
                case "4":
                    displayAllContacts();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void addContact() {
        System.out.println("ДОБАВЛЕНИЕ НОВОГО КОНТАКТА");

        try {
            System.out.print("Введите имя: ");
            String firstName = scanner.nextLine();
            if (firstName.trim().isEmpty()) {
                System.out.println("Имя не может быть пустым!");
                return;
            }

            System.out.print("Введите фамилию: ");
            String lastName = scanner.nextLine();

            List<String> phones = new ArrayList<>();
            while (true) {
                System.out.print("Введите номер телефона (или 'стоп' для завершения): ");
                String phone = scanner.nextLine();
                if (phone.equalsIgnoreCase("стоп")) {
                    break;
                }
                if (isValidPhone(phone)) {
                    phones.add(phone);
                } else {
                    System.out.println("Некорректный номер телефона. Номер должен содержать только цифры и может начинаться с +");
                }
            }

            if (phones.isEmpty()) {
                System.out.println("Должен быть хотя бы один номер телефона!");
                return;
            }

            System.out.print("Введите возраст: ");
            int age;
            try {
                age = Integer.parseInt(scanner.nextLine());
                if (age <= 0 || age > 150) {
                    System.out.println("Некорректный возраст!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Возраст должен быть числом!");
                return;
            }

            System.out.print("Введите пол (М/Ж): ");
            String gender = scanner.nextLine().toUpperCase();
            if (!gender.equals("М") && !gender.equals("Ж")) {
                System.out.println("Пол должен быть М или Ж!");
                return;
            }

            Contact contact = new Contact(firstName, lastName, phones, age, gender);
            contacts.add(contact);
            saveContactsForCurrentUser();

            logAction("Добавлен контакт: " + firstName + " " + lastName + " (ID: " + contact.getId() + ")");
            System.out.println("Контакт успешно добавлен с ID: " + contact.getId());

        } catch (Exception e) {
            System.out.println("Ошибка при добавлении контакта: " + e.getMessage());
        }
    }

    private static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        String cleaned = phone.replace("+", "");
        return cleaned.matches("\\d+");
    }

    private static void deleteContact() {
        if (contacts.isEmpty()) {
            System.out.println("Список контактов пуст!");
            return;
        }

        System.out.println("УДАЛЕНИЕ КОНТАКТА");
        System.out.println("1. По ID");
        System.out.println("2. По номеру телефона");
        System.out.println("3. По имени пользователя");
        System.out.print("Выберите способ удаления: ");

        String choice = scanner.nextLine();
        boolean deleted = false;

        switch (choice) {
            case "1":
                System.out.print("Введите ID контакта: ");
                try {
                    int id = Integer.parseInt(scanner.nextLine());
                    Optional<Contact> contact = contacts.stream()
                            .filter(c -> c.getId() == id)
                            .findFirst();

                    if (contact.isPresent()) {
                        System.out.println("Найден контакт: " + contact.get().getFirstName() + " " + contact.get().getLastName());
                        System.out.print("Подтвердите удаление (да/нет): ");
                        if (scanner.nextLine().equalsIgnoreCase("да")) {
                            contacts.remove(contact.get());
                            logAction("Удален контакт с ID: " + id);
                            deleted = true;
                        }
                    } else {
                        System.out.println("Контакт с ID " + id + " не найден!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Некорректный ID!");
                }
                break;

            case "2":
                System.out.print("Введите номер телефона: ");
                String phone = scanner.nextLine();
                List<Contact> foundByPhone = contacts.stream()
                        .filter(c -> c.getPhones().contains(phone))
                        .toList();

                if (!foundByPhone.isEmpty()) {
                    System.out.println("Найдено контактов: " + foundByPhone.size());
                    for (Contact c : foundByPhone) {
                        System.out.println(c.getShortInfo());
                    }
                    System.out.print("Подтвердите удаление всех найденных контактов (да/нет): ");
                    if (scanner.nextLine().equalsIgnoreCase("да")) {
                        contacts.removeAll(foundByPhone);
                        logAction("Удалено " + foundByPhone.size() + " контактов с номером: " + phone);
                        deleted = true;
                    }
                } else {
                    System.out.println("Контакты с номером " + phone + " не найдены!");
                }
                break;

            case "3":
                System.out.print("Введите имя: ");
                String firstName = scanner.nextLine();
                System.out.print("Введите фамилию: ");
                String lastName = scanner.nextLine();

                List<Contact> foundByName = contacts.stream()
                        .filter(c -> c.getFirstName().equalsIgnoreCase(firstName) &&
                                c.getLastName().equalsIgnoreCase(lastName))
                        .toList();

                if (!foundByName.isEmpty()) {
                    System.out.println("Найдено контактов: " + foundByName.size());
                    for (Contact c : foundByName) {
                        System.out.println(c.getShortInfo());
                    }
                    System.out.print("Подтвердите удаление всех найденных контактов (да/нет): ");
                    if (scanner.nextLine().equalsIgnoreCase("да")) {
                        contacts.removeAll(foundByName);
                        logAction("Удалено " + foundByName.size() + " контактов с именем: " + firstName + " " + lastName);
                        deleted = true;
                    }
                } else {
                    System.out.println("Контакты с именем " + firstName + " " + lastName + " не найдены!");
                }
                break;

            default:
                System.out.println("Неверный выбор!");
        }

        if (deleted) {
            saveContactsForCurrentUser();
            System.out.println("Контакт(ы) успешно удален(ы)!");
        }
    }

    private static void editContact() {
        if (contacts.isEmpty()) {
            System.out.println("Список контактов пуст!");
            return;
        }

        System.out.println("РЕДАКТИРОВАНИЕ КОНТАКТА");
        System.out.print("Введите ID контакта для редактирования: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            Optional<Contact> contactOpt = contacts.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst();

            if (contactOpt.isPresent()) {
                Contact contact = contactOpt.get();
                System.out.println("Текущие данные: " + contact.getFullInfo());
                System.out.println("Оставьте поле пустым, чтобы оставить без изменений");

                System.out.print("Новое имя (" + contact.getFirstName() + "): ");
                String firstName = scanner.nextLine();
                if (!firstName.trim().isEmpty()) {
                    contact.setFirstName(firstName);
                }

                System.out.print("Новая фамилия (" + contact.getLastName() + "): ");
                String lastName = scanner.nextLine();
                if (!lastName.trim().isEmpty()) {
                    contact.setLastName(lastName);
                }

                System.out.println("Текущие номера: " + String.join(", ", contact.getPhones()));
                System.out.print("Хотите изменить номера? (да/нет): ");
                if (scanner.nextLine().equalsIgnoreCase("да")) {
                    List<String> newPhones = new ArrayList<>();
                    while (true) {
                        System.out.print("Введите номер телефона (или 'стоп' для завершения): ");
                        String phone = scanner.nextLine();
                        if (phone.equalsIgnoreCase("стоп")) {
                            break;
                        }
                        if (isValidPhone(phone)) {
                            newPhones.add(phone);
                        } else {
                            System.out.println("Некорректный номер телефона.");
                        }
                    }
                    if (!newPhones.isEmpty()) {
                        contact.setPhones(newPhones);
                    }
                }

                System.out.print("Новый возраст (" + contact.getAge() + "): ");
                String ageStr = scanner.nextLine();
                if (!ageStr.trim().isEmpty()) {
                    try {
                        int age = Integer.parseInt(ageStr);
                        if (age > 0 && age <= 150) {
                            contact.setAge(age);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Некорректный возраст, оставлен текущий");
                    }
                }

                System.out.print("Новый пол (" + contact.getGender() + "): ");
                String gender = scanner.nextLine().toUpperCase();
                if (!gender.trim().isEmpty() && (gender.equals("М") || gender.equals("Ж"))) {
                    contact.setGender(gender);
                }

                saveContactsForCurrentUser();
                logAction("Отредактирован контакт с ID: " + id);
                System.out.println("Контакт успешно обновлен!");

            } else {
                System.out.println("Контакт с ID " + id + " не найден!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ID!");
        }
    }

    private static void displayAllContacts() {
        if (contacts.isEmpty()) {
            System.out.println("\nСписок контактов пуст!");
            return;
        }

        System.out.println("ВСЕ КОНТАКТЫ");
        System.out.println();
        System.out.println("Всего контактов: " + contacts.size());
        System.out.println();
        for (Contact contact : contacts) {
            System.out.println(contact.getFullInfo());
            System.out.println();
        }
    }

    private static void showSearchMenu() {
        System.out.println("ПОИСК КОНТАКТОВ");
        System.out.println("1. По имени");
        System.out.println("2. По фамилии");
        System.out.println("3. По номеру");
        System.out.println("4. По всем параметрам");
        System.out.println("5. Спец поиск (_, %)");
        System.out.print("Выберите опцию: ");

        String choice = scanner.nextLine();
        boolean ignoreCase = false;

        if (choice.equals("0")) {
            ignoreCase = true;
            System.out.print("Выберите тип поиска (1-5): ");
            choice = scanner.nextLine();
        }

        List<Contact> results = new ArrayList<>();

        switch (choice) {
            case "1":
                System.out.print("Введите имя для поиска: ");
                String firstName = scanner.nextLine();
                results = searchByFirstName(firstName, ignoreCase);
                break;
            case "2":
                System.out.print("Введите фамилию для поиска: ");
                String lastName = scanner.nextLine();
                results = searchByLastName(lastName, ignoreCase);
                break;
            case "3":
                System.out.print("Введите номер для поиска: ");
                String phone = scanner.nextLine();
                results = searchByPhone(phone);
                break;
            case "4":
                System.out.print("Введите текст для поиска по всем полям: ");
                String text = scanner.nextLine();
                results = searchAllFields(text, ignoreCase);
                break;
            case "5":
                System.out.print("Введите шаблон (_ - один символ, % - любая последовательность): ");
                String pattern = scanner.nextLine();
                results = specialSearch(pattern, ignoreCase);
                break;
            default:
                System.out.println("Неверный выбор!");
                return;
        }

        displaySearchResults(results);
    }

    private static List<Contact> searchByFirstName(String firstName, boolean ignoreCase) {
        if (ignoreCase) {
            return contacts.stream()
                    .filter(c -> c.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                    .toList();
        } else {
            return contacts.stream()
                    .filter(c -> c.getFirstName().contains(firstName))
                    .toList();
        }
    }

    private static List<Contact> searchByLastName(String lastName, boolean ignoreCase) {
        if (ignoreCase) {
            return contacts.stream()
                    .filter(c -> c.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                    .toList();
        } else {
            return contacts.stream()
                    .filter(c -> c.getLastName().contains(lastName))
                    .toList();
        }
    }

    private static List<Contact> searchByPhone(String phone) {
        return contacts.stream()
                .filter(c -> c.getPhones().stream().anyMatch(p -> p.contains(phone)))
                .toList();
    }

    private static List<Contact> searchAllFields(String text, boolean ignoreCase) {
        String searchText = ignoreCase ? text.toLowerCase() : text;

        return contacts.stream()
                .filter(c -> {
                    if (ignoreCase) {
                        return c.getFirstName().toLowerCase().contains(searchText) ||
                                c.getLastName().toLowerCase().contains(searchText) ||
                                c.getPhones().stream().anyMatch(p -> p.toLowerCase().contains(searchText));
                    } else {
                        return c.getFirstName().contains(searchText) ||
                                c.getLastName().contains(searchText) ||
                                c.getPhones().stream().anyMatch(p -> p.contains(searchText));
                    }
                })
                .toList();
    }

    private static List<Contact> specialSearch(String pattern, boolean ignoreCase) {
        String regex = pattern
                .replace("_", ".")
                .replace("%", ".*");

        String finalRegex = ignoreCase ? "(?i)" + regex : regex;

        return contacts.stream()
                .filter(c -> c.getFirstName().matches(finalRegex) ||
                        c.getLastName().matches(finalRegex) ||
                        c.getPhones().stream().anyMatch(p -> p.matches(finalRegex)))
                .toList();
    }

    private static void displaySearchResults(List<Contact> results) {
        if (results.isEmpty()) {
            System.out.println("Ничего не найдено!");
            return;
        }

        System.out.println("РЕЗУЛЬТАТЫ ПОИСКА (" + results.size() + ")");
        for (Contact contact : results) {
            System.out.println(contact.getShortInfo());
        }
    }

    private static void showFilterMenu() {
        System.out.println("ФИЛЬТРАЦИЯ КОНТАКТОВ");
        System.out.println("1. Только мужчины");
        System.out.println("2. Только женщины");
        System.out.println("3. Возраст больше N");
        System.out.println("4. Возраст меньше N");
        System.out.print("Выберите опцию: ");

        String choice = scanner.nextLine();
        List<Contact> filtered = new ArrayList<>();

        switch (choice) {
            case "1":
                filtered = contacts.stream()
                        .filter(c -> c.getGender().equals("М"))
                        .toList();
                break;
            case "2":
                filtered = contacts.stream()
                        .filter(c -> c.getGender().equals("Ж"))
                        .toList();
                break;
            case "3":
                System.out.print("Введите возраст: ");
                try {
                    int age = Integer.parseInt(scanner.nextLine());
                    filtered = contacts.stream()
                            .filter(c -> c.getAge() > age)
                            .toList();
                } catch (NumberFormatException e) {
                    System.out.println("Некорректный возраст!");
                    return;
                }
                break;
            case "4":
                System.out.print("Введите возраст: ");
                try {
                    int age = Integer.parseInt(scanner.nextLine());
                    filtered = contacts.stream()
                            .filter(c -> c.getAge() < age)
                            .toList();
                } catch (NumberFormatException e) {
                    System.out.println("Некорректный возраст!");
                    return;
                }
                break;
            default:
                System.out.println("Неверный выбор!");
                return;
        }

        System.out.println("РЕЗУЛЬТАТЫ ФИЛЬТРАЦИИ (" + filtered.size() + ")");
        for (Contact contact : filtered) {
            System.out.println(contact.getShortInfo());
        }
    }

    private static void showSortMenu() {
        if (contacts.isEmpty()) {
            System.out.println("Список контактов пуст!");
            return;
        }

        System.out.println("СОРТИРОВКА КОНТАКТОВ");
        System.out.println("1. По имени (А-Я)");
        System.out.println("2. По имени (Я-А)");
        System.out.println("3. По фамилии");
        System.out.println("4. По номеру телефона");
        System.out.print("Выберите опцию: ");

        String choice = scanner.nextLine();
        List<Contact> sorted = new ArrayList<>(contacts);

        switch (choice) {
            case "1":
                sorted.sort(Comparator.comparing(Contact::getFirstName));
                break;
            case "2":
                sorted.sort(Comparator.comparing(Contact::getFirstName).reversed());
                break;
            case "3":
                sorted.sort(Comparator.comparing(Contact::getLastName));
                break;
            case "4":
                sorted.sort(Comparator.comparing(c -> c.getPhones().get(0)));
                break;
            default:
                System.out.println("Неверный выбор!");
                return;
        }

        System.out.println("ОТСОРТИРОВАННЫЕ КОНТАКТЫ");
        for (Contact contact : sorted) {
            System.out.println(contact.getShortInfo());
        }
    }

    private static void showLogger() {
        System.out.println("ПОСЛЕДНИЕ ДЕЙСТВИЯ В ЛОГГЕРЕ");
        File file = new File(LOGGER_FILE);

        if (!file.exists()) {
            System.out.println("Лог-файл не найден!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            // Показываем последние 20 записей
            int start = Math.max(0, lines.size() - 20);
            for (int i = start; i < lines.size(); i++) {
                System.out.println(lines.get(i));
            }

            System.out.println("Всего записей: " + lines.size());

        } catch (IOException e) {
            System.out.println("Ошибка при чтении лог-файла: " + e.getMessage());
        }

        System.out.println("Нажмите Enter для продолжения...");
        scanner.nextLine();
    }

    private static void logout() {
        logAction("Пользователь " + currentUser.getUsername() + " вышел из системы");
        currentUser = null;
        contacts.clear();
        System.out.println("Вы вышли из системы.");
    }
}


