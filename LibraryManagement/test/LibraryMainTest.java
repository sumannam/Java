import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class LibraryMainTest {

    @Test
    @DisplayName("관리자 로그인 성공 (기존 CSV 사용)")
    void testAdminLoginSuccess() {
        // [입력 시뮬레이션] 파일에 적힌 'admin', '1111'과 일치해야 함
        String input = "admin\n1111\n";
        provideInput(input);

        // [실행]
        String result = LibraryMain.login();

        // [검증]
        assertEquals("ADMIN", result);
    }

    @Test
    @DisplayName("일반 사용자 로그인 성공 (기존 CSV 사용)")
    void testUserLoginSuccess() {
        // [입력 시뮬레이션] 파일에 적힌 'user', '2222'와 일치해야 함 (경우에 따라 user01 등 수정)
        String input = "user\n2222\n";
        provideInput(input);

        // [실행]
        String result = LibraryMain.login();

        // [검증]
        assertEquals("USER", result);
    }

    // System.in 시뮬레이션 및 Scanner 초기화 함수
    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // 메인 클래스의 static Scanner가 바뀐 입력을 인식하도록 갱신
        LibraryMain.sc = new Scanner(System.in);
    }
}