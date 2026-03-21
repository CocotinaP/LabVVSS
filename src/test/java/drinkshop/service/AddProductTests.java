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
 * Suite de teste: pdir3599_BBT - Teste pentru adăugarea de produse
 * Specificație VVSS: 4 cazuri de test ECP + 6 cazuri de test BVA
 * Metode de testare folosite:
 * - ECP (Equivalence Class Partitioning)
 * - BVA (Boundary Value Analysis)
 */
class AddProductTests {

    private ProductService productService;
    private MockRepository mockRepository;
    private Validator<Product> validator;

    @BeforeEach
    void setUp() {
        mockRepository = new MockRepository();
        validator = new ProductValidator();
        productService = new ProductService(mockRepository, validator);
    }

    // ============================================
    // TESTE ECP: Equivalence Class Partitioning
    // 3 clase: EC1-nume valid, EC2-pret>0, EC3-tip not null
    // ============================================

    /**
     * Caz de test ECP 1: Toate EC valide
     * Testez că se adaugă un produs când toate câmpurile sunt corecte
     * Input: name="Red Bull 0", pret=10, tip=Water_Based
     * Rezultat așteptat: record adăugat
     */
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
     * Caz de test ECP 2: Doar nume invalid + alte câmpuri OK
     * Testez ce se întâmplă când numele e gol
     * Input: name="", pret=15, tip=suc
     * Rezultat așteptat: Arunca eroare - nume gol
     */
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
     * Caz de test ECP 3: Doar preț invalid + alte câmpuri OK
     * Testez ce se întâmplă când prețul e 0
     * Input: name="Red Bull 0", pret=0, tip=fresh
     * Rezultat așteptat: Arunca eroare - preț trebuie >= 1
     */
    @Test
    @DisplayName("TC3: EC2 invalid + EC1,3 valid - price zero")
    @Tag("ecp")
    void testECP_TC3_InvalidPrice() {
        // Arrange
        Product product = new Product(3, "Red Bull 0", 0.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productService.addProduct(product);
        });
        assertTrue(exception.getMessage().contains("Pret invalid"));
        assertNull(mockRepository.findOne(3), "Product with EC2 invalid should NOT be saved");
    }

    /**
     * Caz de test ECP 4: Doar tip invalid + alte câmpuri OK
     * Testez ce se întâmplă când tipul e null
     * Input: name="Red Bull 0", pret=10, tip=null
     * Rezultat așteptat: (încă nu e validare pentru null tip)
     */
    @Test
    @DisplayName("TC4: EC3 invalid + EC1,2 valid - null tip")
    @Tag("ecp")
    void testECP_TC4_InvalidTip() {
        // Arrange
        Product product = new Product(4, "Red Bull 0", 10.0, CategorieBautura.JUICE, null);

        // Act
        productService.addProduct(product);

        // Assert
        Product saved = mockRepository.findOne(4);
        assertNotNull(saved, "Product is accepted (no tip null validation)");
    }

    // ============================================
    // TESTE BVA: Boundary Value Analysis (6 cazuri)
    // ============================================

    /**
     * Caz BVA 1: preț = 1 (minim valid)
     * Input: pret = 1
     * Rezultat așteptat: record adăugat
     */
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
     * Caz BVA 2: preț = 0 (minim-1, sub minim)
     * Input: pret = 0
     * Rezultat așteptat: Arunca eroare - preț trebuie >=1
     */
    @Test
    @DisplayName("BVA-2-MIN-1: Reject product with price below minimum (0)")
    @Tag("bva")
    void testBVA_Case2_BelowMinimum_Price0() {
        Product product = new Product(202, "Red Bull 0", 0.0, CategorieBautura.JUICE, TipBautura.BASIC);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productService.addProduct(product);
        });
        assertTrue(exception.getMessage().contains("Pret invalid"));
        assertNull(mockRepository.findOne(202));
    }

    /**
     * Caz BVA 3: preț = 2 (minim+1, chiar deasupra minimului)
     * Input: pret = 2
     * Rezultat așteptat: record adăugat
     */
    @Test
    @DisplayName("BVA-3-MIN+1: Add product with price just above minimum (2)")
    @Tag("bva")
    void testBVA_Case3_AboveMinimum_Price2() {
        Product product = new Product(203, "Red Bull 0", 2.0, CategorieBautura.JUICE, TipBautura.BASIC);
        productService.addProduct(product);
        Product saved = mockRepository.findOne(203);
        assertNotNull(saved, "Product with price=2 should be saved");
        assertEquals(2.0, saved.getPret());
    }

    /**
     * Caz BVA 4: preț = 1000 (maxim valid)
     * Input: pret = 1000
     * Rezultat așteptat: record adăugat
     */
    @Test
    @DisplayName("BVA-4-MAX: Add product with maximum valid price (1000)")
    @Tag("bva")
    void testBVA_Case4_Maximum_Price1000() {
        Product product = new Product(204, "Red Bull 0", 1000.0, CategorieBautura.JUICE, TipBautura.BASIC);
        productService.addProduct(product);
        Product saved = mockRepository.findOne(204);
        assertNotNull(saved, "Product with price=1000 should be saved");
        assertEquals(1000.0, saved.getPret());
    }

    /**
     * Caz BVA 5: preț = 999 (maxim-1, chiar sub maxim)
     * Input: pret = 999
     * Rezultat așteptat: record adăugat
     */
    @Test
    @DisplayName("BVA-5-MAX-1: Add product with price just below maximum (999)")
    @Tag("bva")
    void testBVA_Case5_BelowMaximum_Price999() {
        Product product = new Product(205, "Red Bull 0", 999.0, CategorieBautura.JUICE, TipBautura.BASIC);
        productService.addProduct(product);
        Product saved = mockRepository.findOne(205);
        assertNotNull(saved, "Product with price=999 should be saved");
        assertEquals(999.0, saved.getPret());
    }

    /**
     * Caz BVA 6: preț = 1001 (maxim+1, depășește maxim)
     * Input: pret = 1001
     * Rezultat așteptat: Arunca eroare - preț prea mare
     */
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
     * Simulates the behavior of a real repository without database dependencies
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
        public Product delete(Integer id) {
            return storage.remove(id);
        }

        @Override
        public Product update(Product entity) {
            storage.put(entity.getId(), entity);
            return entity;
        }
    }
}
