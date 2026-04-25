package drinkshop.product;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceIntegrationSVTest {

    // Mock pentru Repository (E) – îl izolăm pentru a testa integrarea doar între S și V
    private Repository<Integer, Product> repoMock;

    // Clasa S testată împreună cu validatorul real (V)
    private ProductService service;

    @BeforeEach
    void setUp() {
        // Repository este mock → nu testăm persistența, doar fluxul S → V → E
        repoMock = Mockito.mock(Repository.class);

        // Injectăm validatorul real → test de integrare S + V
        service = new ProductService(repoMock, new ProductValidator());
    }

    @Test
    void testAddProduct_InvalidPrice_ThrowsException() {
        // Arrange: produs cu preț invalid (< 1) → validatorul real trebuie să arunce excepție
        Product p = new Product(1, "Cola", 0.5,
                CategorieBautura.JUICE, TipBautura.BASIC);

        // Act + Assert:
        // Verificăm că validatorul real (V) respinge produsul → testăm integrarea S → V
        assertThrows(ValidationException.class, () -> service.addProduct(p));

        // Verify:
        // Deoarece validarea a eșuat, repository (E) NU trebuie apelat
        verify(repoMock, never()).save(any());

        // Acest test demonstrează că S și V funcționează împreună corect,
        // iar E rămâne izolat prin mock.
    }

    @Test
    void testAddProduct_ValidProduct_SavesToRepo() {
        // Arrange: produs valid → validatorul real trebuie să îl accepte
        Product p = new Product(1, "Cola", 10.0,
                CategorieBautura.JUICE, TipBautura.BASIC);

        // Act: apelăm metoda din S
        service.addProduct(p);

        // Verify:
        // Deoarece produsul este valid, validatorul real nu aruncă excepție,
        // iar S trebuie să apeleze repository (E)
        verify(repoMock, times(1)).save(p);

        // Acest test confirmă integrarea corectă S → V → E pentru un caz valid.
    }
}
