package drinkshop.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product product;

    @BeforeEach
    void setUp() {
        product =new Product(100, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
    }

    @AfterEach
    void tearDown() {
        product = null;
    }

    @Test
    void getId() {
        assertEquals(100, product.getId());
    }

    @Test
    void getNume() {
        assertEquals("Limonada", product.getNume());
    }

    @Test
    void getPret() {
        assertEquals(10.0, product.getPret());
    }

    @Test
    void getCategorie() {
        assertEquals(CategorieBautura.JUICE, product.getCategorie());
    }

    @Test
    void setCategorie() {
        product.setCategorie(CategorieBautura.SMOOTHIE);
        assertEquals(CategorieBautura.SMOOTHIE, product.getCategorie());
    }

    @Test
    void getTip() {
        assertEquals(TipBautura.WATER_BASED, product.getTip());
    }

    @Test
    void setTip() {
        product.setTip(TipBautura.BASIC);
        assertEquals(TipBautura.BASIC, product.getTip());
    }

    @Test
    void setNume() {
        product.setNume("newLimonada");
        assertEquals("newLimonada", product.getNume());
    }

    @Test
    void setPret() {
        product.setPret(10.05);
        assertEquals(10.05, product.getPret());
    }

    @Test
    void testToString() {
        assertEquals("Limonada (JUICE, WATER_BASED) - 10.0 lei", product.toString());
    }

}