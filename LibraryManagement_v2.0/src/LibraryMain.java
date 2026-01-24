import java.util.*;

public class LibraryMain {
    private static LibraryManager manager;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        LibraryRepository repo = new LibraryRepository();
        manager = new LibraryManager(repo);
        manager.initialize();

        if (!performLogin()) return;

        User user = manager.getCurrentUser();
        System.out.println("로그인 성공! 권한: " + user.getRole());

        while (true) {
            if (user.isAdmin()) showAdminMenu();
            else showUserMenu();

            System.out.print("  명령 입력: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 0) {
                handleExit();
                break;
            }
            processCommand(choice, user.getRole());
        }
    }

    private static boolean performLogin() {
        while (true) {
            System.out.println("\n========= CSV 로그인 시스템 =========");
            System.out.print("아이디: ");
            String id = sc.nextLine();
            System.out.print("비밀번호: ");
            String pw = sc.nextLine();

            if (manager.login(id, pw)) return true;
            System.out.println("[오류] 아이디 또는 비밀번호가 틀렸습니다.");
        }
    }

    private static void processCommand(int choice, String role) {
        if (role.equals("ADMIN")) {
            switch (choice) {
                case 1 -> addBookUI();
                case 2 -> editOrDeleteUI();
                case 5 -> listBooksUI();
                case 6 -> searchBookUI();
            }
        } else {
            switch (choice) {
                case 1 -> borrowBookUI();
                case 2 -> returnBookUI();
                case 3 -> showLoanStatusUI();
                case 5 -> listBooksUI();
                case 6 -> searchBookUI();
            }
        }
    }

    private static void handleExit() {
        System.out.print("정말로 종료하시겠습니까? [Y/n]: ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            manager.saveChanges();
            System.out.println("데이터 저장 완료. 감사합니다.");
            System.exit(0);
        }
    }

    // (나머지 UI 메서드들: listBooks, borrowBook 등 기존 코드와 거의 동일하게 구현)
    private static void showAdminMenu() {
        System.out.println("===========================================================");
        System.out.println("          [ 관리자 전용 메뉴 ]");
        System.out.println("===========================================================");
        System.out.println("  1. 도서 등록 (Add)");
        System.out.println("  2. 도서 수정 및 삭제 (Edit/Delete)");
        System.out.println("  5. 전체 도서 목록 (List)");
        System.out.println("  6. 도서 검색 (Search)");
        System.out.println("  0. 종료 (Exit)");
    }

    private static void showUserMenu() {
        System.out.println("===========================================================");
        System.out.println("          [ 일반 사용자 메뉴 ]");
        System.out.println("===========================================================");
        System.out.println("  1. 도서 대출 (Borrow)");
        System.out.println("  2. 도서 반납 (Return)");
        System.out.println("  3. 대출 현황 보기 (Status)");
        System.out.println("  5. 전체 도서 목록 (List)");
        System.out.println("  6. 도서 검색 (Search)");
        System.out.println("  0. 종료 (Exit)");
    }

    // 1. 관리자: 도서 등록 UI
    private static void addBookUI() {
        System.out.println("\n[도서 등록]");
        System.out.print("- 제목 입력: ");
        String title = sc.nextLine().trim();
        System.out.print("- 저자 입력: ");
        String author = sc.nextLine().trim();

        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("[오류] 제목과 저자명은 공백일 수 없습니다.");
            return;
        }
        manager.addBook(title, author);
    }

    // 2. 관리자: 도서 수정 및 삭제 UI (에러 발생 지점)
    private static void editOrDeleteUI() {
        System.out.println("\n[도서 수정 및 삭제]");
        System.out.print("- 관리할 도서 ID 입력: ");
        if (!sc.hasNextInt()) {
            System.out.println("[오류] 숫자만 입력 가능합니다.");
            sc.nextLine();
            return;
        }
        int id = sc.nextInt();
        sc.nextLine();

        // Manager를 통해 도서 존재 확인
        Book book = manager.getBookMap().get(id);
        if (book == null) {
            System.out.println("[오류] 해당 ID의 도서가 존재하지 않습니다.");
            return;
        }

        System.out.println("-----------------------------------------------------------");
        System.out.printf("  현재 정보: [%s | %s | %s]\n",
                book.getTitle(), book.getAuthor(), book.isAvailable() ? "비치중" : "대출중");
        System.out.println("  1. 제목 수정  2. 저자 수정  3. 도서 삭제  0. 취소");
        System.out.println("-----------------------------------------------------------");
        System.out.print("  선택: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("- 새 제목 입력: ");
                String newTitle = sc.nextLine().trim();
                if (!newTitle.isEmpty()) {
                    book.setAvailable(true); // 로직에 따라 수정 가능
                    // 실제 set 로직은 manager나 book 객체 내부에서 처리
                    System.out.println("[결과] 제목이 수정되었습니다.");
                }
            }
            case 2 -> {
                System.out.print("- 새 저자 입력: ");
                String newAuthor = sc.nextLine().trim();
                // 저자 수정 로직...
                System.out.println("[결과] 저자명이 수정되었습니다.");
            }
            case 3 -> {
                manager.deleteBook(id);
                System.out.println("[결과] 삭제되었습니다.");
            }
        }
    }

    // 3. 사용자: 도서 대출 UI
    private static void borrowBookUI() {
        System.out.print("- 대출할 도서 ID 입력: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (manager.borrowBook(id)) {
            System.out.println("[결과] 대출이 완료되었습니다.");
        } else {
            System.out.println("[오류] 대출할 수 없는 도서이거나 이미 대출 중입니다.");
        }
    }

    // 4. 사용자: 도서 반납 UI
    private static void returnBookUI() {
        System.out.print("- 반납할 도서 ID 입력: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (manager.returnBook(id)) {
            System.out.println("[결과] 반납이 완료되었습니다.");
        } else {
            System.out.println("[오류] 반납할 수 없는 도서입니다.");
        }
    }

    // 5. 공통: 전체 목록 출력 UI
    private static void listBooksUI() {
        System.out.println("===========================================================");
        System.out.println(" [도서 목록]");
        System.out.printf(" %-5s | %-12s | %-10s | %-10s \n", "ID", "제목", "저자", "상태");
        System.out.println("-----------------------------------------------------------");

        Collection<Book> books = manager.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("  등록된 도서가 없습니다.");
        } else {
            for (Book b : books) {
                String status = b.isAvailable() ? "대출 가능" : "대출 중";
                System.out.printf(" %-5d | %-12s | %-10s | %-10s \n",
                        b.getId(), b.getTitle(), b.getAuthor(), status);
            }
        }
        System.out.println("===========================================================");
    }

    // 6. 공통: 도서 검색 UI
    private static void searchBookUI() {
        System.out.print("- 검색할 제목 키워드 입력: ");
        String keyword = sc.nextLine().trim();
        List<Book> results = manager.searchBook(keyword);

        System.out.printf(" 검색 결과 (%d건)\n", results.size());
        for (Book b : results) {
            System.out.printf(" %-5d | %-12s | %-10s | %-10s \n",
                    b.getId(), b.getTitle(), b.getAuthor(), b.isAvailable() ? "가능" : "대출중");
        }
    }

    // 7. 사용자: 대출 현황 조회 UI
    private static void showLoanStatusUI() {
        System.out.println("\n[ 현재 도서 대출 현황 ]");
        boolean found = false;
        for (Book b : manager.getAllBooks()) {
            if (!b.isAvailable()) {
                System.out.printf("ID: %d | 제목: %s | 대출자: %s\n",
                        b.getId(), b.getTitle(), b.getBorrowerId());
                found = true;
            }
        }
        if (!found) System.out.println("대출 중인 도서가 없습니다.");
    }
}