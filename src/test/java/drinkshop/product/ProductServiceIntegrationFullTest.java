package drinkshop.product;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceIntegrationFullTest {

    // Fișier temporar folosit pentru repository real
    // → evităm afectarea datelor reale din proiect
    private File tempFile;

    // Repository real (nu mock!)
    // → testăm integrarea completă S + V + E + Repository
    private FileProductRepository repo;

    // Service real + Validator real
    private ProductService service;

    @BeforeEach
    void setUp() throws Exception {
        // Creăm un fișier temporar în folderul target/
        tempFile = new File("target/test-products.txt");

        // Curățăm fișierul înainte de fiecare test
        FileWriter writer = new FileWriter(tempFile);
        writer.write("");
        writer.close();

        // Inițializăm repository-ul real care lucrează cu fișierul
        repo = new FileProductRepository(tempFile.getAbsolutePath());

        // Service real + Validator real → integrare completă
        service = new ProductService(repo, new ProductValidator());
    }

    @Test
    void testAddProduct_FullIntegration() {
        // Arrange:
        // Creăm o entitate reală Product (E)
        Product p = new Product(1, "Cola", 10.0,
                CategorieBautura.JUICE, TipBautura.BASIC);

        // Act:
        // Apelăm metoda din Service (S)
        // Validatorul real (V) validează produsul
        // Repository-ul real îl scrie în fișier
        service.addProduct(p);

        // Assert:
        // Citim din repository → verificăm că produsul a fost salvat în fișier
        List<Product> all = service.getAllProducts();

        assertEquals(1, all.size());
        assertEquals("Cola", all.get(0).getNume());
        assertEquals(10.0, all.get(0).getPret());

        // Acest test demonstrează integrarea completă:
        // S → V → E → Repository real → fișier
    }

    @Test
    void testUpdateProduct_FullIntegration() {
        // Arrange:
        // Adăugăm un produs inițial în repository
        Product p = new Product(1, "Cola", 10.0,
                CategorieBautura.JUICE, TipBautura.BASIC);
        service.addProduct(p);

        // Act:
        // Actualizăm produsul prin Service (S)
        // Se creează o nouă entitate Product (E)
        // Repository-ul real actualizează fișierul
        service.updateProduct(1, "Sprite", 8.0,
                CategorieBautura.JUICE, TipBautura.BASIC);

        // Assert:
        // Verificăm că modificarea s-a propagat în fișier
        Product updated = service.findById(1);

        assertEquals("Sprite", updated.getNume());
        assertEquals(8.0, updated.getPret());

        // Acest test confirmă că update-ul funcționează în lanțul complet S + V + E + Repo
    }

    @Test
    void testDeleteProduct_FullIntegration() {
        // Arrange:
        // Adăugăm un produs real în repository
        Product p = new Product(1, "Cola", 10.0,
                CategorieBautura.JUICE, TipBautura.BASIC);
        service.addProduct(p);

        // Act:
        // Ștergem produsul prin Service (S)
        // Repository-ul real îl elimină din fișier
        service.deleteProduct(1);

        // Assert:
        // Verificăm că fișierul nu mai conține produsul
        List<Product> all = service.getAllProducts();
        assertEquals(0, all.size());

        // Demonstrează integrarea completă cu repository real
    }
}
