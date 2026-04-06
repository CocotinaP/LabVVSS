package drinkshop.service;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.repository.AbstractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StocServiceTest {

    private InMemoryStocRepository repo;
    private StocService service;

    @BeforeEach
    void setUp() {
        repo = new InMemoryStocRepository();
        service = new StocService(repo);
    }

    @Test
    void consuma_stocInsuficient_throwsException() {
        repo.save(new Stoc(1, "espresso", 50, 10));
        Reteta reteta = new Reteta(1, List.of(
                new IngredientReteta("espresso", 100)
        ));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> service.consuma(reteta)
        );

        assertEquals("Stoc insuficient pentru rețeta.", ex.getMessage());
        assertEquals(50.0, repo.findOne(1).getCantitate(), 0.001);
    }

    @Test
    void consuma_retetaFaraIngrediente_nuModificaStocul() {
        repo.save(new Stoc(1, "espresso", 50, 10));
        Reteta reteta = new Reteta(2, List.of());

        service.consuma(reteta);

        assertEquals(50.0, repo.findOne(1).getCantitate(), 0.001);
    }

    @Test
    void consuma_unIngredient_unSingurStoc_consumaCorect() {
        repo.save(new Stoc(1, "espresso", 100, 10));
        Reteta reteta = new Reteta(3, List.of(
                new IngredientReteta("espresso", 50)
        ));

        service.consuma(reteta);

        assertEquals(50.0, repo.findOne(1).getCantitate(), 0.001);
    }

    @Test
    void consuma_unIngredient_douaPozitiiStoc_consumaDinAmbele() {
        repo.save(new Stoc(1, "lapte", 30, 10));
        repo.save(new Stoc(2, "lapte", 50, 10));
        Reteta reteta = new Reteta(4, List.of(
                new IngredientReteta("lapte", 70)
        ));

        service.consuma(reteta);

        assertAll(
                () -> assertEquals(0.0, repo.findOne(1).getCantitate(), 0.001),
                () -> assertEquals(10.0, repo.findOne(2).getCantitate(), 0.001)
        );
    }

    @Test
    void consuma_ramasDevineZero_seExecutaBreak() {
        repo.save(new Stoc(1, "sirop", 50, 10));
        repo.save(new Stoc(2, "sirop", 30, 10));
        Reteta reteta = new Reteta(5, List.of(
                new IngredientReteta("sirop", 20)
        ));

        service.consuma(reteta);

        assertAll(
                () -> assertEquals(30.0, repo.findOne(1).getCantitate(), 0.001),
                () -> assertEquals(30.0, repo.findOne(2).getCantitate(), 0.001)
        );
    }

    @Test
    void consuma_douaIngrediente_consumaCorectPentruAmbele() {
        repo.save(new Stoc(1, "espresso", 50, 10));
        repo.save(new Stoc(2, "lapte", 60, 10));
        Reteta reteta = new Reteta(6, List.of(
                new IngredientReteta("espresso", 30),
                new IngredientReteta("lapte", 40)
        ));

        service.consuma(reteta);

        assertAll(
                () -> assertEquals(20.0, repo.findOne(1).getCantitate(), 0.001),
                () -> assertEquals(20.0, repo.findOne(2).getCantitate(), 0.001)
        );
    }

    /**
     * Repository in-memory pentru teste.
     * Folosește implementarea generică deja existentă în proiect.
     */
    private static class InMemoryStocRepository extends AbstractRepository<Integer, Stoc> {
        @Override
        protected Integer getId(Stoc entity) {
            return entity.getId();
        }
    }
}