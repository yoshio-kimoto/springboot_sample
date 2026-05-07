package com.imanage.interview.company;

import com.imanage.interview.cache.Cache;
import com.imanage.interview.cache.SimpleCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imanage.interview.employee.Employee;
import com.imanage.interview.employee.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("cachedCompanyService")
public class CachedCompanyService implements CompanyService {
    private final JdbcCompanyService companyService;
    private final Cache<Long, Company> companyCache;
    private final EmployeeService employeeService;

    public CachedCompanyService(JdbcCompanyService companyService, EmployeeService employeeService) {
        this.companyService = companyService;
        this.companyCache = new SimpleCache<>();
        this.employeeService = employeeService;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @Override
    public Map<String, Integer> getAllCompaniesAndEmployeeNumber() {
        List<Company> companies = companyService.getAllCompanies();
        Map<String, Integer> map = new HashMap<>();

        for (Company company: companies) {
            Long companyId = company.getId();
            List<Employee> employees = employeeService.getEmployeesByCompanyId(companyId);
            map.put(company.getName(), employees.size());
        }

        return map;
    }

    @Override
    public Company getById(Long id) {
        Company company = companyCache.get(id);
        if (company == null) {
            company = companyService.getById(id);
        }
        return company;
    }

    @Override
    public Company saveCompany(Company company) {
        var savedCompany = companyService.saveCompany(company);
        companyCache.put(savedCompany.getId(), savedCompany);
        return savedCompany;
    }

    @Override
    @Transactional
    public Company updateCompany(Company company) {
        var updateCompany = companyService.updateCompany(company);
        companyCache.put(updateCompany.getId(), updateCompany);
        return updateCompany;
    }

    @Override
    public void deleteCompany(Long id) {
        companyService.deleteCompany(id);
        companyCache.remove(id);
    }
}
