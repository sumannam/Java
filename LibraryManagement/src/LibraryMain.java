import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LibraryMain {
    static Map<Integer, ArrayList<Object>> bookMap = new HashMap<>(); // 도서 저장소
    static int bookCount = 0; // 고유 ID 생성을 위한 카운트 변수
    static Scanner sc = new Scanner(System.in);

    private static final String usersFile = "data/users.csv";
    private static final String booksFile = "data/books.csv";

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    public static void main(String[] args)
    {
        boolean isRunning = true;

        //String currentRole = login();
        String currentRole = "ADMIN";
        System.out.println("로그인 성공! 권한: " + currentRole);

        while (isRunning)
        {
            int choice = -9;
            if ( currentRole.equals(ADMIN) )
            {
                showAdminMenu();
                choice = restart(ADMIN);
            }
            else if ( currentRole.equals(USER) )
            {
                showUserMenu();
                choice = restart(USER);
            }

            if ( choice == 0 )
                break;
        }
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
            try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
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

    /**
     * 관리자 전용 메뉴 출력
     */
    public static void showAdminMenu() {
        System.out.println("===========================================================");
        System.out.println("          [ 관리자 전용 메뉴 ]");
        System.out.println("===========================================================");
        System.out.println("  1. 도서 등록 (Add)");
        System.out.println("  2. 도서 수정 및 삭제 (Edit/Delete)");
        showCommonMenu(); // 공통 메뉴 호출
    }

    /**
     * 일반 사용자 전용 메뉴 출력
     */
    public static void showUserMenu() {
        System.out.println("===========================================================");
        System.out.println("          [ 일반 사용자 메뉴 ]");
        System.out.println("===========================================================");
        System.out.println("  1. 도서 대출 (Borrow)");
        System.out.println("  2. 나의 대출 현황 (My Books)");
        showCommonMenu(); // 공통 메뉴 호출
    }

    /**
     * 모든 사용자가 공통으로 사용하는 메뉴 출력
     */
    private static void showCommonMenu() {
        System.out.println("  3. 전체 도서 목록 (List)");
        System.out.println("  4. 도서 검색 (Search)");
        System.out.println("  0. 종료 (Exit)");
        System.out.println("-----------------------------------------------------------");
    }

    public static int restart(String role) {
        System.out.print("  명령 입력: ");
        int choice = sc.nextInt();
        sc.nextLine(); // 숫자 입력 후 남은 엔터 버퍼 비우기

        switch (choice) {
            case 1:
                if (role.equals("ADMIN")) {
                    addBook(); // 관리자: 도서 등록 메소드 호출
                    System.out.println("[알림] 도서 등록 기능을 실행합니다.");
                } else {
                    // borrowBook(); // 일반 유저: 도서 대출 메소드 호출
                    System.out.println("[알림] 도서 대출 기능을 실행합니다.");
                }
                break;

            case 2:
                if (role.equals("ADMIN")) {
                    // editOrDeleteBook(); // 관리자: 수정/삭제 호출
                    System.out.println("[알림] 도서 수정/삭제 기능을 실행합니다.");
                } else {
                    // showMyStatus(); // 일반 유저: 대출 현황 호출
                    System.out.println("[알림] 나의 대출 현황을 확인합니다.");
                }
                break;

            case 3:
                // listBooks(); // 공통: 전체 목록 조회
                System.out.println("[알림] 전체 도서 목록을 출력합니다.");
                break;

            case 4:
                // searchBook(); // 공통: 도서 검색
                System.out.println("[알림] 도서 검색 기능을 실행합니다.");
                break;

            case 0:
                System.out.println("[시스템] 종료를 선택하셨습니다.");
                break;

            default:
                System.out.println("[오류] 잘못된 번호입니다. 다시 선택해 주세요.");
                break;
        }

        return choice;
    }

    public static void addBook() {
        System.out.println("\n[도서 등록]");

        System.out.print("- 제목 입력: ");
        String title = sc.nextLine().trim();

        System.out.print("- 저자 입력: ");
        String author = sc.nextLine().trim();

        // [조건] 제목과 저자명이 공백이 아닌가?
        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("[오류] 제목과 저자명은 공백일 수 없습니다. 다시 시도해주세요.");
            return; // 관리자 메뉴로 복귀
        }

        // [처리] count 변수 1 증가시켜 book_id 생성
        bookCount++;
        int book_id = bookCount;

        // [처리] ArrayList에 [제목, 저자, true] 저장
        ArrayList<Object> bookInfo = new ArrayList<>();
        bookInfo.add(title);   // index 0: 제목
        bookInfo.add(author);  // index 1: 저자
        bookInfo.add(true);    // index 2: 대출 가능 여부 (true = 가능)

        // [처리] Map.put(book_id, list)
        bookMap.put(book_id, bookInfo);

        // [결과] 메시지 출력
        System.out.println("-----------------------------------------------------------");
        System.out.printf("[결과] 등록이 완료되었습니다. (도서 ID: %d)\n", book_id);
    }

}