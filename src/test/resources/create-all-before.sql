DELETE from book_requests_journals;
DELETE from book_requests;
DELETE from book_request_types;
DELETE from users;
DELETE from roles;
DELETE from book_items;
DELETE from books;
DELETE from authors;

INSERT INTO roles VALUES(1, 'READER'), (2, 'LIBRARIAN'), (3 ,'ADMIN');

INSERT INTO USERS (id,login, checksum, first_name, last_name, role_id) VALUES (1,'admin', 'C4CA4238A0B923820DCC509A6F75849B', 'Kostya', 'Morozov', 3);
INSERT INTO USERS (id,login, checksum, first_name, last_name, role_id) VALUES (2,'librarian', 'C4CA4238A0B923820DCC509A6F75849B', 'Jane', 'Doe', 2);
INSERT INTO USERS (id,login, checksum, first_name, last_name, role_id) VALUES (3,'jane', 'C4CA4238A0B923820DCC509A6F75849B', 'Jane', 'Doe', 2);
INSERT INTO USERS (id,login, checksum, first_name, last_name, role_id) VALUES (4,'user', 'C4CA4238A0B923820DCC509A6F75849B', 'user', 'user', 1);

INSERT INTO authors  VALUES (1, 'Robert Martin'), (2, 'Joshua Bloch'), (3, 'William Shakespeare'), (4, 'Mark Twain'), (5, 'Тарас Шевченко'), (6, 'Іван Котляревський'), (7,'J. K. Rowling') ;

INSERT INTO books (id, name, author_id, publisher,publish_year,image_link,) VALUES
(1 ,'Kobzar', 5, 'Barvinok', 1999, 'https://img.yakaboo.ua/media/catalog/product/cache/1/image/398x565/234c7c011ba026e66d29567e1be1d1f7/1/8/18291_26236_13.jpg'),
(2 ,'Clean Code', 1, 'PROSTYLE', 2012, 'https://saltares.com/img/wp/clean-code-uncle-bob.jpg'),
(3 ,'JAVA Effective Programming', 2, 'American House', 2015, 'https://images-na.ssl-images-amazon.com/images/I/41JLgmt8MlL._SX402_BO1,204,203,200_.jpg'),
(4 ,'Romeo and Juliet', 3, 'Simon & Schuster', 2004, 'https://images-na.ssl-images-amazon.com/images/I/31ObBpDEOcL._BO1,204,203,200_.jpg'),
(5 ,'Енеїда', 6, 'Книги Львова', 2004, 'https://img.yakaboo.ua/media/catalog/product/cache/1/image/398x565/234c7c011ba026e66d29567e1be1d1f7/i/m/img240_1_60.jpg'),
(6 ,'Harry Potter and the Philosophers Stone', 1, 'London Lit', 2002, 'https://img.yakaboo.ua/media/catalog/product/cache/1/image/398x565/234c7c011ba026e66d29567e1be1d1f7/4/8/483720_11873905.jpg');

INSERT INTO book_items VALUES (1,1), (2,2), (3,3), (4,4), (5,5), (6,6), (7,3), (8,3), (9,5), (10,5), (11,5);

INSERT INTO book_request_types  VALUES(1,'ABONEMENT'), (2,'READING_AREA');
