package com.imanage.interview.company;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyRepository {
    private final JdbcTemplate jdbcTemplate;

    public CompanyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Company save(Company company) {
        String sql = "INSERT INTO companies (name, address) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, company.getName());
            ps.setString(2, company.getAddress());
            return ps;
        }, keyHolder);
        company.setId(keyHolder.getKey().longValue());
        return company;
    }

    public List<Company> findAll() {
        String sql = "SELECT * FROM companies";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Company.class));
    }

    public Company findById(Long id) {
        String sql = "SELECT * FROM companies WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Company.class), id);
    }

    public Company update(Company company) {
        String sql = "UPDATE companies SET name = ?, address = ? WHERE id = ?";
        jdbcTemplate.update(sql, company.getName(), company.getAddress(), company.getId());
        return company;
    }

    public int delete(Long id) {
        String sql = "DELETE FROM companies WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
