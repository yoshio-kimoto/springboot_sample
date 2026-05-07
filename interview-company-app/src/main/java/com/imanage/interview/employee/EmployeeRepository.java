package com.imanage.interview.employee;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Employee save(Employee employee) {
        String sql = "INSERT INTO employees (company_id, name) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, employee.getCompanyId());
            ps.setString(2, employee.getName());
            return ps;
        }, keyHolder);
        employee.setId(keyHolder.getKey().longValue());
        return employee;
    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Employee.class));
    }

    public Employee findById(Long id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Employee.class), id);
    }

    public List<Employee> findByCompanyId(Long id) {
        String sql = "SELECT * FROM employees WHERE company_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Employee.class), id);
    }

    public int update(Employee employee) {
        String sql = "UPDATE employees SET company_id = ?, name= ?, WHERE id = ?";
        return jdbcTemplate.update(sql, employee.getCompanyId(), employee.getName());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
