insert into book_requests (id, user_id, book_item_id,book_request_type_id) values (1, 1,2,1);
insert into book_requests_journals (id, book_request_id, create_date) values( 1, 1, CURRENT_TIMESTAMP());