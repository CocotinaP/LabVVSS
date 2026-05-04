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
 * TestLink Test Case: ECP_TC1_AllValid
 * Testez că se adaugă un produs când toate câmpurile sunt corecte
 * Input: name="Red Bull 0", pret=10, tip=Water_Based
 * Rezultat așteptat: record adăugat
 */
class AddProductTests_ECP_TC1_AllValid {

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
    @DisplayName("TC1: EC1,2,3 valid - all fields valid")
    @Tag("ecp")
    void testECP_TC1_AllValid() {
        // Arrange
        Product product = new Product(1, "Red Bull 0", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        // Act
        productService.addProduct(product);

        // Assert
        Product saved = mockRepository.findOne(1);
        assertNotNull(saved, "Product with all valid ECs should be saved");
        assertEquals("Red Bull 0", saved.getNume());
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