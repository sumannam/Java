import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LibraryMain {
    static Map<Integer, ArrayList<Object>> bookMap = new HashMap<>(); // 도서 저장소
    static int bookCount = 0; // 고유 ID 생성을 위한 카운트 변수
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args)
    {
        // String currentRole = login();
        String currentRole = "ADMIN";
        System.out.println("로그인 성공! 권한: " + currentRole);
    }

    /**
     * 사용자로부터 아이디와 비밀번호를 입력받아 CSV 파일을 통해 로그인을 인증합니다.
     * <p>
     * {@code data/users.csv} 파일을 한 줄씩 읽어 입력된 정보(아이디, 비밀번호)와
     * 일치하는 계정이 있는지 확인합니다. CSV 파일은 "아이디,비밀번호,권한" 형식을 따라야 합니다.
     * </p>
     * <ul>
     * <li>로그인 성공 시: 해당 사용자의 권한("ADMIN" 또는 "USER")을 반환하고 루프를 종료합니다.</li>
     * <li>로그인 실패 시: 오류 메시지를 출력하고 올바른 정보를 입력할 때까지 반복합니다.</li>
     * <li>파일 입출력 오류 시: "FAIL" 문자열을 반환합니다.</li>
     * </ul>
     * * @return 로그인된 사용자의 권한 문자열 (예: "ADMIN", "USER"), 시스템 오류 시 "FAIL"
     * @see java.io.BufferedReader
     * @see java.io.FileReader
     * @see <a href="https://github.com/sumannam/Java/issues/8">Github Issue #8: 로그인 기능 개발</a>
     * @see <a href="https://github.com/sumannam/Java/issues/9">Github Issue #9: 로그인을 위한 users.csv 파일 구조</a>
     * @see <a href="https://github.com/sumannam/Java/issues/10">Github Issue #10: 로그인 단위 테스트</a>
     */
    public static String login()
    {
        String csvFile = "data/users.csv"; // 파일 경로 (환경에 따라 "src/data/users.csv" 등으로 수정 가능)

        while (true)
        {
            System.out.println("\n========= CSV 로그인 시스템 =========");
            System.out.print("아이디: ");
            String inputId = sc.nextLine();
            System.out.print("비밀번호: ");
            String inputPw = sc.nextLine();

            boolean isSuccess = false;
            String role = "";

            // 파일 읽기 시작
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;

                while ((line = br.readLine()) != null) {
                    // CSV의 한 줄을 쉼표로 분리 (데이터 예시: admin,1111,ADMIN)
                    String[] userInfo = line.split(",");

                    String fileId = userInfo[0].trim();   // 아이디
                    String filePw = userInfo[1].trim();   // 비밀번호
                    String fileRole = userInfo[2].trim(); // 권한

                    // 입력한 정보와 파일 정보가 일치하는지 확인
                    if (fileId.equals(inputId) && filePw.equals(inputPw)) {
                        isSuccess = true;
                        role = fileRole;
                        break; // 일치하는 정보를 찾았으므로 더 이상 읽지 않음
                    }
                }
            }
            catch (IOException e)
            {
                System.out.println("[오류] 사용자 데이터를 읽는 중 문제가 발생했습니다: " + e.getMessage());
                return "FAIL";
            }

            if (isSuccess)
            {
                System.out.println("=> [" + role + "] 권한으로 로그인되었습니다.");
                return role; // "ADMIN" 또는 "USER" 반환
            }
            else
            {
                System.out.println("[오류] 아이디 또는 비밀번호가 틀렸습니다. 다시 시도해 주세요.");
            }
        }
    }
}