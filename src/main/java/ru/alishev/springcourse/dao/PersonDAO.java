package ru.alishev.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        List<Person> people = jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));

        return people.stream()
                .sorted(Comparator.comparing(Person::getName))
                .collect(Collectors.toList());
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, age, email) VALUES(?, ?, ?)",
                person.getName(), person.getAge(), person.getEmail());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person SET name=?, age=?, email=? WHERE id=?",
                updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }
}
