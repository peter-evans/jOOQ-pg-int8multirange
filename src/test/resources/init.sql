CREATE TABLE test (
    ranges int8multirange NOT NULL
);

INSERT INTO test (ranges) VALUES ('{[1, 10],[12, 20]}');
INSERT INTO test (ranges) VALUES ('{[1, 5],[7, 13]}');
