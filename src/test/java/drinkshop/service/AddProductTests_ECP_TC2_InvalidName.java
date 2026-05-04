package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TestLink Test Case: ECP_TC2_InvalidName
 * Testez ce se întâmplă când numele e gol
 * Input: name="", pret=15, tip=suc
 * Rezultat așteptat: Arunca eroare - nume gol
 */
class AddProductTests_ECP_TC2_InvalidName {

    private ProductService productService;
    private MockRepository mockRepository;
    private Validator<Product> validator;

    @BeforeEach
    void setUp() {
        mockRepository = new MockRepository();
        validator = new ProductValidator();
        productService = new ProductService(mockRepository, validator);
    }

    @Test
    @DisplayName("TC2: EC1 invalid + EC2,3 valid - empty name")
    @Tag("ecp")
    void testECP_TC2_InvalidName() {
        // Arrange
        Product product = new Product(2, "", 15.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productService.addProduct(product);
        });
        assertTrue(exception.getMessage().contains("Numele nu poate fi gol"));
        assertNull(mockRepository.findOne(2), "Product with EC1 invalid should NOT be saved");
    }

    /**
     * Mock Repository for testing purposes
     */
    static class MockRepository implements Repository<Integer, Product> {
        private final Map<Integer, Product> storage = new HashMap<>();

        @Override
        public Product findOne(Integer id) {
            return storage.get(id);
        }

        @Override
        public List<Product> findAll() {
            return new ArrayList<>(storage.values());
        }

        @Override
        public Product save(Product entity) {
            storage.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public Product update(Product entity) {
            storage.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public Product delete(Integer id) {
            return storage.remove(id);
        }
    }
}