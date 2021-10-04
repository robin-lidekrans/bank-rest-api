# RESTful Bank API
A RESTful bank API implemented as part of the course Enterprise Systems (TDP024) at Linköping University. The bank-system is implemented in Java using the Spring framwork to implement a REST API that stores information about customers' accounts and transactions. In addition, two more REST APIs are implemented in Go and contain information about valid customers and banks. The bank system will communicate with these to ensure accounts are only created for valid customers and banks.

Testing is done using Junit. Database transactions and REST calls are logged using Apache Kafka.
