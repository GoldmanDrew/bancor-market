# Bancor Market Backend
### Setup
* Clone the repository
* Build using gradle
* Create a `credentials.properties` file at `src/main/resources/`
```
user = MySQLUser
password = MySQLPassword
```

### Database Creation
Use init.sql to initalize the MySQL database

### Testing
Use insert.sql to insert dummy values into MySQL, then run using an IDE or `gradle run` from a command line

### TODO
* Use BigDecimal rather than Double
* Write unit tests
