# Reviews API 
Supports operations for writing reviews and listing reviews for a product but with no sorting or filtering.

### Prerequisites
MySQL needs to be installed and configured. Instructions provided separately.

### Getting Started
* Configure the MySQL Datasource in application.properties.
* Add Flyway scripts in src/main/resources/db/migration.
* Define JPA Entities and relationships.
* Define Spring Data JPA Repositories.
* Add tests for JPA Repositories.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

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
* http://localhost:8080/swagger-ui/index.html

Sample Payload
```
Create new Product
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
Create New Review
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
Create new Comment
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
```
             

