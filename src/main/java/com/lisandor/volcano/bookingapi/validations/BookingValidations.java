package com.lisandor.volcano.bookingapi.validations;


import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Class for grouping validations and evaluate at the same time. This allows you to define in a flexible way groups of
 * different, related and reusable validations.
 */
public class BookingValidations implements Validator<BookingDto> {

    private final @NotNull Collection<Validator<BookingDto>> validators;

    public BookingValidations(final @NotNull Validator... validators) {
        this.validators = Arrays.asList(validators);
    }


    @Override
    public Optional<String> valid(BookingDto instance) {
        return validators.stream()
                .map(v -> v.valid(instance))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
