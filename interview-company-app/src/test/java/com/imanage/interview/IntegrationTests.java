package com.imanage.interview;


import com.imanage.interview.company.Company;
import com.imanage.interview.company.CompanyService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    @Qualifier("jdbcCompanyService")
    private CompanyService jdbcCompanyService;

    @Autowired
    @Qualifier("cachedCompanyService")
    private CompanyService cachedCompanyService;

    @Test
    public void testCreate() {
        Company company = new Company(null, "iManage", "71 S. Wacker");

        Company created = restTemplate.postForObject("/companies", company, Company.class);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("iManage");

        ResponseEntity<Company> getResponse = restTemplate.getForEntity("/companies/" + created.getId(), Company.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getId()).isEqualTo(created.getId());
    }

    @Test
    public void testDelete() {
        Company company = new Company(null, "iManage", "71 S. Wacker");

        Company created = restTemplate.postForObject("/companies", company, Company.class);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/companies/" + created.getId(), HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Company> getResponse = restTemplate.getForEntity("/companies/" + created.getId(), Company.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdate() {
        Company company = new Company(null, "iManage", "An updated address!");

        Company created = restTemplate.postForObject("/companies", company, Company.class);
        Company update = new Company(created.getId(), "iManage", "An updated address!");

        ResponseEntity<Company> updateResponse = restTemplate.exchange("/companies", HttpMethod.PUT, new HttpEntity<>(update), Company.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Company> getResponse = restTemplate.getForEntity("/companies/" + created.getId(), Company.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getAddress()).isEqualTo("An updated address!");
    }

    @Test
    public void testParallelUpdates() throws Exception {
        Company company = new Company(null, "iManage", "Original Address");
        Company created = restTemplate.postForObject("/companies", company, Company.class);
        Long companyId = created.getId();

        // Number of parallel update tasks
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Callable<ResponseEntity<Company>>> tasks = new ArrayList<>();

        // Run updates in parallel with different address data
        for (int i = 0; i < numThreads; i++) {
            int index = i;
            tasks.add(() -> {
                Company update = new Company(companyId, "iManage", "Address " + index);
                return restTemplate.exchange(
                    "/companies",
                    HttpMethod.PUT,
                    new HttpEntity<>(update),
                    Company.class
                );
            });
        }

        List<Future<ResponseEntity<Company>>> futures = executor.invokeAll(tasks);
        for (Future<ResponseEntity<Company>> future : futures) {
            ResponseEntity<Company> response = future.get();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        executor.shutdown();

        // Get company address from cache and compare with the address from the database
        Company companyFromCache = cachedCompanyService.getById(companyId);
        Company companyFromDb = jdbcCompanyService.getById(companyId);

        assertThat(companyFromCache.getAddress()).isEqualTo(companyFromDb.getAddress());
    }
}
