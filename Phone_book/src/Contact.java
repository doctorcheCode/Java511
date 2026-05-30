import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Contact {
    private static int nextId = 1;
    private int id;
    private String firstName;
    private String lastName;
    private List<String> phones;
    private int age;
    private String gender;

    public Contact(String firstName, String lastName, List<String> phones, int age, String gender) {
        this.id = nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phones = new ArrayList<>(phones);
        this.age = age;
        this.gender = gender;
    }

    private Contact(int id, String firstName, String lastName, List<String> phones, int age, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phones = new ArrayList<>(phones);
        this.age = age;
        this.gender = gender;
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = new ArrayList<>(phones);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShortInfo() {
        return String.format("ID: %d | %s %s | Возраст: %d | Пол: %s | Тел: %s",
                id, firstName, lastName, age, gender, phones.get(0));
    }

    public String getFullInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Имя: ").append(firstName).append("\n");
        sb.append("Фамилия: ").append(lastName).append("\n");
        sb.append("Телефоны: ").append(String.join(", ", phones)).append("\n");
        sb.append("Возраст: ").append(age).append("\n");
        sb.append("Пол: ").append(gender);
        return sb.toString();
    }

    @Override
    public String toString() {
        return id + ":" + firstName + ":" + lastName + ":" + age + ":" + gender + ":" +
                String.join(",", phones);
    }

    public static Contact fromString(String line) {
        try {
            String[] parts = line.split(":", -1);
            if (parts.length >= 6) {
                int id = Integer.parseInt(parts[0]);
                String firstName = parts[1];
                String lastName = parts[2];
                int age = Integer.parseInt(parts[3]);
                String gender = parts[4];
                List<String> phones = Arrays.asList(parts[5].split(","));
                return new Contact(id, firstName, lastName, phones, age, gender);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при обработке контакта: " + e.getMessage());
        }
        return null;
    }
}