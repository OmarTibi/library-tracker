
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<Book> books = new ArrayList<>();
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        // Load saved books when the program starts
        books = BookStore.loadBooks();

        System.out.println("Welcome to Library Tracker!");

        boolean running = true;

        while (running) {

            System.out.println("\n===== LIBRARY TRACKER =====");
            System.out.println("1. Add Book");
            System.out.println("2. List Books");
            System.out.println("3. Search Book");
            System.out.println("4. Remove Book");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                case 1:
                    addBook();
                    break;

                case 2:
                    listBooks();
                    break;

                case 3:
                    searchBook();
                    break;

                case 4:
                    removeBook();
                    break;

                case 5:
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }

        input.close();
    }

    // Add a new book
    static void addBook() {

        System.out.print("Enter book ID: ");
        int id = input.nextInt();
        input.nextLine();

        System.out.print("Enter book title: ");
        String title = input.nextLine();

        System.out.print("Enter author name: ");
        String author = input.nextLine();

        books.add(new Book(id, title, author));

        BookStore.saveBooks(books);

        System.out.println("Book added successfully!");
    }

    // Display all books
    static void listBooks() {

        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.println("\n===== BOOK LIST =====");

        for (Book book : books) {

            System.out.println(
                "ID: " + book.getId() +
                " | Title: " + book.getTitle() +
                " | Author: " + book.getAuthor() +
                " | Available: " + book.isAvailable()
            );
        }
    }

    // Search for a book by title
    static void searchBook() {

        System.out.print("Enter book title: ");
        String title = input.nextLine();

        for (Book book : books) {

            if (book.getTitle().equalsIgnoreCase(title)) {

                System.out.println("Book found!");
                System.out.println("ID: " + book.getId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());

                return;
            }
        }

        System.out.println("Book not found.");
    }

    // Remove a book by ID
    static void removeBook() {

        System.out.print("Enter book ID: ");
        int id = input.nextInt();
        input.nextLine();

        boolean removed = books.removeIf(
            book -> book.getId() == id
        );

        if (removed) {

            BookStore.saveBooks(books);

            System.out.println("Book removed successfully!");

        } else {

            System.out.println("Book not found.");
        }
    }
}
