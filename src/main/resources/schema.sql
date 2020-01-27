DROP TABLE IF EXISTS booking_booking_dates;
DROP TABLE IF EXISTS booking_date;
DROP TABLE IF EXISTS Booking;
DROP SEQUENCE IF EXISTS booking_date_generator;

CREATE TABLE booking (
  id UUID  PRIMARY KEY,
  email VARCHAR(250) NOT NULL,
  full_name VARCHAR(250) NOT NULL,
  created_at DATETIME DEFAULT NULL,
  modified_at DATETIME DEFAULT NULL
);

CREATE TABLE booking_date (
  id INT AUTO_INCREMENT PRIMARY KEY,
  calendar_date DATE NOT NULL,
  status VARCHAR(250) NOT NULL
);

CREATE TABLE booking_booking_dates(
    booking_id UUID NOT NULL,
    booking_date_id INT NOT NULL
);

ALTER TABLE booking_booking_dates
ADD CONSTRAINT fk_booking_id FOREIGN KEY (booking_id) REFERENCES Booking(id);

ALTER TABLE booking_booking_dates
ADD CONSTRAINT fk_booking_date_id FOREIGN KEY (booking_date_id) REFERENCES Booking_date(id);

CREATE SEQUENCE booking_date_generator;