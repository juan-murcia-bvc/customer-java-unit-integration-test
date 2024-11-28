package com.nuamx.customers.unittesting;

import com.nuamx.customers.model.Customer;
import com.nuamx.customers.repository.CustomerRepository;
import com.nuamx.customers.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    /**
     * Prueba unitaria. Valida que se esté generando la excepción según la validación de negocio a nivel del servicio.
     */
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Se mockea el repositorio
        CustomerRepository mockRepository = Mockito.mock(CustomerRepository.class);
        CustomerService customerService = new CustomerService(mockRepository);

        // Se configura el comportamiento del mock para que simule que el email si existe
        when(mockRepository.findByEmail("pepito.perez@nuamx.com")).thenReturn(Optional.of(new Customer()));

        // Se instancia el customer que se pretende persistir
        Customer customer = new Customer();
        customer.setEmail("pepito.perez@nuamx.com");

        // Se ejecutan las aserciones para validar que el desarrollo esté OK
        assertThrows(IllegalArgumentException.class, () -> customerService.saveCustomer(customer));
    }

    /**
     * Prueba unitaria. Valida que la lógica de negocio esté funcionando según las validaciones de negocio implementadas.
     */
    @Test
    void shouldSaveCustomerWhenEmailDoesNotExist() {
        // Se mockea el repositorio
        CustomerRepository mockRepository = Mockito.mock(CustomerRepository.class);
        CustomerService customerService = new CustomerService(mockRepository);

        // Se configura el comportamiento del mock para que simule que el email no existe
        when(mockRepository.findByEmail("pepito.perez@nuamx.com")).thenReturn(Optional.empty());

        // Se instancia el customer que se va a persistir
        Customer customer = new Customer();
        customer.setName("Pepito Perez");
        customer.setEmail("pepito.perez@nuamx.com");

        // Se configura el comportamiento del mock para que simule la inserción del customer
        when(mockRepository.save(customer)).thenReturn(customer);

        // Se ejecuta el método saveCustomer del servicio que persiste el customer
        Customer savedCustomer = customerService.saveCustomer(customer);

        // Se ejecutan las aserciones para validar que el desarrollo esté OK
        assertEquals("Pepito Perez", savedCustomer.getName());
        assertEquals("pepito.perez@nuamx.com", savedCustomer.getEmail());
    }

}
