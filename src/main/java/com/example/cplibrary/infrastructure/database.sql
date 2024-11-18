-- Tạo cơ sở dữ liệu nếu chưa tồn tại
USE cplibrary;

-- Bảng Members
CREATE TABLE IF NOT EXISTS Members (
                                       member_id INT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL,
                                       email VARCHAR(100) UNIQUE NOT NULL,
                                       phone VARCHAR(15),
                                       password VARCHAR(255) NOT NULL,
                                       role ENUM('staff', 'member') NOT NULL,
                                       status ENUM('active', 'banned') NOT NULL DEFAULT 'active'
);

-- Bảng Books
CREATE TABLE IF NOT EXISTS Books (
                                     book_id INT AUTO_INCREMENT PRIMARY KEY,
                                     isbn VARCHAR(20) UNIQUE NOT NULL,
                                     title VARCHAR(255) NOT NULL,
                                     author VARCHAR(255) NOT NULL,
                                     subject VARCHAR(100),
                                     publisher VARCHAR(100),
                                     shelf_location VARCHAR(50),
                                     rating FLOAT DEFAULT 0
);

-- Bảng Loans
CREATE TABLE IF NOT EXISTS Loans (
                                     loan_id INT AUTO_INCREMENT PRIMARY KEY,
                                     book_id INT NOT NULL,
                                     member_id INT NOT NULL,
                                     borrow_date DATE NOT NULL,
                                     due_date DATE NOT NULL,
                                     return_date DATE,
                                     FOREIGN KEY (book_id) REFERENCES Books(book_id) ON DELETE CASCADE,
                                     FOREIGN KEY (member_id) REFERENCES Members(member_id) ON DELETE CASCADE
);

-- Bảng Reservations
CREATE TABLE IF NOT EXISTS Reservations (
                                            reservation_id INT AUTO_INCREMENT PRIMARY KEY,
                                            book_id INT NOT NULL,
                                            member_id INT NOT NULL,
                                            reservation_date DATE NOT NULL,
                                            status ENUM('pending', 'cancelled', 'fulfilled') NOT NULL DEFAULT 'pending',
                                            FOREIGN KEY (book_id) REFERENCES Books(book_id) ON DELETE CASCADE,
                                            FOREIGN KEY (member_id) REFERENCES Members(member_id) ON DELETE CASCADE
);

-- Bảng Reviews
CREATE TABLE IF NOT EXISTS Reviews (
                                       review_id INT AUTO_INCREMENT PRIMARY KEY,
                                       book_id INT NOT NULL,
                                       member_id INT NOT NULL,
                                       review TEXT,
                                       rating INT CHECK (rating BETWEEN 1 AND 5),
                                       review_date DATE NOT NULL,
                                       FOREIGN KEY (book_id) REFERENCES Books(book_id) ON DELETE CASCADE,
                                       FOREIGN KEY (member_id) REFERENCES Members(member_id) ON DELETE CASCADE
);

-- Bảng Notifications
CREATE TABLE IF NOT EXISTS Notifications (
                                             notification_id INT AUTO_INCREMENT PRIMARY KEY,
                                             member_id INT NOT NULL,
                                             message TEXT NOT NULL,
                                             date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             FOREIGN KEY (member_id) REFERENCES Members(member_id) ON DELETE CASCADE
);

-- Chức năng đăng nhập
DELIMITER //
CREATE PROCEDURE LoginUser(IN p_email VARCHAR(100), IN p_password VARCHAR(255), OUT p_role ENUM('staff', 'member'), OUT p_status ENUM('active', 'banned'))
BEGIN
    DECLARE temp_role ENUM('staff', 'member');
    DECLARE temp_status ENUM('active', 'banned');

    SELECT role, status INTO temp_role, temp_status
    FROM Members
    WHERE email = p_email AND password = MD5(p_password);

    SET p_role = temp_role;
    SET p_status = temp_status;
END //
DELIMITER ;
