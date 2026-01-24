import java.io.*;
import java.util.*;

public class LibraryRepository {
    private final String booksFile = "data/books.csv";
    private final String usersFile = "data/users.csv";

    public Map<Integer, Book> loadBooks() {
        Map<Integer, Book> bookMap = new HashMap<>();
        File file = new File(booksFile);
        if (!file.exists()) return bookMap;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length < 5) continue;

                int id = Integer.parseInt(data[0].trim());
                String title = data[1].trim();
                String author = data[2].trim();
                boolean isAvailable = Boolean.parseBoolean(data[3].trim());
                String memberId = data[4].trim();

                bookMap.put(id, new Book(id, title, author, isAvailable, memberId));
            }
        } catch (IOException e) {
            System.out.println("[오류] 도서 로딩 실패: " + e.getMessage());
        }
        return bookMap;
    }

    public void saveBooks(Map<Integer, Book> bookMap) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(booksFile))) {
            for (Book book : bookMap.values()) {
                String line = String.format("%d,%s,%s,%b,%s",
                        book.getId(), book.getTitle(), book.getAuthor(),
                        book.isAvailable(), book.getBorrowerId());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("[오류] 도서 저장 실패: " + e.getMessage());
        }
    }

    public List<User> loadUsers() {
        List<User> userList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                userList.add(new User(data[0].trim(), data[1].trim(), data[2].trim()));
            }
        } catch (IOException e) {
            System.out.println("[오류] 사용자 데이터 로딩 실패.");
        }
        return userList;
    }
}