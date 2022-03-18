# jOOQ-pg-int8multirange

[![CI](https://github.com/peter-evans/patience/actions/workflows/ci.yml/badge.svg)](https://github.com/peter-evans/patience/actions/workflows/ci.yml)

Custom jOOQ binding for PostgreSQL int8multirange type.

# About

Postgres 14 introduces [built-in multirange types](https://www.postgresql.org/docs/14/rangetypes.html).
Support for these types is [not yet available in jOOQ](https://github.com/jOOQ/jOOQ/issues/13172).
This repository demostrates a custom binding and converter to allow jOOQ to work with these types.

# Usage

Create a field and set the `DataType` with the binding.
```kotlin
    private val rangesField = field(
        name("test", "ranges"),
        SQLDataType.VARCHAR.asConvertedDataType(Int8MultiRangeBinding())
    )
```

See [Int8MultiRange](src/main/kotlin/Int8MultiRange.kt) and associated [tests](src/test/kotlin/Int8MultiRangeTest.kt) for working examples.
