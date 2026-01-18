import java.io.*;
import java.util.*;

public class LibraryMain {
    static Map<Integer, ArrayList<Object>> bookMap = new HashMap<>(); // 도서 저장소
    static int bookCount = 0; // 고유 ID 생성을 위한 카운트 변수
    static Scanner sc = new Scanner(System.in);

    static String loginId = "";
    static String currentRole = "";

    private static final String usersFile = "data/users.csv";
    private static final String booksFile = "data/books.csv";

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    public static void main(String[] args)
    {
        boolean isRunning = true;

        //String currentRole = login();
        loginId = "user";
        System.out.println("로그인 성공! 권한: " + currentRole);

        loadBooksFromCSV();

        while (isRunning)
        {
            int choice = -9;
            if ( currentRole.equals(ADMIN) & loginId.equals("admin") )
            {
                showAdminMenu();
                choice = restart(ADMIN);
            }
            else
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
     * 지정된 경로의 CSV 파일로부터 도서 데이터를 읽어 메모리({@code bookMap})에 로드합니다.
     * <p>파일을 한 줄씩 읽어 데이터를 분리한 후, 적절한 타입으로 변환하여 저장합니다.</p>
     * <p>로드 완료 후, 다음 도서 등록 시 ID가 중복되지 않도록 {@code bookCount}를 가장 큰 ID 값으로 업데이트합니다.</p>
     *
     * @see #bookMap
     * @see #bookCount
     *
     * @see <a href="https://github.com/sumannam/Java/issues/22">Github Issue #22: 이전에 저장한 데이터가 종료 시 삭제되는 문제 발생</a>
     * @see <a href="https://github.com/sumannam/Java/issues/23">Github Issue #23: 프로그램 실행 시 books.csv 파일 bookMap에 저장</a>
     *
     * @see LibraryMainTest#testMapIsNotEmptyAfterLoad()
     */
    public static void loadBooksFromCSV()
    {
        File file = new File(booksFile);

        // 파일이 존재하지 않으면 로드할 데이터가 없는 것이므로 종료
        if (!file.exists()) {
            System.out.println("[시스템] 기존 데이터 파일이 존재하지 않아 빈 상태로 시작합니다.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int maxId = 0; // bookCount 동기화를 위한 변수

            while ((line = br.readLine()) != null) {
                // 빈 줄 건너뛰기
                if (line.trim().isEmpty()) continue;

                // CSV 데이터 분리 (ID, 제목, 저자, 대출여부, 대출한 사용자)
                String[] data = line.split(",");
                if (data.length < 5)
                    continue; // 데이터 형식이 맞지 않으면 건너뜀

                int id = Integer.parseInt(data[0].trim());
                String title = data[1].trim();
                String author = data[2].trim();
                boolean isAvailable = Boolean.parseBoolean(data[3].trim());
                String memberId = data[4].trim();

                // 메모리에 복구
                ArrayList<Object> bookInfo = new ArrayList<>();
                bookInfo.add(title);
                bookInfo.add(author);
                bookInfo.add(isAvailable);
                bookInfo.add(memberId);

                bookMap.put(id, bookInfo);

                // 가장 큰 ID 값을 추적하여 카운트 업데이트 준비
                if (id > maxId) {
                    maxId = id;
                }
            }

            // 다음 도서 등록을 위해 ID 카운트를 현재 최대 ID로 맞춤
            bookCount = maxId;
            System.out.println("[시스템] 데이터를 성공적으로 불러왔습니다. (총 " + bookMap.size() + "권)");

        } catch (IOException | NumberFormatException e) {
            System.out.println("[오류] 데이터 로딩 중 문제가 발생했습니다: " + e.getMessage());
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
            String userId = "";

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
                        userId = fileId;
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
                currentRole = role;
                return userId;
            }
            else
            {
                System.out.println("[오류] 아이디 또는 비밀번호가 틀렸습니다. 다시 시도해 주세요.");
            }
        }
    }

    /**
     * 관리자 전용 메뉴를 콘솔에 출력합니다.
     * <p>도서의 등록, 수정, 삭제와 같은 관리 권한이 필요한 메뉴를 제공하며,
     * 마지막에 모든 권한 공통 메뉴를 포함하여 출력합니다.</p>
     * @see #showCommonMenu() 공통 메뉴 출력 메소드
     *
     * @see <a href="https://github.com/sumannam/Java/issues/13">Github Issue #13: 관리자 메뉴 출력</a>
     */
    public static void showAdminMenu()
    {
        System.out.println("===========================================================");
        System.out.println("          [ 관리자 전용 메뉴 ]");
        System.out.println("===========================================================");
        System.out.println("  1. 도서 등록 (Add)");
        System.out.println("  2. 도서 수정 및 삭제 (Edit/Delete)");
        showCommonMenu(); // 공통 메뉴 호출
    }

    /**
     * 일반 사용자 전용 메뉴를 콘솔에 출력합니다.
     * <p>도서의 대출 및 반납과 같은 일반적인 이용 메뉴를 제공하며,
     * 마지막에 모든 권한 공통 메뉴를 포함하여 출력합니다.</p>
     * @see #showCommonMenu() 공통 메뉴 출력 메소드
     *
     * @see <a href="https://github.com/sumannam/Java/issues/14">Github Issue #14: 사용자 메뉴 출력</a>
     */
    public static void showUserMenu()
    {
        System.out.println("===========================================================");
        System.out.println("          [ 일반 사용자 메뉴 ]");
        System.out.println("===========================================================");
        System.out.println("  1. 도서 대출 (Borrow)");
        System.out.println("  2. 도서 반납 (Return)");
        System.out.println("  3. 대출 현황 보기 (Status)");
        showCommonMenu(); // 공통 메뉴 호출
    }

    /**
     * 모든 사용자가 권한에 관계없이 공통으로 사용하는 메뉴 항목을 출력합니다.
     * <p>이 메소드는 {@link #showAdminMenu()} 및 {@link #showUserMenu()} 내부에서
     * 호출되어 메뉴 하단의 공통 기능(조회, 검색, 종료)을 일관성 있게 표시합니다.</p>
     */
    private static void showCommonMenu()
    {
        System.out.println("  5. 전체 도서 목록 (List)");
        System.out.println("  6. 도서 검색 (Search)");
        System.out.println("  0. 종료 (Exit)");
        System.out.println("-----------------------------------------------------------");
    }

    public static int restart(String role)
    {
        System.out.print("  명령 입력: ");
        int choice = sc.nextInt();
        sc.nextLine(); // 숫자 입력 후 남은 엔터 버퍼 비우기

        switch (choice) {
            case 1:
                if (role.equals("ADMIN"))
                    addBook(); // 관리자: 도서 등록 메소드 호출
                else
                    borrowBook(); // 일반 유저: 도서 대출 메소드 호출
                break;

            case 2:
                if (role.equals("ADMIN"))
                    editOrDeleteBook(); // 관리자: 수정/삭제 호출
                else {
                    returnBook(); // 사용자: 반납 기능 개발
                }
                break;

            case 3:
                showLoanStatus(); // 사용자: 대출 현황 보기
                break;

            case 5:
                listBooks(); // 공통: 전체 목록 조회
                break;

            case 6:
                searchBook(); // 공통: 도서 검색
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
                String line = String.format("%d,%s,%s,%b,%s",
                        id,
                        info.get(0), // 제목
                        info.get(1), // 저자
                        info.get(2),  // 대출여부 (true/false)
                        info.get(3)  // 대출한 사용자
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

    /**
     * 시스템에 등록된 모든 도서 정보를 콘솔에 표(Table) 형식으로 출력합니다.
     * * <p>이 메서드는 {@link #bookMap}의 모든 데이터를 순회하며 다음 정보를 출력합니다:</p>
     * <ul>
     * <li><b>ID:</b> 도서의 고유 번호 (Map의 Key)</li>
     * <li><b>제목:</b> 도서 명 (ArrayList의 Index 0)</li>
     * <li><b>저자:</b> 저자 명 (ArrayList의 Index 1)</li>
     * <li><b>상태:</b> 대출 가능 여부 (ArrayList의 Index 2의 {@code boolean} 값을 문자열로 변환)</li>
     * </ul>
     * * <p>출력 형식은 {@code printf}를 사용하여 열 정렬을 수행하며,
     * 등록된 도서가 없을 경우 "등록된 도서가 없습니다."라는 안내 메시지를 표시합니다.</p>
     * @see #bookMap
     *
     * @see <a href="https://github.com/sumannam/Java/issues/24">Github Issue #24: 전체 도서 목록 출력</a>
     *
     * @see LibraryMainTest#testListBooksOutput() 단위 테스트: 표 출력 형식 및 데이터 일치 검증
     */
    public static void listBooks() {
        System.out.println("===========================================================");
        System.out.println(" [도서 목록]");
        System.out.printf(" %-5s | %-12s | %-10s | %-10s \n", "ID", "제목", "저자", "상태");
        System.out.println("-----------------------------------------------------------");

        if (bookMap.isEmpty()) {
            System.out.println("  등록된 도서가 없습니다.");
        } else {
            for (Map.Entry<Integer, ArrayList<Object>> entry : bookMap.entrySet()) {
                Integer id = entry.getKey();
                ArrayList<Object> info = entry.getValue();

                String title = (String) info.get(0);
                String author = (String) info.get(1);
                boolean isAvailable = (boolean) info.get(2);
                String status = isAvailable ? "대출 가능" : "대출 중";

                // %-Ns: 왼쪽 정렬로 N칸 확보
                System.out.printf(" %-5d | %-12s | %-10s | %-10s \n", id, title, author, status);
            }
        }
        System.out.println("===========================================================");
    }

    /**
     * 사용자로부터 입력받은 키워드를 도서 제목에서 검색하여 결과를 출력합니다.
     * * <p>이 메서드의 동작 방식은 다음과 같습니다:</p>
     * <ol>
     * <li>사용자에게 검색할 제목의 키워드를 입력받습니다.</li>
     * <li>입력된 키워드가 빈 문자열인 경우 안내 메시지를 출력하고 종료합니다.</li>
     * <li>{@link #bookMap}의 모든 데이터를 순회하며 {@link String#contains(CharSequence)}를 사용하여 제목에 키워드가 포함되었는지 확인합니다.</li>
     * <li>매칭되는 도서의 ID를 별도의 리스트({@code foundIds})에 수집합니다.</li>
     * <li>검색된 총 건수와 도서 상세 정보를 표 형식으로 출력합니다.</li>
     * </ol>
     * * <p>출력되는 도서 상태는 {@code boolean} 값에 따라 다음과 같이 변환됩니다:</p>
     * <ul>
     * <li>{@code true}: 대출 가능</li>
     * <li>{@code false}: 대출 중</li>
     * </ul>
     * * @see #bookMap
     * @see #listBooks() 출력 형식의 일관성을 위해 동일한 포맷팅 사용
     *
     * @see <a href="https://github.com/sumannam/Java/issues/26">Github Issue #26: 도서 검색 기능 개발</a>
     *
     * @see LibraryMainTest#testSearchBookSuccess() 단위 테스트: 키워드 검색 및 필터링 결과 검증
     */
    public static void searchBook() {
        System.out.println("\n[도서 검색]");
        System.out.print("- 검색할 제목 키워드 입력: ");
        String keyword = sc.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("[알림] 검색어를 입력해주세요.");
            return;
        }

        // 결과를 임시로 담을 리스트 (건수 확인을 위해 필요)
        ArrayList<Integer> foundIds = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Object>> entry : bookMap.entrySet()) {
            String title = (String) entry.getValue().get(0);
            if (title.contains(keyword)) {
                foundIds.add(entry.getKey());
            }
        }

        System.out.println("-----------------------------------------------------------");
        System.out.printf(" 검색 결과 (%d건)\n", foundIds.size());
        System.out.printf(" %-5s | %-12s | %-10s | %-10s \n", "ID", "제목", "저자", "상태");
        System.out.println("-----------------------------------------------------------");

        if (foundIds.isEmpty()) {
            System.out.println("  검색 결과가 없습니다.");
        } else {
            for (int id : foundIds) {
                ArrayList<Object> info = bookMap.get(id);
                String title = (String) info.get(0);
                String author = (String) info.get(1);
                String status = (boolean) info.get(2) ? "대출 가능" : "대출 중";

                System.out.printf(" %-5d | %-12s | %-10s | %-10s \n", id, title, author, status);
            }
        }
        System.out.println("-----------------------------------------------------------");
    }

    /**
     * 사용자로부터 도서 ID를 입력받아 도서 대출 처리를 수행하는 메소드입니다.
     * * <p>이 메소드는 다음과 같은 비즈니스 로직을 따릅니다:</p>
     * <ul>
     * <li><b>조건 1:</b> 입력된 ID가 {@code bookMap}에 존재하는지 확인합니다.</li>
     * <li><b>조건 2:</b> 해당 도서 리스트의 2번 인덱스(상태값)가 {@code true}(대출 가능)인지 확인합니다.</li>
     * <li><b>확인 절차:</b> 대출 가능 시 사용자의 최종 승인(Y/N)을 입력받습니다.</li>
     * <li><b>데이터 업데이트:</b> 대출 성공 시 해당 도서의 상태값을 {@code false}(대출 불가)로 변경합니다.</li>
     * </ul>
     * * @throws java.util.NoSuchElementException 입력 과정에서 요소가 없을 경우 발생할 수 있습니다.
     * @see #bookMap
     *
     * @see <a href="https://github.com/sumannam/Java/issues/28">Github Issue #28: 도서 대출 기능 개발</a>
     *
     * @see LibraryMainTest#borrowBook_Success() 도서 대출 성공 테스트: ID 존재 및 대출 가능 상태
     * @see LibraryMainTest#borrowBook_AlreadyBorrowed() 도서 대출 실패 테스트: 이미 대출 중인 경우
     * @see LibraryMainTest#borrowBook_Cancel() 도서 대출 취소 테스트: 사용자가 'N'을 선택한 경우
     * @see LibraryMainTest#borrowBook_InvalidId() 도서 대출 취소 테스트: 도서 대출 실패 테스트: 존재하지 않는 ID를 입력한 경우
     */
    public static void borrowBook()
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("[도서 대출]");
        System.out.print("- 대출할 도서 ID 입력: ");

        // 1. 도서 ID 입력 받기
        if (!sc.hasNextInt()) {
            System.out.println("[오류] 숫자 형식의 ID를 입력해주세요.");
            sc.nextLine(); // 버퍼 비우기
            return;
        }
        int inputId = sc.nextInt();
        sc.nextLine(); // 버퍼 비우기

        // 2. 조건 확인 및 처리
        if (bookMap.containsKey(inputId)) {
            List<Object> targetBook = bookMap.get(inputId);
            boolean isAvailable = (boolean) targetBook.get(2);

            if (isAvailable) {
                // 즉시 대출 처리 (확인 질문 생략)
                targetBook.set(2, false);    // 상태: 대출 중
                targetBook.set(3, loginId);  // 대출자 ID 자동 저장

                System.out.println("-----------------------------------------------------------");
                System.out.println("[결과] '" + targetBook.get(0) + "' 도서 대출이 완료되었습니다.");
                System.out.println("(대출자: " + loginId + ")");
            } else {
                System.out.println("-----------------------------------------------------------");
                System.out.println("[결과] 이미 대출 중인 도서입니다. (대출자: " + targetBook.get(3) + ")");
            }
        } else {
            System.out.println("[오류] 해당 ID의 도서가 존재하지 않습니다.");
        }
    }

    /**
     * 사용자로부터 도서 ID를 입력받아 도서 반납 처리를 수행하는 메소드입니다.
     * * <p>이 메소드는 다음과 같은 유효성 검사 및 비즈니스 로직을 따릅니다:</p>
     * <ul>
     * <li><b>조건 1 (ID 확인):</b> 입력된 ID가 {@code bookMap}에 등록된 도서인지 확인합니다.</li>
     * <li><b>조건 2 (상태 확인):</b> 해당 도서의 상태값(index 2)이 {@code false}(대출 중)인지 확인합니다.</li>
     * <li><b>확인 절차:</b> 반납 가능한 상태일 경우 사용자의 최종 승인(Y/N)을 입력받습니다.</li>
     * <li><b>데이터 처리:</b> 반납이 승인되면 해당 도서의 상태값을 {@code true}(대출 가능)로 변경합니다.</li>
     * </ul>
     * * <p><b>출력 결과:</b></p>
     * <ul>
     * <li>반납 성공 시: "[결과] 반납이 완료되었습니다."</li>
     * <li>이미 반납된 상태일 시: "[결과] 이미 반납된 도서이거나 대출 중이 아닙니다."</li>
     * <li>ID 미존재 시: "[오류] 해당 ID의 도서가 존재하지 않습니다."</li>
     * </ul>
     * @see #bookMap
     *
     * @see <a href="https://github.com/sumannam/Java/issues/31">Github Issue #31: 도서 반납 기능 개발</a>
     *
     * @see LibraryMainTest#returnBook_Success() 도서 반납 성공 테스트: 대출 중인 도서를 반납하고 승인(Y)한 경우
     * @see LibraryMainTest#returnBook_Cancel() 도서 반납 취소 테스트: 대출 중인 도서이지만 승인을 거절(N)한 경우
     * @see LibraryMainTest#returnBook_AlreadyReturned() 반납 불가 테스트: 이미 대출 가능 상태인 도서를 반납하려는 경우
     * @see LibraryMainTest#returnBook_NotFound() 반납 실패 테스트: 존재하지 않는 도서 ID를 입력한 경우
     */
    public static void returnBook()
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("[도서 반납]");
        System.out.print("- 반납할 도서 ID 입력: ");
        int inputId = sc.nextInt();
        sc.nextLine();

        if (bookMap.containsKey(inputId)) {
            List<Object> targetBook = bookMap.get(inputId);
            String bookTitle = (String) targetBook.get(0);
            boolean isAvailable = (boolean) targetBook.get(2);
            String borrowerId = (String) targetBook.get(3);

            if (!isAvailable) { // 대출 중인 상태 (false)
                System.out.println("-----------------------------------------------------------");
                System.out.print("[확인] '" + borrowerId + "'님이 대출 중인 '" + bookTitle + "' 도서를 반납하시겠습니까? (Y/N): ");

                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    targetBook.set(2, true);  // 상태: 대출 가능
                    targetBook.set(3, null);  // 대출자 ID를 null로 초기화
                    System.out.println("[결과] 반납이 완료되었습니다.");
                }
            } else {
                System.out.println("[결과] 대출 중인 도서가 아닙니다.");
            }
        } else {
            System.out.println("[오류] 해당 ID의 도서가 존재하지 않습니다.");
        }
    }

    /**
     * 메모리 내 {@code bookMap}에 저장된 모든 도서 데이터를 CSV 파일로 저장합니다.
     * * <p>이 메소드는 {@link java.util.ArrayList}의 각 인덱스 데이터를 다음과 같은 CSV 형식으로 변환하여 기록합니다:</p>
     * <ul>
     * <li><b>형식:</b> ID,도서명,저자,대출가능여부,대출자ID</li>
     * <li><b>데이터 맵핑:</b>
     * <ul>
     * <li>{@code index 0}: 도서명 (String)</li>
     * <li>{@code index 1}: 저자 (String)</li>
     * <li>{@code index 2}: 대출 가능 여부 (Boolean)</li>
     * <li>{@code index 3}: 대출자 ID (String, 대출 중이 아닐 경우 "null")</li>
     * </ul>
     * </li>
     * </ul>
     * * <p><b>파일 처리:</b> {@link java.io.BufferedWriter}와 {@link java.io.FileWriter}를 사용하여
     * 파일 쓰기 성능을 최적화하며, 저장 완료 후 자동으로 자원을 해제합니다.</p>
     * * @param filePath 저장할 CSV 파일의 전체 경로 또는 파일명
     * @see #bookMap
     *
     * @see <a href="https://github.com/sumannam/Java/issues/30">Github Issue #31: 사용자별 대출 현황 기능 개발</a>
     *
     * @see LibraryMainTest#verifyLoanDataInArrayList() 대출 현황 데이터 검증: ArrayList 내의 상태값과 대출자 정보 체크
     */
    public static void showLoanStatus() {
        System.out.println("===========================================================");
        System.out.println("                [ 현재 도서 대출 현황 ]");
        System.out.println("===========================================================");
        System.out.printf("%-5s | %-20s | %-10s%n", "ID", "도서명", "대출자");
        System.out.println("-----------------------------------------------------------");

        boolean hasBorrowedBooks = false;

        // Map의 키(ID)를 정렬하여 출력하기 위해 TreeMap 등을 사용할 수 있으나, 여기서는 일반 순회 수행
        for (Integer id : bookMap.keySet()) {
            List<Object> targetBook = bookMap.get(id);
            boolean isAvailable = (boolean) targetBook.get(2);

            // 조건: 상태값이 false(대출 중)인 경우만 출력
            if (!isAvailable) {
                String bookTitle = (String) targetBook.get(0);
                String borrowerId = (String) targetBook.get(3);

                System.out.printf("%-5d | %-20s | %-10s%n", id, bookTitle, borrowerId);
                hasBorrowedBooks = true;
            }
        }

        if (!hasBorrowedBooks) {
            System.out.println("   현재 대출 중인 도서가 없습니다.");
        }
        System.out.println("===========================================================");
    }

}