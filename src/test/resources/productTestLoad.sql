insert into product(product_id,name, description,image) values (1,'Product1', 'Product1Description', 'www.Product1Description.com');
insert into product(product_id,name, description,image) values (2,'Product2', 'Product2Description', 'www.Product2Description.com');
insert into product(product_id,name, description,image) values (3,'Product3', 'Product3Description', 'www.Product3Description.com');

insert into review(review_id,review_rating,name,review_body,author,publisher,product_id) values (1,1,'review1','review1','self1','publisher',1);
insert into review(review_id,review_rating,name,review_body,author,publisher,product_id) values (2,2,'review2','review2','self2','publisher',1);
insert into review(review_id,review_rating,name,review_body,author,publisher,product_id) values (3,0,'review3','review3','self3','publisher',2);
insert into review(review_id,review_rating,name,review_body,author,publisher,product_id) values (4,2,'review4','review4','self4','publisher',2);

insert into comment(comment_id,comment_body,upvote_count,downvote_count,review_id) values(1,'comment_body1',0,0,1);
insert into comment(comment_id,comment_body,upvote_count,downvote_count,review_id) values(2,'comment_body2',0,0,1);
insert into comment(comment_id,comment_body,upvote_count,downvote_count,review_id) values(3,'comment_body3',0,0,1);
insert into comment(comment_id,comment_body,upvote_count,downvote_count,review_id) values(4,'comment_body4',0,0,1);
insert into comment(comment_id,comment_body,upvote_count,downvote_count,review_id) values(5,'comment_body1',0,0,2);
insert into comment(comment_id,comment_body,upvote_count,downvote_count,review_id) values(6,'comment_body1',0,0,2);