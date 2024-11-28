package com.nuamx.customers.integrationtesting;

import com.nuamx.customers.model.Customer;
import com.nuamx.customers.repository.CustomerRepository;
import com.nuamx.customers.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest // Requerido para el uso de anotaciones de Spring Boot
@Testcontainers // Indica el contexto de ejecución de TesContainers
@Transactional // Asegura que los cambios en la BD se reviertan después de cada prueba
public class CustomerIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    /**
     * Contenedor de PostgreSQL de pruebas
     */
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    /**
     * Método que sobreescribe las propiedades del application.properties de pruebas
     * @param registry
     */
    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    /**
     * Método que se ejecuta antes de cualquier prueba. Inicializa el contenedor.
     */
    @BeforeAll
    static void setup() {
        postgres.start();
    }

    /**
     * Prueba de integración. Valida la persistencia de un customer en la base de datos.
     */
    @Test
    void shouldSaveCustomerInDatabase() {
        // Se instancia el customer que se va a persistir
        Customer customer = new Customer();
        customer.setName("Pepito Perez");
        customer.setEmail("pepito.perez@nuamx.com");

        // Se ejecuta el método saveCustomer del servicio que persiste el customer
        Customer savedCustomer = customerService.saveCustomer(customer);

        // Se ejecutan las aserciones para validar que el desarrollo esté OK
        assertNotNull(savedCustomer.getId());
        assertNotNull(customerRepository.findById(savedCustomer.getId()));
    }

    /**
     * Prueba de integración. Valida que efectivamente no permita insertar un registro de email duplicado.
     */
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExistsInDatabase() {
        // Se instancia el customer que se va a persistir
        Customer customer = new Customer();
        customer.setName("Pepito Perez");
        customer.setEmail("pepito.perez@nuamx.com");

        // Se ejecuta el método save directamente del Repository para persistir el customer
        customerRepository.save(customer);

        // Se instancia un nuevo customer con email duplicado
        Customer newCustomer = new Customer();
        newCustomer.setEmail("pepito.perez@nuamx.com");

        // Se ejecutan las aserciones para validar que el desarrollo esté OK. Para este caso se usa el servicio
        assertThrows(IllegalArgumentException.class, () -> customerService.saveCustomer(newCustomer));
    }

}
