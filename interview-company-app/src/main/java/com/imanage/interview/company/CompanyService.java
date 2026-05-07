package com.imanage.interview.company;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    List<Company> getAllCompanies();
    Company getById(Long id);
    Company saveCompany(Company company);
    Company updateCompany(Company company);
    Map<String, Integer> getAllCompaniesAndEmployeeNumber();

    void deleteCompany(Long id);
}
