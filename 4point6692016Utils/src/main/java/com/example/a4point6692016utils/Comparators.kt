package com.example.a4point6692016utils

object Comparators {
    enum class NumericComparators(val value: String) {
        EQUALS("-equals-"),
        NOT_EQUALS("-not equals-"),
        CONTAINS("-contains-"),
        NOT_CONTAINS("-does not contain-"),
        LESS_THAN("-less than-"),
        LESS_THAN_OR_EQUAL_TO("-less than or equal to-"),
        GREATER_THAN("-greater than-"),
        GREATER_THAN_OR_EQUAL_TO("-greater than or equal to-"),
        IS_BETWEEN("-is between-"),
        IS_NOT_BETWEEN("-is not between-");
    }

    enum class TemporalComparators(val value: String) {
        IS_ON("-is on-"),
        IS_NOT_ON("-is not on-"),
        IS_BEFORE("-is before-"),
        IS_ON_OR_BEFORE("-is on or before-"),
        IS_AFTER("-is after-"),
        IS_ON_OR_AFTER("-is on or after-"),
        IS_BETWEEN("-is between-"),
        IS_NOT_BETWEEN("-is not between-");
    }

    enum class PicklistComparators(val value: String) {
        IS("-is-"),
        IS_NOT("-is not-"),
        IS_ONE_OF("-is one of-"),
        IS_NOT_ONE_OF("-is not one of-");
    }

    enum class StringComparators(val value: String) {
        IS("-is-"),
        IS_NOT("-is not-"),
        CONTAINS("-contains-"),
        NOT_CONTAINS("-does not contain-");
    }
}