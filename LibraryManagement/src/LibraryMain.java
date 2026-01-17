import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

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

            if ( choice == 0 ) {
                handleExit();
                break;
            }
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
     *
     * @see LibraryMainTest#testAdminLoginSuccess()
     * @see LibraryMainTest#testUserLoginSuccess()
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
                if (role.equals("ADMIN"))
                    addBook(); // 관리자: 도서 등록 메소드 호출
                else {
                    // borrowBook(); // 일반 유저: 도서 대출 메소드 호출
                    System.out.println("[알림] 도서 대출 기능을 실행합니다.");
                }
                break;

            case 2:
                if (role.equals("ADMIN"))
                    editOrDeleteBook(); // 관리자: 수정/삭제 호출
                else {
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

    /**
     * 사용자로부터 도서 정보를 입력받아 시스템에 새로운 도서를 등록합니다.
     * * <p>이 메서드는 다음과 같은 절차로 진행됩니다:</p>
     * <ol>
     * <li>사용자로부터 도서의 제목과 저자명을 입력받습니다.</li>
     * <li>입력값이 비어있는지(공백 포함) 유효성 검사를 수행합니다.</li>
     * <li>{@code bookCount}를 증가시켜 고유한 도서 ID({@code book_id})를 생성합니다.</li>
     * <li>도서 정보를 {@code ArrayList}에 담아 {@link #bookMap}에 저장합니다.</li>
     * </ol>
     * * <p>저장되는 리스트 구조(ArrayList&lt;Object&gt;):</p>
     * <ul>
     * <li>Index 0: {@code String} 제목</li>
     * <li>Index 1: {@code String} 저자</li>
     * <li>Index 2: {@code Boolean} 대출 가능 여부 (기본값: {@code true})</li>
     * </ul>
     * * @see #bookCount
     * @see #bookMap
     * @see #sc
     * @see <a href="https://github.com/sumannam/Java/issues/11">Github Issue #11: 도서 등록 개발</a>
     *
     * @see LibraryMainTest#testAddBookSuccess()
     */
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

    public static void handleExit() {
        System.out.println("===========================================================");
        System.out.println("          [ 프로그램 종료 확인 ]");
        System.out.println("===========================================================");
        System.out.print("  정말로 프로그램을 종료하시겠습니까? [Y/n]: ");

        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.isEmpty()) {
            System.out.println("-----------------------------------------------------------");
            System.out.println("  [데이터 저장 중...]");

            // 파일 저장 메서드 호출
            if (saveBooksToCSV(booksFile)) {
                System.out.println("  => 모든 데이터가 성공적으로 저장되었습니다.");
            } else {
                System.out.println("  [!] 데이터 저장 중 오류가 발생했습니다.");
            }

            System.out.println("  도서 관리 프로그램을 이용해 주셔서 감사합니다.");
            System.out.println("===========================================================");
            System.exit(0); // 프로그램 완전 종료
        } else {
            System.out.println("[알림] 종료를 취소하고 메뉴로 돌아갑니다.");
        }
    }

    /**
     * 현재 메모리(bookMap)에 저장된 모든 도서 데이터를 CSV 파일로 저장합니다.
     * * <p>파일 저장 형식은 다음과 같습니다:</p>
     * <ul>
     * <li>ID (Integer)</li>
     * <li>제목 (String)</li>
     * <li>저자 (String)</li>
     * <li>대출 가능 여부 (Boolean)</li>
     * </ul>
     * * <p>입력 파라미터 {@code filepath}는 books.csv와 books_test.csv를 분리하기 위해 사용한다.</p>
     *
     * @return 저장에 성공하면 {@code true}, 입출력 오류(IOException) 발생 시 {@code false}를 반환합니다.
     * @see #bookMap
     * @see #booksFile
     * @see <a href="https://github.com/sumannam/Java/issues/17">Github Issue #17: 데이터 저장 기능 개발</a>
     *
     * @see LibraryMainTest#testSaveBooksToCSV()
     */
    public static boolean saveBooksToCSV(String filepath)
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            // 헤더 작성 (선택사항)
            // bw.write("ID,제목,저자,대출가능여부");
            // bw.newLine();

            for (Map.Entry<Integer, ArrayList<Object>> entry : bookMap.entrySet()) {
                Integer id = entry.getKey();
                ArrayList<Object> info = entry.getValue();

                // CSV 형식: ID,제목,저자,대출여부
                String line = String.format("%d,%s,%s,%b",
                        id,
                        info.get(0), // 제목
                        info.get(1), // 저자
                        info.get(2)  // 대출여부 (true/false)
                );

                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 특정 도서의 고유 ID를 입력받아 해당 도서의 정보를 수정하거나 시스템에서 삭제합니다.
     * * <p>이 메서드는 다음과 같은 흐름으로 동작합니다:</p>
     * <ol>
     * <li>사용자로부터 관리할 도서의 ID를 입력받습니다.</li>
     * <li>{@link #bookMap}에서 해당 ID의 존재 여부를 확인합니다.</li>
     * <li>도서가 존재할 경우 현재 상세 정보(제목, 저자, 대출 상태)를 표시합니다.</li>
     * <li>하위 메뉴를 통해 다음 작업을 수행합니다:
     * <ul>
     * <li><b>1. 제목 수정:</b> 해당 도서 리스트의 {@code index 0}을 갱신합니다.</li>
     * <li><b>2. 저자 수정:</b> 해당 도서 리스트의 {@code index 1}을 갱신합니다.</li>
     * <li><b>3. 도서 삭제:</b> {@code bookMap}에서 해당 키(ID)와 값을 완전히 제거합니다.</li>
     * <li><b>0. 취소:</b> 변경 사항 없이 이전 메뉴로 돌아갑니다.</li>
     * </ul>
     * </li>
     * </ol>
     *
     * <p>데이터 유효성 검사:</p>
     * <ul>
     * <li>존재하지 않는 ID 입력 시 오류 메시지를 출력하고 종료됩니다.</li>
     * <li>수정 시 공백(Empty String)을 입력하면 데이터가 변경되지 않습니다.</li>
     * </ul>
     *
     * @see #bookMap
     * @see #sc
     *
     * @see <a href="https://github.com/sumannam/Java/issues/19">Github Issue #19: 도서 수정 및 수정</a>
     *
     * @see LibraryMainTest#testEditTitle() 단위 테스트: 제목 수정 검증
     * @see LibraryMainTest#testEditAuthor() 단위 테스트: 저자 수정 검증
     * @see LibraryMainTest#testDeleteBook() 단위 테스트: 도서 삭제 검증
     */
    public static void editOrDeleteBook() {
        System.out.println("\n[도서 수정 및 삭제]");
        System.out.print("- 관리할 도서 ID 입력: ");

        // ID 입력 받기
        if (!sc.hasNextInt()) {
            System.out.println("[오류] 숫자만 입력 가능합니다.");
            sc.nextLine(); // 버퍼 비우기
            return;
        }
        int bookId = sc.nextInt();
        sc.nextLine(); // 엔터 버퍼 비우기

        // 1. 도서 존재 여부 확인
        if (!bookMap.containsKey(bookId)) {
            System.out.println("[오류] 해당 ID의 도서가 존재하지 않습니다.");
            return;
        }

        ArrayList<Object> bookInfo = bookMap.get(bookId);
        String title = (String) bookInfo.get(0);
        String author = (String) bookInfo.get(1);
        boolean isAvailable = (boolean) bookInfo.get(2);
        String status = isAvailable ? "비치중" : "대출중";

        // 2. 현재 정보 출력 및 메뉴 표시
        System.out.println("-----------------------------------------------------------");
        System.out.printf("  현재 정보: [%s | %s | %s]\n", title, author, status);
        System.out.println("  1. 제목 수정  2. 저자 수정  3. 도서 삭제  0. 취소");
        System.out.println("-----------------------------------------------------------");
        System.out.print("  선택: ");

        int choice = sc.nextInt();
        sc.nextLine(); // 버퍼 비우기

        switch (choice) {
            case 1: // 제목 수정
                System.out.print("- 새 제목 입력: ");
                String newTitle = sc.nextLine().trim();
                if (!newTitle.isEmpty()) {
                    bookInfo.set(0, newTitle);
                    System.out.println("[결과] 도서 제목이 수정되었습니다.");
                }
                break;

            case 2: // 저자 수정
                System.out.print("- 새 저자 입력: ");
                String newAuthor = sc.nextLine().trim();
                if (!newAuthor.isEmpty()) {
                    bookInfo.set(1, newAuthor);
                    System.out.println("[결과] 저자명이 수정되었습니다.");
                }
                break;

            case 3: // 도서 삭제
                bookMap.remove(bookId);
                System.out.println("[결과] 해당 도서 정보가 시스템에서 삭제되었습니다.");
                break;

            case 0:
                System.out.println("[알림] 수정을 취소합니다.");
                break;

            default:
                System.out.println("[오류] 잘못된 선택입니다.");
                break;
        }
    }

}