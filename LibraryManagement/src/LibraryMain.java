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
        String currentRole = login();
        System.out.println("로그인 성공! 권한: " + currentRole);
    }

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