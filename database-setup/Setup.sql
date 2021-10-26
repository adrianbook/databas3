DROP DATABASE [AdrianBook];
GO

CREATE DATABASE [AdrianBook]
COLLATE Finnish_Swedish_100_CS_AI_SC_UTF8;
GO

-- Create tables

USE [AdrianBook];

CREATE TABLE DeweyDecimalSystem
(
    code INT NOT NULL PRIMARY KEY,
    class VARCHAR(60) NOT NULL
);
GO

CREATE TABLE Borrower
(
    borrower_id INT IDENTITY(1,1) PRIMARY KEY,
    full_name VARCHAR(60) NOT NULL,
    email VARCHAR(70) NOT NULL,
    street VARCHAR(40) NOT NULL,
    postal_code VARCHAR(5) NOT NULL,
    city VARCHAR(18) NOT NULL
);
GO

CREATE TABLE BookEdition
(
    isbn CHAR(13) PRIMARY KEY,
    title VARCHAR(80),
    author VARCHAR(60) NOT NULL,
    dds_code INT NOT NULL FOREIGN KEY REFERENCES DeweyDecimalSystem(code),
    times_lent INT NOT NULL DEFAULT 0
)

CREATE TABLE Book
(
    book_id INT IDENTITY(1,1) PRIMARY KEY,
    isbn CHAR(13) NOT NULL FOREIGN KEY REFERENCES BookEdition(isbn),
    purchase_date DATE NOT NULL
);
GO

-- Insert entry here to lend someone a book, delete from here to return

CREATE TABLE BorrowedBook
(
    book_id INT NOT NULL FOREIGN KEY REFERENCES Book(book_id),
    borrower_id INT NOT NULL FOREIGN KEY REFERENCES Borrower(borrower_id),
    return_date DATE NOT NULL,
    PRIMARY KEY (book_id)
);
GO


--SCHEMAS USERS
--Create schemas and roles

--Schema lib_admin
CREATE SCHEMA lib_admin;
GO
CREATE SCHEMA lib_app;
GO

CREATE ROLE lib_admin_reader;
CREATE ROLE lib_app_access;
GO

GRANT SELECT
ON SCHEMA :: lib_admin TO lib_admin_reader;
GRANT SELECT, EXEC
ON SCHEMA :: lib_app TO lib_app_access;
GO


--Create users and assign roles
CREATE LOGIN Librarian
WITH PASSWORD = 'b00ksRule',
CHECK_EXPIRATION = OFF;
GO

CREATE USER Librarian FOR LOGIN Librarian
WITH DEFAULT_SCHEMA = lib_admin;
GO

ALTER ROLE lib_admin_reader ADD MEMBER Librarian;
GO

CREATE LOGIN LibraryApp
WITH PASSWORD = 'Analphab3tic',
CHECK_EXPIRATION = OFF;
GO

CREATE USER LibraryApp FOR LOGIN LibraryApp
WITH DEFAULT_SCHEMA = lib_app;
GO

ALTER ROLE lib_app_access ADD MEMBER LibraryApp;
GO

--VIEWS
--View for overdue books
CREATE VIEW lib_admin.OverdueBook30PlusDays
AS
    SELECT bb.book_id AS [Book_ID], be.title AS [Title], be.author AS [Author], bo.full_name AS [Borrower], bo.email AS [Borrower_E-mail], bb.return_date AS [Return_date]
    FROM BorrowedBook AS bb
    JOIN Book AS b
        ON bb.book_id = b.book_id
    JOIN BookEdition AS be
        ON b.isbn = be.isbn
    JOIN Borrower AS bo
        ON bb.borrower_id = bo.borrower_id
    WHERE DATEADD(DAY, 30, bb.return_date) < GETDATE();
GO

--VIEWS
--View for books to purchase

CREATE VIEW lib_admin.toPurchase
AS
    SELECT TOP(5) be.isbn AS ISBN, be.title AS Title, be.author AS Author
    FROM dbo.BookEdition AS be
    WHERE be.isbn IN (
        SELECT b.isbn 
        FROM dbo.Book as b
    GROUP BY b.isbn
    HAVING COUNT(b.book_id) < 3 
    )
    ORDER BY be.times_lent DESC
GO

CREATE VIEW lib_app.listAvailableBooks
AS
    SELECT b.book_id AS [Book_ID], be.title AS [Title], be.author AS [Author], dds.class AS [Genre]
    FROM Book as b
        JOIN BookEdition AS be
            ON be.isbn = b.isbn
        JOIN DeweyDecimalSystem AS dds
            ON dds.code = be.dds_code
        WHERE b.book_id NOT IN(
            SELECT BorrowedBook.book_id
            FROM BorrowedBook
        );
GO

CREATE VIEW lib_app.listBorrowedBooksByBorrower
AS
    SELECT bo.borrower_id AS [Borrower_id], bo.full_name AS [Borrower_name], bo.email AS [Borrower_email], 
            b.book_id AS [Book_id], be.title AS [Title], be.author AS [Author] 
    FROM BorrowedBook AS bb
        JOIN Book AS b
            ON b.book_id = bb.book_id
        JOIN BookEdition AS be
            ON be.isbn = b.isbn
        JOIN Borrower AS [bo]
            ON bo.borrower_id = bb.borrower_id
GO

--STORED PROCEDURES
-- determine if book too old
CREATE PROCEDURE dbo.removeOldBooks
AS
BEGIN
DELETE FROM dbo.Book 
    WHERE DATEADD(YEAR, 9, purchase_date) < GETDATE()
    AND
    dbo.Book.book_id NOT IN (
        SELECT book_id
        FROM BorrowedBook
    )
DELETE FROM dbo.BookEdition
    WHERE dbo.BookEdition.isbn NOT IN (
        SELECT isbn
        FROM dbo.Book
    )
END
GO

-- lend book and increment the times_lent field in BookEdition
CREATE PROCEDURE lib_app.lendBook
    @book_id AS INT,
    @borrower_id AS INT
AS
BEGIN
    IF @@TRANCOUNT <> 0
        THROW 50000, 'nested transaction not allowed', 0;
    ELSE
    SET XACT_ABORT ON
    BEGIN TRAN;
        INSERT INTO BorrowedBook (book_id, borrower_id, return_date) VALUES (
            @book_id, @borrower_id, DATEADD(DAY, 30, GETDATE())
        );
        UPDATE BookEdition
        SET BookEdition.times_lent = BookEdition.times_lent + 1
        WHERE BookEdition.isbn IN (
            SELECT Book.isbn FROM Book
            WHERE Book.book_id = @book_id
        );
    COMMIT TRAN;
END
GO

CREATE PROCEDURE lib_app.returnBook
    @book_id AS INT
AS
BEGIN
    DELETE FROM dbo.BorrowedBook
        WHERE dbo.BorrowedBook.book_id = @book_id;
END
GO

-- Insert test data

INSERT INTO DeweyDecimalSystem
VALUES
    (0, 'Computer science, information and general works'),
    (5, 'Computer programming, programs and data'),
    (10, 'Bibliography'),
    (20, 'Book rarities'),
    (30, 'General cyclopedias'),
    (813, 'Romance'),
    (898, 'Scandinavian');

INSERT INTO Borrower
VALUES
    ('Nisse Hult', 'nisse@bossnniss.com', 'Nissegränd 3', '44444', 'Nisseby'),
    ('Lena Lamm', 'lena@lamm.com', 'Fårgränd 12A', '44678', 'Storstaden');

INSERT INTO BookEdition (isbn, title, author, dds_code)
VALUES
    ('9781509302000', 'T-SQL Fundamentals', 'Itzik Ben-Gan', 5),
    ('9789129697285', 'Här kommer Pippi Långstrump', 'Astrid Lindgren', 898),
    ('9789177423379', 'Den gamle och havet', 'Ernest Hemingway', 813);

INSERT INTO Book
VALUES
    ('9781509302000', '2018-01-23'),
    ('9781509302000', '2019-04-12'),
    ('9789129697285', '2014-05-02'),
    ('9789129697285', '2015-08-12'),
    ('9789129697285', '2019-04-22'),
    ('9789177423379', '2011-12-21'),
    ('9789177423379', '2011-12-21');

-- INSERT INTO BorrowedBook
-- VALUES
--     (2, 1, '2021-05-12'),
--     (4, 2, '2021-05-02'),
--     (1, 2, '2021-03-17'),
--     (3, 2, '2021-03-20');
GO

--show all books overdue by more than 30 days

-- SELECT * FROM dbo.OverdueBook30PlusDays;

--show list of popular books to be purchased

--SELECT * FROM dbo.ToPurchase;

--remove old books from database

-- EXEC lendBook @borrower_id = 2, @book_id = 6;

-- SELECT * FROM Book;
-- SELECT * FROM BookEdition;
-- GO

-- EXEC dbo.removeOldBooks;

-- SELECT * FROM Book;
-- SELECT * FROM BookEdition;
-- GO
--lend a book from the library



-- SELECT * FROM BookEdition
-- JOIN Book
-- ON BookEdition.isbn = Book.isbn
-- WHERE Book.book_id = 6;

