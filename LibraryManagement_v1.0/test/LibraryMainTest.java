import org.junit.jupiter.api.*;
import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LibraryMainTest {

    // 콘솔 출력을 캡처하기 위한 스트림 변수
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private final String TEST_FILE_PATH = "data/books_test.csv";

    @BeforeEach
    void setUp() {
        try {
            File file = new File(TEST_FILE_PATH);

            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            LibraryMain.bookMap.clear();

            // 테스트 데이터 준비
            LibraryMain.bookMap.clear();

            ArrayList<Object> book1 = new ArrayList<>(Arrays.asList("어린왕자", "생텍쥐페리", true));
            ArrayList<Object> book2 = new ArrayList<>(Arrays.asList("자바의 정석", "남궁성", false));

            LibraryMain.bookMap.put(1, book1);
            LibraryMain.bookMap.put(2, book2);

        } catch (IOException e) {
            // 테스트 환경 구축 실패 시 강제로 테스트 실패 처리
            org.junit.jupiter.api.Assertions.fail("파일 생성 중 오류 발생: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        // 테스트가 끝난 후 생성된 파일 삭제 (테스트 환경 정리)
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @BeforeEach
    void setUpStreams() {
        // 테스트 동안 System.out이 콘솔이 아닌 outContent(메모리)로 향하게 설정
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        // 테스트 종료 후 다시 원래의 콘솔(System.out)로 복구
        System.setOut(originalOut);
    }

    // System.in 시뮬레이션 및 Scanner 초기화 함수
    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        LibraryMain.sc = new Scanner(System.in);
    }

    @Test
    @DisplayName("관리자 로그인 성공 (기존 CSV 사용)")
    void testAdminLoginSuccess() {
        String input = "admin\n1111\n";
        provideInput(input);

        String result = LibraryMain.login();
        assertEquals("ADMIN", result);
    }

    @Test
    @DisplayName("일반 사용자 로그인 성공 (기존 CSV 사용)")
    void testUserLoginSuccess() {
        String input = "user\n2222\n";
        provideInput(input);

        String result = LibraryMain.login();
        assertEquals("USER", result);
    }

    @Test
    @DisplayName("파일 로드 후 bookMap 데이터 적재 확인")
    void testMapIsNotEmptyAfterLoad() {
        // When: 파일 불러오기 실행
        LibraryMain.loadBooksFromCSV();

        // Then: bookMap이 비어있지 않은지(사이즈가 1 이상인지) 검증
        assertFalse(LibraryMain.bookMap.isEmpty(), "데이터를 불러온 후에는 bookMap이 비어있으면 안 됩니다.");
        assertTrue(LibraryMain.bookMap.size() >= 1, "최소 한 권 이상의 도서가 로드되어야 합니다.");
    }


    @Test
    @DisplayName("관리자 전용 메뉴 출력 검증")
    void testShowAdminMenuOutput() {
        // [실행]
        LibraryMain.showAdminMenu();

        // [캡처된 출력값 가져오기]
        String output = outContent.toString();

        // [검증] 핵심 메뉴 키워드들이 포함되어 있는지 확인
        assertTrue(output.contains("[ 관리자 전용 메뉴 ]"));
        assertTrue(output.contains("1. 도서 등록 (Add)"));
        assertTrue(output.contains("0. 종료 (Exit)"));
    }

    @Test
    @DisplayName("사용자 전용 메뉴 출력 검증")
    void testShowUserMenuOutput() {
        // [실행]
        LibraryMain.showUserMenu();

        // [캡처된 출력값 가져오기]
        String output = outContent.toString();

        // [검증] 핵심 메뉴 키워드들이 포함되어 있는지 확인
        assertTrue(output.contains("[ 일반 사용자 메뉴 ]"));
        assertTrue(output.contains("3. 전체 도서 목록 (List)"));
        assertTrue(output.contains("4. 도서 검색 (Search)"));
        assertTrue(output.contains("0. 종료 (Exit)"));
    }

    @Test
    @DisplayName("도서 등록 시 입력값에 따른 출력 메시지와 Map 저장 상태 확인")
    void testAddBookSuccess() {
        // Given: 사용자 입력 시뮬레이션 (제목: 어린왕자, 저자: 생텍쥐페리)
        String input = "어린왕자\n생텍쥐페리\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // LibrarySystem 내부의 Scanner를 새 입력 스트림으로 갱신
        LibraryMain.sc = new Scanner(System.in);

        // When: 도서 등록 실행
        LibraryMain.addBook();

        // Then 1: 콘솔 출력 결과 확인
        String output = outContent.toString();
        assertTrue(output.contains("[결과] 등록이 완료되었습니다."),
                "성공 메시지에 도서 ID 1이 포함되어야 합니다.");

        // Then 2: Map 데이터 저장 확인
        assertFalse(LibraryMain.bookMap.isEmpty(), "bookMap에 데이터가 저장되어야 합니다.");

        ArrayList<Object> savedBook = LibraryMain.bookMap.get(1);
        assertNotNull(savedBook, "ID 1번에 해당하는 도서 정보가 있어야 합니다.");
        assertEquals("어린왕자", savedBook.get(0));
        assertEquals("생텍쥐페리", savedBook.get(1));
        assertEquals(true, savedBook.get(2)); // 대출 가능 여부 확인
    }

    // https://github.com/sumannam/Java/issues/18
    @Test
    @DisplayName("도서 목록이 CSV 파일로 성공적으로 저장되는지 확인")
    void testSaveBooksToCSV() throws IOException {
        // When: 저장 메서드 실행
        boolean isSaved = LibraryMain.saveBooksToCSV(TEST_FILE_PATH);

        // Then 1: 반환값이 true여야 함
        assertTrue(isSaved, "파일 저장 메서드는 true를 반환해야 합니다.");

        // Then 1: 반환값이 true여야 함
        File file = new File(TEST_FILE_PATH);
        assertTrue(file.exists(), "books.csv 파일이 생성되어야 합니다.");

        // Then 3: 파일 내용 검증
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line1 = br.readLine();
            String line2 = br.readLine();

            // Map은 순서가 보장되지 않을 수 있으므로 포함 여부로 체크하거나
            // 정렬된 상태라면 직접 비교합니다.
            assertNotNull(line1);
            assertTrue(line1.contains("1,어린왕자,생텍쥐페리,true") || line1.contains("2,자바의 정석,남궁성,false"));

            assertNotNull(line2);
            assertTrue(line2.contains("2,자바의 정석,남궁성,false") || line2.contains("1,어린왕자,생텍쥐페리,true"));
        }
    }

    @Test
    @DisplayName("도서 제목 수정 성공")
    void testEditTitle() {
        // Given: ID 1번에 도서 등록
        LibraryMain.bookMap.put(1, new ArrayList<>(Arrays.asList("오래된 제목", "저자", true)));
        // 입력 시나리오: ID(1) -> 메뉴(1.제목수정) -> 새 제목(어린왕자)
        provideInput("1\n1\n어린왕자\n");

        // When
        LibraryMain.editOrDeleteBook();

        // Then
        assertEquals("어린왕자", LibraryMain.bookMap.get(1).get(0));
        assertTrue(outContent.toString().contains("제목이 수정되었습니다"));
    }

    @Test
    @DisplayName("도서 저자 수정 성공")
    void testEditAuthor() {
        // Given: ID 1번에 도서 등록
        LibraryMain.bookMap.put(1, new ArrayList<>(Arrays.asList("제목", "오래된 저자", true)));
        // 입력 시나리오: ID(1) -> 메뉴(2.저자수정) -> 새 저자(생텍쥐페리)
        provideInput("1\n2\n생텍쥐페리\n");

        // When
        LibraryMain.editOrDeleteBook();

        // Then
        assertEquals("생텍쥐페리", LibraryMain.bookMap.get(1).get(1));
        assertTrue(outContent.toString().contains("저자명이 수정되었습니다"));
    }

    @Test
    @DisplayName("도서 삭제 성공")
    void testDeleteBook() {
        // Given: ID 5번에 도서 등록
        LibraryMain.bookMap.put(5, new ArrayList<>(Arrays.asList("삭제할 도서", "저자", true)));
        // 입력 시나리오: ID(5) -> 메뉴(3.도서삭제)
        provideInput("5\n3\n");

        // When
        LibraryMain.editOrDeleteBook();

        // Then
        assertFalse(LibraryMain.bookMap.containsKey(5), "ID 5번 도서는 삭제되어 없어야 합니다.");
        assertTrue(outContent.toString().contains("삭제되었습니다"));
    }

    @Test
    @DisplayName("존재하지 않는 ID 입력 시 오류 메시지 출력")
    void testInvalidId() {
        // Given: Map이 비어있거나 해당 ID가 없음
        LibraryMain.bookMap.clear();
        provideInput("99\n"); // 존재하지 않는 ID 99 입력

        // When
        LibraryMain.editOrDeleteBook();

        // Then
        assertTrue(outContent.toString().contains("[오류] 해당 ID의 도서가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("수정 중 0번(취소)을 선택하면 데이터 유지")
    void testCancelEdit() {
        // Given
        String originalTitle = "기존제목";
        LibraryMain.bookMap.put(1, new ArrayList<>(Arrays.asList(originalTitle, "저자", true)));
        // 입력 시나리오: ID(1) -> 메뉴(0.취소)
        provideInput("1\n0\n");

        // When
        LibraryMain.editOrDeleteBook();

        // Then
        assertEquals(originalTitle, LibraryMain.bookMap.get(1).get(0), "취소 시 제목이 변하지 않아야 합니다.");
        assertTrue(outContent.toString().contains("수정을 취소합니다"));
    }

    @Test
    @DisplayName("전체 도서 목록 출력 검증")
    void testListBooksOutput() {
        // Given: 테스트용 데이터 2건 준비
        LibraryMain.bookMap.clear();
        LibraryMain.bookMap.put(1, new ArrayList<>(Arrays.asList("어린왕자", "생텍쥐페리", true)));
        LibraryMain.bookMap.put(2, new ArrayList<>(Arrays.asList("자바 입문", "김자바", false)));

        // When: 목록 출력 실행
        LibraryMain.listBooks();

        // Then: 출력된 내용 캡처 및 검증
        String output = outContent.toString();

        // 헤더 및 구분선 검증
        assertTrue(output.contains("[도서 목록]"));
        assertTrue(output.contains("ID") && output.contains("제목") && output.contains("상태"));

        // 데이터 검증
        assertTrue(output.contains("1") && output.contains("어린왕자") && output.contains("대출 가능"));
        assertTrue(output.contains("2") && output.contains("자바 입문") && output.contains("대출 중"));
    }

    @Test
    @DisplayName("제목 키워드를 통한 도서 검색 검증")
    void testSearchBookSuccess() {
        // Given: 테스트 데이터 준비
        LibraryMain.bookMap.clear();
        LibraryMain.bookMap.put(1, new ArrayList<>(Arrays.asList("어린왕자", "생텍쥐페리", true)));
        LibraryMain.bookMap.put(2, new ArrayList<>(Arrays.asList("자바의 정석", "남궁성", true)));
        LibraryMain.bookMap.put(5, new ArrayList<>(Arrays.asList("Do it! 자바", "박응용", false)));

        // "자바" 키워드 입력 시뮬레이션
        provideInput("자바\n");

        // When: 검색 실행
        LibraryMain.searchBook();

        // Then: 출력 내용 확인
        String output = outContent.toString();

        assertTrue(output.contains("검색 결과 (2건)"), "검색 건수가 일치해야 합니다.");
        assertTrue(output.contains("자바의 정석") && output.contains("Do it! 자바"));
        assertFalse(output.contains("어린왕자"), "검색어와 관련 없는 도서는 출력되지 않아야 합니다.");
    }

    @Test
    @DisplayName("도서 대출 성공 테스트: ID 존재 및 대출 가능 상태")
    void borrowBook_Success() {
        // 가상 입력 설정: ID 2 입력 후, 확인 대답 'Y' 입력
        String input = "2\nY\n";
        provideInput(input);

        LibraryMain.borrowBook();

        // 결과 검증: ID 2번 도서의 상태값(index 2)이 false로 변경되었는지 확인
        assertFalse((boolean) LibraryMain.bookMap.get(2).get(2));
    }

    @Test
    @DisplayName("도서 대출 실패 테스트: 이미 대출 중인 경우")
    void borrowBook_AlreadyBorrowed() {
        // 이미 대출 중인 상태로 설정
        LibraryMain.bookMap.get(2).set(2, false);

        // 가상 입력 설정: ID 2 입력
        String input = "2\n";
        provideInput(input);

        LibraryMain.borrowBook();

        // 결과 검증: 상태값은 그대로 false여야 함
        assertFalse((boolean) LibraryMain.bookMap.get(2).get(2));
    }

    @Test
    @DisplayName("도서 대출 취소 테스트: 사용자가 'N'을 선택한 경우")
    void borrowBook_Cancel() {
        // 가상 입력 설정: ID 1 입력 후, 취소 대답 'N' 입력
        String input = "1\nN\n";
        provideInput(input);

        LibraryMain.borrowBook();

        // 결과 검증: 대출을 취소했으므로 상태값은 그대로 true여야 함
        assertTrue((boolean) LibraryMain.bookMap.get(1).get(2));
    }

    @Test
    @DisplayName("도서 반납 성공 테스트: 대출 중인 도서를 반납하고 승인(Y)한 경우")
    void returnBook_Success() {
        // 가상 입력: ID 2 입력 -> 승인 'Y' 입력
        provideInput("2\nY\n");

        LibraryMain.returnBook();

        // 결과 검증: ID 2번 도서의 상태값(index 2)이 true로 변경되었는지 확인
        assertTrue((boolean) LibraryMain.bookMap.get(2).get(2));
    }

    @Test
    @DisplayName("도서 반납 취소 테스트: 대출 중인 도서이지만 승인을 거절(N)한 경우")
    void returnBook_Cancel() {
        // 가상 입력: ID 2 입력 -> 거절 'N' 입력
        provideInput("2\nN\n");

        LibraryMain.returnBook();

        // 결과 검증: 반납을 취소했으므로 상태값은 여전히 false(대출 중)여야 함
        assertFalse((boolean) LibraryMain.bookMap.get(2).get(2));
    }

    @Test
    @DisplayName("반납 불가 테스트: 이미 대출 가능 상태인 도서를 반납하려는 경우")
    void returnBook_AlreadyReturned() {
        // 가상 입력: 이미 대출 가능 상태인 ID 1 입력
        provideInput("1\n");

        LibraryMain.returnBook();

        // 결과 검증: 상태값은 변화 없이 true여야 함
        assertTrue((boolean) LibraryMain.bookMap.get(1).get(2));
    }

    @Test
    @DisplayName("반납 실패 테스트: 존재하지 않는 도서 ID를 입력한 경우")
    void returnBook_NotFound() {
        // 가상 입력: 존재하지 않는 ID 99 입력
        provideInput("99\n");

        // 예외 없이 메시지만 출력되는지 확인 (상태 변화를 확인할 대상 없음)
        assertDoesNotThrow(LibraryMain::returnBook);
    }
}