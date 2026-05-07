package com.imanage.interview.company;

import com.imanage.interview.employee.Employee;
import com.imanage.interview.employee.EmployeeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;
    private final EmployeeService employeeService;

    public CompanyController(
        @Qualifier("cachedCompanyService") CompanyService companyService,
        EmployeeService employeeService
    ) {
        this.companyService = companyService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping
    public List<Company> getAllCompaniesAndEmployeeNumber() {
        return companyService.getAllCompanies();
    }

    @PostMapping
    public Company addCompany(@RequestBody Company company) {
        return companyService.saveCompany(company);
    }

    @DeleteMapping("/{companyId}")
    public void deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
    }

    @PutMapping
    public Company updateCompany(@RequestBody Company company) {
        return companyService.updateCompany(company);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long companyId) {
        try {
            Company company = companyService.getById(companyId);
            return ResponseEntity.ok(company);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{companyId}/employees")
    public List<Employee> getEmployeesByCompany(@PathVariable Long companyId) {
        return employeeService.getEmployeesByCompanyId(companyId);
    }
}
