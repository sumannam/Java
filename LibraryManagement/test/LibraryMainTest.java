import org.junit.jupiter.api.*;
import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class LibraryMainTest {

    // 콘솔 출력을 캡처하기 위한 스트림 변수
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

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
    @DisplayName("도서 등록 시 입력값에 따른 출력 메시지와 Map 저장 상태를 확인한다")
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
        assertTrue(output.contains("[결과] 등록이 완료되었습니다. (도서 ID: 1)"),
                "성공 메시지에 도서 ID 1이 포함되어야 합니다.");

        // Then 2: Map 데이터 저장 확인
        assertFalse(LibraryMain.bookMap.isEmpty(), "bookMap에 데이터가 저장되어야 합니다.");

        ArrayList<Object> savedBook = LibraryMain.bookMap.get(1);
        assertNotNull(savedBook, "ID 1번에 해당하는 도서 정보가 있어야 합니다.");
        assertEquals("어린왕자", savedBook.get(0));
        assertEquals("생텍쥐페리", savedBook.get(1));
        assertEquals(true, savedBook.get(2)); // 대출 가능 여부 확인
    }
}