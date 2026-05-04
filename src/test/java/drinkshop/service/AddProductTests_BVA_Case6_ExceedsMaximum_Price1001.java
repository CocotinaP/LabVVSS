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
 * TestLink Test Case: BVA_Case6_ExceedsMaximum_Price1001
 * Caz BVA 6: preț = 1001 (maxim+1, depășește maxim)
 * Input: pret = 1001
 * Rezultat așteptat: Arunca eroare - preț prea mare
 */
class AddProductTests_BVA_Case6_ExceedsMaximum_Price1001 {

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
    @DisplayName("BVA-6-MAX+1: Reject product with price exceeding maximum (1001)")
    @Tag("bva")
    void testBVA_Case6_ExceedsMaximum_Price1001() {
        Product product = new Product(206, "Red Bull 0", 1001.0, CategorieBautura.JUICE, TipBautura.BASIC);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productService.addProduct(product);
        });
        assertTrue(exception.getMessage().contains("Pret invalid"));
        assertNull(mockRepository.findOne(206), "Product with price>1000 should NOT be saved");
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