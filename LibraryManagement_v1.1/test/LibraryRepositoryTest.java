import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LibraryRepositoryTest {

    private LibraryRepository repository;
    private final String TEST_BOOKS_FILE = "data/books.csv"; // 경로 확인 필요
    private final String TEST_USERS_FILE = "data/users.csv";

    @BeforeEach
    void setUp() {
        repository = new LibraryRepository();
        // 테스트 시작 전 data 폴더가 없다면 생성
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();
    }

    @Test
    @DisplayName("도서 데이터 저장 테스트")
    void saveBooks() {
        // Given: 저장할 도서 데이터 준비
        Map<Integer, Book> bookMap = new HashMap<>();
        bookMap.put(1, new Book(1, "테스트 도서", "저자A", true, "null"));

        // When: 저장 실행
        repository.saveBooks(bookMap);

        // Then: 파일이 존재하고 내용이 비어있지 않은지 확인
        File file = new File(TEST_BOOKS_FILE);
        assertTrue(file.exists(), "파일이 생성되어야 합니다.");
        assertTrue(file.length() > 0, "파일에 데이터가 기록되어야 합니다.");
    }

    @Test
    @DisplayName("파일로부터 도서 데이터 로드 테스트")
    void loadBooks() {
        // Given: 먼저 파일을 직접 생성하여 저장해둠
        Map<Integer, Book> originalMap = new HashMap<>();
        originalMap.put(1, new Book(1, "로드 테스트", "저자B", false, "user01"));
        repository.saveBooks(originalMap);

        // When: 로드 실행
        Map<Integer, Book> loadedMap = repository.loadBooks();

        // Then: 로드된 데이터 검증
        assertNotNull(loadedMap);
        assertEquals(1, loadedMap.size());

        Book loadedBook = loadedMap.get(1);
        assertEquals("로드 테스트", loadedBook.getTitle());
        assertEquals("저자B", loadedBook.getAuthor());
        assertFalse(loadedBook.isAvailable());
        assertEquals("user01", loadedBook.getBorrowerId());
    }

    @Test
    @DisplayName("사용자 데이터 로드 테스트")
    void loadUsers() {
        // Given: users.csv 파일이 존재한다고 가정 (없을 경우를 대비해 Mock 데이터 작성 로직 추가 가능)
        // When: 로드 실행
        List<User> users = repository.loadUsers();

        // Then: 리스트가 null이 아니고 최소한의 계정(admin 등)이 있는지 확인
        assertNotNull(users, "사용자 리스트는 null일 수 없습니다.");

        // 첫 번째 계정이 admin인지 확인 (파일 내용에 의존적)
        if (!users.isEmpty()) {
            boolean hasAdmin = users.stream().anyMatch(u -> u.getUserId().equals("admin"));
            assertTrue(hasAdmin, "사용자 목록에 admin 계정이 포함되어야 합니다.");
        }
    }
}