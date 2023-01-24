package com.example.springboottutorialamigoscode.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional =
                studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) { // Email kullanılıyorsa
            throw new IllegalStateException("Email taken");
        }

        studentRepository.save(student);
    }

    public void deleteStuden(Long studentId) {
        // id varmı diya bakar
        boolean exists = studentRepository.existsById(studentId);

        if (!exists) {
            throw new IllegalStateException("student with id "
                    + studentId + " does not exists");
        }

        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        // Student'ın o id ile var olup olmadığına bakıyoruz, yoksa  exception atıyoruz.
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "student with " + studentId + " does not exists"
                ));

        // Eğer isim null değilse ve isim uzunluğu sıfırdan büyükse ve verilen
        // isim mevcut isimle aynı değilse -> güncellem işlemi gerçekleşir
        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentOptional =
                    studentRepository.findStudentByEmail(email);
            // Yeni vereceğimiz email'in önceden alınmış olup olmadığını kontrol ediyoruz.
            if (studentOptional.isPresent()) { // Email kullanılıyorsa
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }
    }
}
