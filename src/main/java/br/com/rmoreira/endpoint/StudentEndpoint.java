package br.com.rmoreira.endpoint;

import br.com.rmoreira.error.CustomErrorType;
import br.com.rmoreira.error.ResourceNotFoundException;
import br.com.rmoreira.model.Student;
import br.com.rmoreira.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("students")
public class StudentEndpoint {

    private final StudentRepository studentDao;

    @Autowired
    public StudentEndpoint(StudentRepository studentDao) {
        this.studentDao = studentDao;
    }

    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<?> listAll() {
        return new ResponseEntity<>(studentDao.findAll(), HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {
        verifyStudentExists(id);
        Optional<Student> student = studentDao.findById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> getStudentByName(@PathVariable("name") String name) {

        List<Student> students = studentDao.findByNameIgnoreCaseContaining(name);
        if (students.isEmpty()) {
            return new ResponseEntity<>(new CustomErrorType("Student not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.POST)
    @PostMapping
    @Transactional
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        return new ResponseEntity<>(studentDao.save(student), HttpStatus.CREATED);
    }

    //@RequestMapping(method = RequestMethod.DELETE)
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyStudentExists(id);
        studentDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.PUT)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Student student) {
        verifyStudentExists(student.getId());
        studentDao.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyStudentExists(Long id) {
        if (studentDao.findById(id) == null) {
            throw new ResourceNotFoundException("Student not found for ID: " + id);
        }
    }
}
