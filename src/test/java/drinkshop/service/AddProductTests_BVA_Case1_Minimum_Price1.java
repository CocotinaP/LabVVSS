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
 * TestLink Test Case: BVA_Case1_Minimum_Price1
 * Caz BVA 1: preț = 1 (minim valid)
 * Input: pret = 1
 * Rezultat așteptat: record adăugat
 */
class AddProductTests_BVA_Case1_Minimum_Price1 {

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
    @DisplayName("BVA-1-MIN: Add product with minimum valid price (1)")
    @Tag("bva")
    void testBVA_Case1_Minimum_Price1() {
        Product product = new Product(201, "Red Bull 0", 1.0, CategorieBautura.JUICE, TipBautura.BASIC);
        productService.addProduct(product);
        Product saved = mockRepository.findOne(201);
        assertNotNull(saved, "Product with price=1 should be saved");
        assertEquals(1.0, saved.getPret());
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