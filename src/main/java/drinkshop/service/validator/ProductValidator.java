package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    // Limitele pentru preț conform specificației BVA
    private static final double MINIMUM_PRICE = 1.0;
    private static final double MAXIMUM_PRICE = 1000.0;

    @Override
    public void validate(Product product) {

        String errors = "";

        // Validez că ID-ul e pozitiv
        if (product.getId() <= 0)
            errors += "ID invalid!\n";

        // Validez că numele nu e gol sau null
        if (product.getNume() == null || product.getNume().isBlank())
            errors += "Numele nu poate fi gol!\n";

        // Validez prețul: minim 1, maxim 1000
        if (product.getPret() < MINIMUM_PRICE)
            errors += "Pret invalid!\n";

        if (product.getPret() > MAXIMUM_PRICE)
            errors += "Pret invalid!\n";

        // Dacă sunt erori, arunc excepție
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
