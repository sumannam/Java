import java.sql.*;
import java.util.*;
import java.util.List;

public class LibraryRepository {
    // DB 연결 정보
    private final String URL = "jdbc:mariadb://192.168.100.20:3306/library";
    private final String USER = "cjulib";
    private final String PASSWORD = "security";

    /**
     * MariaDB 연결을 위한 전용 메소드
     */
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver"); //
        } catch (ClassNotFoundException e) {
            throw new SQLException("드라이버 로드 실패: " + e.getMessage());
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * [추가] 메모리의 모든 도서 정보를 MariaDB에 저장(동기화)합니다.
     * CSV의 '전체 저장' 기능을 DB의 Upsert 로직으로 변환한 것입니다.
     */
    public void saveBooks(Map<Integer, Book> bookMap) {
        // 중복된 ID가 있으면 업데이트, 없으면 삽입하는 MariaDB 쿼리
        String sql = "INSERT INTO books (book_id, title, author, is_available, member_id) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "title = VALUES(title), " +
                "author = VALUES(author), " +
                "is_available = VALUES(is_available), " +
                "member_id = VALUES(member_id)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 성능 최적화를 위한 배치(Batch) 처리
            for (Book book : bookMap.values()) {
                pstmt.setInt(1, book.getId());
                pstmt.setString(2, book.getTitle());
                pstmt.setString(3, book.getAuthor());
                pstmt.setBoolean(4, book.isAvailable());

                // 대출자가 "null" 문자열인 경우 DB 실제 NULL로 처리
                if (book.getBorrowerId() == null || "null".equals(book.getBorrowerId())) {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                } else {
                    pstmt.setString(5, book.getBorrowerId());
                }
                pstmt.addBatch(); // 대기열에 추가
            }

            pstmt.executeBatch(); // 한 번에 실행
            System.out.println("[시스템] 모든 도서 데이터가 MariaDB에 동기화되었습니다.");

        } catch (SQLException e) {
            System.err.println("[오류] DB 저장(saveBooks) 실패: " + e.getMessage());
        }
    }

    /**
     * DB로부터 모든 도서 데이터를 로드합니다.
     */
    public Map<Integer, Book> loadBooks() {
        Map<Integer, Book> bookMap = new HashMap<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean available = rs.getBoolean("is_available");
                String mid = rs.getString("member_id");

                bookMap.put(id, new Book(id, title, author, available, mid == null ? "null" : mid));
            }
        } catch (SQLException e) {
            System.err.println("[오류] 로드 실패: " + e.getMessage());
        }
        return bookMap;
    }

    /**
     * DB로부터 사용자 목록을 로드합니다.
     */
    public User loadUser(String id, String pw) {
        //String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
        String sql = "SELECT * FROM users WHERE user_id = '" + id + "' AND password = '" + pw + "'";
        //System.out.println(sql);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, pw);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 반환 타입이 User로 바뀌었으므로 이제 에러 없이 정상 작동합니다.
                    return new User(
                            rs.getString("user_id"),
                            rs.getString("password"),
                            rs.getString("type")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[오류] 로그인 조회 실패: " + e.getMessage());
        }
        return null; // 일치하는 사용자가 없을 때
    }
}