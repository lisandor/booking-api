package com.lisandor.volcano.bookingapi.validations;

/**
 * Class for defining error messages used by validations
 */
public class ValidationErrorMessages {

    public static final String BOOKING_TIME_RANGE_ERROR = "Booking time range should be from 1 to 3 days";
    public static final String BOOKING_ANTICIPATION_ERROR = "Reservations can be made from one day prior to arrival up to one month in advance";
    public static final String INVALID_BOOKING_DATES = "The selected time range is invalid";
    public static final String BOOKING_DATE_MISSING = "You must provide valid start date and end date for the booking.";
}
