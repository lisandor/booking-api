#Booking API

Provides convenient endpoints to check availability checking and to manage reservations 

## Tests
To run all the tests, execute the following command
./mvnw test

## Running the application
Start the application using an in memory database

./mvnw spring-boot:run


### Relevant concurrency info

I decided to synchronize at Date level in order to not to block bookings for different dates
The first user to ask for a booking will lock the only selected dates till booking is created (or throws an error).

Bookings on different date ranges should not be executing sequentially.If I synchronize at request level, I'd be creating a bottleneck when as load grows up on my endpoint.

