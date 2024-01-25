CREATE TABLE IF NOT EXISTS groups
(
    group_id   SERIAL PRIMARY KEY,
    group_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS students
(
    student_id SERIAL PRIMARY KEY,
    group_id   INT REFERENCES groups (group_id),
    first_name VARCHAR(255),
    last_name  VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS courses
(
    course_id          SERIAL PRIMARY KEY,
    course_name        VARCHAR(255),
    course_description TEXT
);

CREATE TABLE IF NOT EXISTS student_courses
(
    student_id INT,
    course_id  INT,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students (student_id),
    FOREIGN KEY (course_id) REFERENCES courses (course_id)
);
