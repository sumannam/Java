import java.util.*;

public class LibraryManager {
    private Map<Integer, Book> bookMap;
    private List<User> userList;
    private User currentUser;
    private LibraryRepository repository;
    private int bookCount = 0;

    public LibraryManager(LibraryRepository repository) {
        this.repository = repository;
    }

    public void initialize() {
        this.bookMap = repository.loadBooks();
        // ID 카운트 동기화
        for (Integer id : bookMap.keySet()) {
            if (id > bookCount) bookCount = id;
        }
    }

    public boolean login(String id, String pw) {
        // 기존에 List<String>으로 받던 부분을 User로 변경
//        this.userList = repository.loadLogin(id, pw);
        User user = repository.loadUser(id, pw);

        if (user != null) {
            this.currentUser = user; // 로그인 성공 시 현재 사용자 저장
            return true;
        }
        return false;
    }

    public User getCurrentUser() { return currentUser; }

    public void addBook(String title, String author) {
        bookCount++;
        bookMap.put(bookCount, new Book(bookCount, title, author, true, "null"));
        System.out.println("-----------------------------------------------------------");
        System.out.printf("[결과] 등록이 완료되었습니다. (도서 ID: %d)\n", bookCount);
    }

    public boolean editBook(int id, String title, String author) {
        if (!bookMap.containsKey(id)) return false;
        Book book = bookMap.get(id);
        // 제목이나 저자가 비어있지 않을 때만 수정 (기존 로직 유지)
        return true;
    }

    public boolean deleteBook(int id) {
        return bookMap.remove(id) != null;
    }

    public boolean borrowBook(int id) {
        if (!bookMap.containsKey(id))
            return false;

        Book book = bookMap.get(id);
        if (book.isAvailable()) {
            book.setAvailable(false);
            book.setBorrowerId(currentUser.getUserId());
            return true;
        }
        return false;
    }

    public boolean returnBook(int id) {
        if (!bookMap.containsKey(id))
            return false;

        Book book = bookMap.get(id);
        if (!book.isAvailable()) {
            book.setAvailable(true);
            book.setBorrowerId("null");
            return true;
        }
        return false;
    }

    public List<Book> searchBook(String keyword) {
        List<Book> found = new ArrayList<>();
        for (Book book : bookMap.values()) {
            if (book.getTitle().contains(keyword)) found.add(book);
        }
        return found;
    }

    public Collection<Book> getAllBooks() { return bookMap.values(); }

    public int getBookCount() { return bookCount; }

    public void saveChanges() { repository.saveBooks(bookMap); }

    // 기존 UI 로직 지원을 위한 메서드
    public Map<Integer, Book> getBookMap() { return bookMap; }
}