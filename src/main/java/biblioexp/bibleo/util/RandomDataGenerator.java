package biblioexp.bibleo.util;
import java.util.Random;

public class RandomDataGenerator {
    private static final Random random = new Random();

    public static long generateRandomLong(int upperBound) {
        return random.nextInt(upperBound);
    }

    public static String generateRandomDate() {
        int year = 2000 + random.nextInt(23);
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28);
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public static String generateRandomAuthor() {
        String[] authors = {"Author A", "Author B", "Author C", "Author D"};
        return authors[random.nextInt(authors.length)];
    }
}
