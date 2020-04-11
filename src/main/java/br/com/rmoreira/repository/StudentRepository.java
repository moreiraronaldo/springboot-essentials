package br.com.rmoreira.repository;

import br.com.rmoreira.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student> findByNameIgnoreCaseContaining(String name);
}
