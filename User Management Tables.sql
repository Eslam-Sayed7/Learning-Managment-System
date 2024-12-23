-- docker run -d \
--            --name postgres_LMS_container \
--            -e POSTGRES_USER=postgres \
--            -e POSTGRES_PASSWORD=66c#Abi^Xqjj \
--            -e POSTGRES_DB=LMSplatform \
--            -p 5432:5432 \
--            postgres

-- Create the LMSplatform Database
CREATE DATABASE LMSplatform;

-- Connect to the LMSplatform Database
\c LMSplatform;

-- Roles Table
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Initial Roles
INSERT INTO roles (role_name, description) VALUES
('ROLE_STUDENT', 'Student role'),
('ROLE_INSTRUCTOR', 'Instructor role'),
('ROLE_ADMIN', 'Administrator role');

-- Users Table
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
	role_id INT,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE SET NULL
);

-- Students Table
CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    major VARCHAR(255),
    year_of_study INT,
    additional_info JSONB
);

-- Instructors Table
CREATE TABLE instructors (
    instructor_id SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    department VARCHAR(255),
    title VARCHAR(255),
    additional_info JSONB
);

-- Admins Table
CREATE TABLE admins (
    admin_id SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    access_level VARCHAR(50),
    additional_info JSONB
);

-- Courses Table
CREATE TABLE courses (
    course_id SERIAL PRIMARY KEY,
    course_name VARCHAR(255) NOT NULL,
    description TEXT,
    instructor_id INT REFERENCES instructors(instructor_id) ON DELETE SET NULL
);

-- Enrollments Table
CREATE TABLE enrollments (
    enrollment_id SERIAL PRIMARY KEY,
    student_id INT REFERENCES students(student_id) ON DELETE CASCADE,
    course_id INT REFERENCES courses(course_id) ON DELETE CASCADE,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, course_id)
);

-- Lessons Table
CREATE TABLE lessons (
    lesson_id SERIAL PRIMARY KEY,
    course_id INT REFERENCES courses(course_id) ON DELETE CASCADE,
    lesson_name VARCHAR(255) NOT NULL,
    lesson_date TIMESTAMP NOT NULL,
    otp VARCHAR(6),
    otp_expiration TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    lesson_type VARCHAR(10) CHECK (lesson_type IN ('PDF', 'Video')) NOT NULL, -- Specify type: PDF or Video
    file_url TEXT -- Store the URL for the PDF or Video
);

-- Attendance Table
CREATE TABLE attendance (
    attendance_id SERIAL PRIMARY KEY,
    student_id INT REFERENCES students(student_id) ON DELETE CASCADE,
    lesson_id INT REFERENCES lessons(lesson_id) ON DELETE CASCADE,
    otp_entered VARCHAR(6),
    is_verified BOOLEAN DEFAULT FALSE,
    attendance_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Assessment Types Table
CREATE TABLE assessment_types (
    assessment_type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(50) UNIQUE NOT NULL
);

-- Assessments Table
CREATE TABLE assessments (
    assessment_id SERIAL PRIMARY KEY,
    course_id INT REFERENCES courses(course_id) ON DELETE CASCADE,
    type_id INT REFERENCES assessment_types(assessment_type_id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Questions Table
CREATE TABLE questions (
    question_id SERIAL PRIMARY KEY,
    assessment_id INT REFERENCES assessments(assessment_id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    question_type VARCHAR(50) CHECK (question_type IN ('MCQ', 'True/False'))
);

-- Choices Table
CREATE TABLE choices (
    choice_id SERIAL PRIMARY KEY,
    question_id INT REFERENCES questions(question_id) ON DELETE CASCADE,
    choice_text TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE
);

-- Submissions Table
CREATE TABLE submissions (
    submission_id SERIAL PRIMARY KEY,
    assessment_id INT REFERENCES assessments(assessment_id) ON DELETE CASCADE,
    student_id INT REFERENCES students(student_id) ON DELETE CASCADE,
    file_path VARCHAR(255),
    submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    grade DECIMAL(5, 2),
    feedback TEXT
);

-- Progress Tracking Table
CREATE TABLE progress_tracking (
    progress_id SERIAL PRIMARY KEY,
    student_id INT REFERENCES students(student_id) ON DELETE CASCADE,
    course_id INT REFERENCES courses(course_id) ON DELETE CASCADE,
    quiz_scores JSONB,
    assignments_submitted INT DEFAULT 0,
    attendance_count INT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Notifications Table
CREATE TABLE notifications (
    notification_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    type VARCHAR(50) CHECK (type IN ('ENROLLMENT', 'GRADE', 'COURSE_UPDATE')) NOT NULL, -- Type of notification

    content TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for Optimization
CREATE INDEX idx_user_email ON users (email);
CREATE INDEX idx_user_username ON users (username);
CREATE INDEX idx_role_name ON roles (role_name);
CREATE INDEX idx_course_name ON courses (course_name);

-- Insert Example Assessment Types
INSERT INTO assessment_types (type_name) VALUES
('Quiz'),
('Assignment');

-- First, create the function separately
BEGIN;

-- Create the function
CREATE OR REPLACE FUNCTION calculate_grade(p_submission_id INT)
RETURNS BOOLEAN AS $$
DECLARE
    correct_answers INT;
    total_questions INT;
    calculated_grade DECIMAL(5, 2);  -- renamed to avoid confusion with UPDATE
BEGIN
    -- Get correct answers count
    SELECT COUNT(*) INTO correct_answers
    FROM choices c
    JOIN questions q ON c.question_id = q.question_id
    WHERE c.is_correct = TRUE AND q.assessment_id = (
        SELECT assessment_id
        FROM submissions s
        WHERE s.submission_id = p_submission_id
    );

    -- Get total questions count
    SELECT COUNT(*) INTO total_questions
    FROM questions q
    WHERE q.assessment_id = (
        SELECT assessment_id
        FROM submissions s
        WHERE s.submission_id = p_submission_id
    );

    -- Handle division by zero
    IF total_questions = 0 THEN
        UPDATE submissions
        SET grade = 0,
            feedback = 'No questions found'
        WHERE submission_id = p_submission_id;
        RETURN FALSE;
    END IF;

    -- Calculate grade
    calculated_grade := (correct_answers::DECIMAL / total_questions) * 100;

    -- Update the submission with the calculated grade
    UPDATE submissions
    SET grade = calculated_grade,
        feedback = CASE
            WHEN calculated_grade >= 70 THEN 'Passed'
            ELSE 'Failed - Score below 70%'
        END
    WHERE submission_id = p_submission_id;

    RETURN calculated_grade >= 70;
END;
$$ LANGUAGE plpgsql;
BEGIN;
-- Now test the function with a separate transaction
--DO $$
--DECLARE
--    v_user_id INTEGER;
--    v_instructor_id INTEGER;
--    v_course_id INTEGER;
--    v_assessment_id INTEGER;
--    v_question_id1 INTEGER;
--    v_question_id2 INTEGER;
--    v_student_user_id INTEGER;
--    v_student_id INTEGER;
--    v_submission_id INTEGER;
--    v_test_result BOOLEAN;
--    v_grade DECIMAL(5,2);
--    v_feedback TEXT;
--BEGIN
--    -- Create test instructor
--    INSERT INTO users (username, email, password, role_id)
--    VALUES ('test_instructor', 'instructor@test.com', 'password123',
--           (SELECT role_id FROM roles WHERE role_name = 'ROLE_INSTRUCTOR'))
--    RETURNING user_id INTO v_user_id;
--
--    INSERT INTO instructors (user_id, department, title)
--    VALUES (v_user_id, 'Computer Science', 'Professor')
--    RETURNING instructor_id INTO v_instructor_id;
--
--    -- Create test course
--    INSERT INTO courses (course_name, description, instructor_id)
--    VALUES ('Test Course', 'Test Course Description', v_instructor_id)
--    RETURNING course_id INTO v_course_id;
--
--    -- Create test assessment
--    INSERT INTO assessments (course_id, type_id, name, description)
--    VALUES (
--        v_course_id,
--        (SELECT assessment_type_id FROM assessment_types WHERE type_name = 'Quiz'),
--        'Test Quiz',
--        'Test Quiz Description'
--    ) RETURNING assessment_id INTO v_assessment_id;
--
--    -- Create test questions
--    INSERT INTO questions (assessment_id, question_text, question_type)
--    VALUES (v_assessment_id, 'What is 2+2?', 'MCQ')
--    RETURNING question_id INTO v_question_id1;
--
--    -- Add choices
--    INSERT INTO choices (question_id, choice_text, is_correct)
--    VALUES
--        (v_question_id1, '3', FALSE),
--        (v_question_id1, '4', TRUE),
--        (v_question_id1, '5', FALSE);
--
--    -- Create test student
--    INSERT INTO users (username, email, password, role_id)
--    VALUES ('test_student', 'student@test.com', 'password123',
--           (SELECT role_id FROM roles WHERE role_name = 'ROLE_STUDENT'))
--    RETURNING user_id INTO v_student_user_id;
--
--    INSERT INTO students (user_id, major, year_of_study)
--    VALUES (v_student_user_id, 'Computer Science', 2)
--    RETURNING student_id INTO v_student_id;
--
--    -- Create submission
--    INSERT INTO submissions (assessment_id, student_id)
--    VALUES (v_assessment_id, v_student_id)
--    RETURNING submission_id INTO v_submission_id;
--
--    -- Test the function
--    v_test_result := calculate_grade(v_submission_id);
--
--    -- Get results
--    SELECT grade, feedback
--    INTO v_grade, v_feedback
--    FROM submissions
--    WHERE submission_id = v_submission_id;
--
--    RAISE NOTICE 'Test Result: %, Grade: %, Feedback: %', v_test_result, v_grade, v_feedback;
--
--EXCEPTION
--    WHEN OTHERS THEN
--        RAISE NOTICE 'Error: %', SQLERRM;
--        RAISE;
--END $$;
--
--COMMIT;
