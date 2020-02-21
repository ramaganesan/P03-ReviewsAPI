# Reviews API 
Supports operations for writing reviews and listing reviews for a product but with no sorting or filtering.
The API also support Paging.
This API was built on top of the Reviews that was build using MySQL. This API can satisfy the following functionality.

* Support CR Operations on Product. The Product data will be store in MySQL Repository
* Suppport Read Operation on Reviews. The API will get data from MongoDB Repository
* Support CU of a Review. All new Reviews will be created  first created in MySql Repository and 
Mongo Repository.
* Support CRU of Comments to a Review. All Comments will be stored in MySql Repository  and as a embedded document with Review
in the Mongo Repository
* Support upVoting and downVoting a comment as well

### Prerequisites
* MySQL needs to be installed and configured. It also assumes that Review and Comment table is available in 
the MySQL repository. Can provide access to MySQL repository if needed.
* API assumes that MongoDB cluster is created and DB user properly set up. Can provide access to
MongoDB cluster if needed

### Getting Started
* Check out the code from Git
* Set up the datasource configuration, will be provided to the evaluator
* Build the project
* Flyway scripts available in src/main/resources/db/migration.
* Run the application

###Developer Notes
Schema Help: 
* https://developers.google.com/search/docs/data-types/product
* https://developers.google.com/search/docs/data-types/review-snippet

Flyway:
* https://flywaydb.org/documentation/maven/

Schema Creation:

Run schema creation using Flyway Maven Plugin
```
  CD to Project root e.g  cd '/c/workspace/ideaworkspace/JDND/JDND-course3_midterm_starter/P03-ReviewsAPI'
  mvn org.flywaydb:flyway-maven-plugin:6.1.3:migrate -Dflyway.user=WILL_BE_PROVIDED -Dflyway.password=WILL_BE_PROVIDED -Dflyway.schemas=WILL_BE_PROVIDED -Dflyway.url=jdbc:mysql://WILL_BE_PROVIDED:3306/WILL_BE_PROVIDED
  Above command will create the schema and tables for the project
```
Run the Project
```
Run of the ReviewsApplication will also use flyway to create the schema
```

Endpoints:
* http://localhost:8080/swagger-ui/index.html?url=/v3/api-docs&validatorUrl=

Sample Payload
```
Create new Product:
POST http://localhost:8080/products/:
{
	"name" : "UdacityShampoo",
	"description": "UdacityShampooDescription",
	"image": "www.UdacityShampoo.com"
}

{
        "name": "UdacityConditioner",
        "description": "UdacityConditionerDescription",
        "image": "www.UdacityConditioner.com"
    }

Create New Review:
POST http://localhost:8080/reviews/products/{productId}
{
	"name" : "UdacityShampooReview",
	"reviewBody":"Excellent Review",
	"author":"self",
	"publisher": "udacity",
	"reviewRating" : 2
}

{
	"name" : "UdacityConditionerReview",
	"reviewBody":"Good product",
	"author":"self",
	"publisher": "udacity",
	"reviewRating" : 1
}
Note: Review Rating 0(POOR), 1(GOOD), 2(EXCELLENT)

Create new Comment:
POST http://localhost:8080/comments/reviews/{reviewId}
{
    "commentBody": "GoodReview",
    "upvoteCount": 0,
    "downVoteCount": 0
}

{
	"commentBody" : "GoodReview",
	"upvoteCount" : 0,
	"downVoteCount" : 0
}
reviewId => Must be reviewId from MySQL or MongoDB
e.g http://localhost:8080/comments/reviews/10
10 here is Review Id from MongoDB or Mysql

UpVote a Comment:
PATCH http://localhost:8080/comments/{commentId}/upvote
{
  {
  	"upvoteCount": 4
  }
}
commentId => Must be Comment Id of Embedded Comment Document within Review or Comment Table
from MySQL
e.g http://localhost:8080/comments/10/upvote
10 here is the Comment Id of Comment with Review

DownVote a Comment:
PATCH http://localhost:8080/comments/{commentId}/downvote
{
  {
  	"downvoteCount": 4
  }
}
commentId => Must be Comment Id of Embedded Comment Document within Review or Comment Table
from MySQL
e.g http://localhost:8080/comments/10/upvote
10 here is the Comment Id of Comment with Review
```
             

