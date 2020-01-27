package com.lisandor.volcano.bookingapi.validations;

import java.util.Optional;

@FunctionalInterface
public interface Validator<T> {

    Optional<String> valid(T instance);
}
