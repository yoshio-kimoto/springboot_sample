package com.imanage.interview.company;

import java.util.List;
import org.springframework.stereotype.Service;

@Service("jdbcCompanyService")
public class JdbcCompanyService implements CompanyService {
    private final CompanyRepository companyRepository;

    public JdbcCompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(Company company) {
        return companyRepository.update(company);
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.delete(id);
    }
}
