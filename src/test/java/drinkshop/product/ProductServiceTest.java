package drinkshop.product;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    // Mock pentru Repository (E) – necesar pentru testarea în izolare a clasei S
    private Repository<Integer, Product> repoMock;

    // Mock pentru Validator (V) – pentru a nu apela validarea reală
    private Validator<Product> validatorMock;

    // Clasa S testată în izolare
    private ProductService service;

    @BeforeEach
    void setUp() {
        // Creăm mock‑uri pentru dependențele lui S (V și E)
        repoMock = Mockito.mock(Repository.class);
        validatorMock = Mockito.mock(Validator.class);

        // Injectăm mock‑urile → respectăm scenariul S → V → E
        service = new ProductService(repoMock, validatorMock);
    }

    @Test
    void testAddProduct_CallsValidatorAndRepository() {
        // Arrange: creăm un produs valid
        Product p = new Product(1, "Cola", 10.0,
                CategorieBautura.JUICE, TipBautura.BASIC);

        // Act: apelăm metoda din S
        service.addProduct(p);

        // Assert + Verify:
        // Verificăm că validatorul (V) a fost apelat → respectă scenariul S → V
        verify(validatorMock, times(1)).validate(p);

        // Verificăm că repository (E) a fost apelat → respectă scenariul S → E
        verify(repoMock, times(1)).save(p);

        // Acest test demonstrează testarea în izolare:
        // S este testată independent, iar V și E sunt mock‑uri.
    }

    @Test
    void testGetAllProducts_ReturnsCorrectList() {
        // Arrange: pregătim datele returnate de repository (E)
        Product p1 = new Product(1, "Cola", 10.0,
                CategorieBautura.JUICE, TipBautura.BASIC);
        Product p2 = new Product(2, "Fanta", 9.0,
                CategorieBautura.JUICE, TipBautura.BASIC);

        // Mock‑ul repo returnează o listă → izolăm complet clasa S
        when(repoMock.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Act: apelăm metoda din S
        List<Product> result = service.getAllProducts();

        // Assert: verificăm rezultatul
        assertEquals(2, result.size());
        assertEquals("Cola", result.get(0).getNume());
        assertEquals("Fanta", result.get(1).getNume());

        // Verify: repository (E) a fost apelat o singură dată
        verify(repoMock, times(1)).findAll();

        // Testul demonstrează că S funcționează corect cu E mock‑uit,
        // deci este testare în izolare conform cerinței.
    }
}
