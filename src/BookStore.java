
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookStore {

    private static final Path FILE_PATH =
            Path.of("data", "books.json");

    public static void saveBooks(ArrayList<Book> books) {
        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);

            json.append("  {\n")
                .append("    \"id\": ").append(b.getId()).append(",\n")
                .append("    \"title\": \"").append(escape(b.getTitle())).append("\",\n")
                .append("    \"author\": \"").append(escape(b.getAuthor())).append("\",\n")
                .append("    \"available\": ").append(b.isAvailable()).append("\n")
                .append("  }");

            if (i < books.size() - 1) json.append(",");
            json.append("\n");
        }

        json.append("]\n");

        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.writeString(FILE_PATH, json.toString());
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    public static ArrayList<Book> loadBooks() {
        ArrayList<Book> books = new ArrayList<>();

        try {
            if (!Files.exists(FILE_PATH)) return books;

            String json = Files.readString(FILE_PATH);

            Pattern p = Pattern.compile(
                "\\{\\s*\"id\"\\s*:\\s*(\\d+)\\s*,\\s*" +
                "\"title\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"\\s*,\\s*" +
                "\"author\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"\\s*,\\s*" +
                "\"available\"\\s*:\\s*(true|false)\\s*\\}"
            );

            Matcher m = p.matcher(json);

            while (m.find()) {
                Book b = new Book(
                    Integer.parseInt(m.group(1)),
                    unescape(m.group(2)),
                    unescape(m.group(3))
                );

                b.setAvailable(Boolean.parseBoolean(m.group(4)));
                books.add(b);
            }

        } catch (IOException e) {
            System.out.println("Load error: " + e.getMessage());
        }

        return books;
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String unescape(String s) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '\\' && i + 1 < s.length()) {
                char next = s.charAt(++i);

                switch (next) {
                    case 'n': result.append('\n'); break;
                    case 'r': result.append('\r'); break;
                    case 't': result.append('\t'); break;
                    case '"': result.append('"'); break;
                    case '\\': result.append('\\'); break;
                    default: result.append('\\').append(next);
                }
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }
}
