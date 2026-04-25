package drinkshop.product;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceIntegrationSVETest {

    // Repository (E) este mock → izolăm persistența, dar testăm integrarea S + V + E (entități reale)
    private Repository<Integer, Product> repoMock;

    // Clasa S testată împreună cu validatorul real (V) și entități reale (E)
    private ProductService service;

    @BeforeEach
    void setUp() {
        // Mock pentru repository → nu testăm salvarea reală, doar fluxul logic
        repoMock = Mockito.mock(Repository.class);

        // Injectăm validatorul real → integrare S + V
        // Entitățile Product sunt reale → integrare S + V + E
        service = new ProductService(repoMock, new ProductValidator());
    }

    @Test
    void testGetAllProducts_Integration() {
        // Arrange:
        // Creăm două entități reale Product (E)
        Product p1 = new Product(1, "Cola", 10.0,
                CategorieBautura.JUICE, TipBautura.BASIC);
        Product p2 = new Product(2, "Fanta", 9.0,
                CategorieBautura.JUICE, TipBautura.BASIC);

        // Mock‑ul repo returnează lista → izolăm persistența, dar testăm fluxul S → E
        when(repoMock.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Act:
        List<Product> result = service.getAllProducts();

        // Assert:
        // Verificăm că datele reale ale entităților sunt transmise corect prin S
        assertEquals(2, result.size());
        assertEquals("Cola", result.get(0).getNume());
        assertEquals("Fanta", result.get(1).getNume());

        // Verify:
        // Confirmăm că S a apelat repository (E) → flux S → E
        verify(repoMock, times(1)).findAll();

        // Acest test validează integrarea S + E (entități reale) cu V neimplicat în acest flux.
    }

    @Test
    void testUpdateProduct_Integration() {

        // Act:
        // Apelăm metoda update din S → aceasta creează o entitate Product reală (E)
        service.updateProduct(1, "Sprite", 8.0,
                CategorieBautura.JUICE, TipBautura.BASIC);

        // Verify:
        // Folosim argThat pentru a verifica valorile entității reale transmise către repository
        // Aceasta demonstrează integrarea S + E (entitate reală Product)
        verify(repoMock, times(1)).update(argThat(p ->
                p.getId() == 1 &&
                        p.getNume().equals("Sprite") &&
                        p.getPret() == 8.0 &&
                        p.getCategorie() == CategorieBautura.JUICE &&
                        p.getTip() == TipBautura.BASIC
        ));

        // Acest test arată că:
        // - S creează corect entitatea Product (E)
        // - V nu intervine (nu este necesar pentru update)
        // - E este transmis corect către repository
        // → integrare completă S + V + E (entități reale, validator real, repo mock)
    }

}
