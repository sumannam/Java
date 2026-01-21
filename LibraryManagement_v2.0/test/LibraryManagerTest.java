import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryManagerTest {

    private LibraryManager manager;
    private LibraryRepository repository;

    @BeforeEach
    void setUp() {
        // 테스트용 레포지토리와 매니저 초기화
        repository = new LibraryRepository();
        manager = new LibraryManager(repository);

        // 매니저 초기화 (파일 로드)
        manager.initialize();

        // 테스트를 위한 초기 데이터 강제 주입 (필요 시)
        // 실제 파일 없이 로직만 테스트하고 싶다면 Mock 객체를 사용하거나
        // 테스트용 도서를 직접 등록합니다.
        manager.getBookMap().clear();
        manager.addBook("테스트 자바", "저자A"); // ID: 1
    }

    @Test
    @DisplayName("로그인 성공 및 실패 테스트")
    void login() {
        // Given: users.csv에 admin/1111 데이터가 있다고 가정
        // When & Then
        assertTrue(manager.login("admin", "1111"), "관리자 로그인이 성공해야 합니다.");
        assertFalse(manager.login("admin", "wrong"), "비밀번호가 틀리면 실패해야 합니다.");
    }

    @Test
    @DisplayName("현재 로그인한 사용자 정보 확인")
    void getCurrentUser() {
        manager.login("admin", "1111");
        User user = manager.getCurrentUser();

        assertNotNull(user);
        assertEquals("admin", user.getUserId());
        assertTrue(user.isAdmin());
    }

    @Test
    @DisplayName("새로운 도서 등록 확인")
    void addBook() {
        int beforeSize = manager.getAllBooks().size();
        manager.addBook("새로운 책", "새로운 저자");

        assertEquals(beforeSize + 1, manager.getAllBooks().size());

        // 마지막 등록된 책 확인 (ID는 2가 될 것으로 예상)
        Book book = manager.getBookMap().get(2);
        assertEquals("새로운 책", book.getTitle());
    }

    @Test
    @DisplayName("도서 삭제 확인")
    void deleteBook() {
        // ID 1번 도서 삭제
        boolean result = manager.deleteBook(1);

        assertTrue(result);
        assertNull(manager.getBookMap().get(1));
    }

    @Test
    @DisplayName("도서 대출 로직 확인")
    void borrowBook() {
        manager.login("user", "2222"); // 대출자 로그인

        // 성공 케이스
        boolean success = manager.borrowBook(1);
        assertTrue(success);
        assertFalse(manager.getBookMap().get(1).isAvailable());
        assertEquals("user", manager.getBookMap().get(1).getBorrowerId());

        // 실패 케이스 (이미 대출 중인 도서)
        boolean fail = manager.borrowBook(1);
        assertFalse(fail);
    }

    @Test
    @DisplayName("도서 반납 로직 확인")
    void returnBook() {
        manager.login("user", "2222");
        manager.borrowBook(1); // 먼저 대출

        // 반납 실행
        boolean result = manager.returnBook(1);
        assertTrue(result);
        assertTrue(manager.getBookMap().get(1).isAvailable());
        assertEquals("null", manager.getBookMap().get(1).getBorrowerId());
    }

    @Test
    @DisplayName("키워드 기반 도서 검색 확인")
    void searchBook() {
        manager.addBook("파이썬 입문", "저자B");

        List<Book> results = manager.searchBook("자바");
        assertEquals(1, results.size());
        assertEquals("테스트 자바", results.get(0).getTitle());
    }

    @Test
    @DisplayName("전체 도서 목록 반환 확인")
    void getAllBooks() {
        Collection<Book> books = manager.getAllBooks();
        assertNotNull(books);
        assertFalse(books.isEmpty());
    }
}