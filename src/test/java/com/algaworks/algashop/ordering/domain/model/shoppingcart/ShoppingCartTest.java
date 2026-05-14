package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

public class ShoppingCartTest {

    @Test
    void givenCustomer_whenStartShopping_shouldInitializeEmptyCart() {
        var customerId = new CustomerId();

        //Carrinho vazio.
        ShoppingCart cart = ShoppingCart.startShopping(customerId);

         //Verifica os atributos de carrinho que voltam zerados.
        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.id()).isNotNull(),
                c -> Assertions.assertThat(c.customerId()).isEqualTo(customerId),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(Money.ZERO),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(Quantity.ZERO),
                c -> Assertions.assertThat(c.isEmpty()).isTrue(),
                c -> Assertions.assertThat(c.items()).isEmpty()
        );
    }

    @Test
    void givenEmptyCart_whenAddNewItem_shouldContainItemAndRecalculateTotals() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product = ProductTestDataBuilder.aProduct().build();

        cart.addItem(product, new Quantity(2));

         //Verifica que um item com dois de quantity foi removido da lista
        //do carrinho de compras.
        Assertions.assertThat(cart.items()).hasSize(1);
        var item = cart.items().iterator().next();
        Assertions.assertThat(item.productId()).isEqualTo(product.id());
        Assertions.assertThat(item.quantity()).isEqualTo(new Quantity(2));
        Assertions.assertThat(cart.totalItems()).isEqualTo(new Quantity(2));
        Assertions.assertThat(cart.totalAmount()).isEqualTo(
                new Money(product.price().value().multiply(new BigDecimal(2))));
    }

    @Test
    void givenCartWithExistingProduct_whenAddSameProduct_shouldIncrementQuantity() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product = ProductTestDataBuilder.aProduct().build();

        cart.addItem(product, new Quantity(3));
        cart.addItem(product, new Quantity(3));
        var existing = cart.items().iterator().next();
        //Sistema reconhece que adiciona mesmo item duas vezes
        //na lista e da update  na quantidade do item para 6.
        Set<ShoppingCartItem> items = cart.items();
        Assertions.assertThat(items).hasSize(1);
        Assertions.assertThat(existing.quantity()).isEqualTo(new Quantity(6));
    }

    @Test
    void givenCartWithItems_whenRemoveExistingItem_shouldRemoveAndRecalculateTotals() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        var item = cart.items().iterator().next();

        cart.removeItem(item.id());

        //Verifica se item do carrinho foi removido e se
        //o total de quantity do carrinho é a soma dos quantity
        //do ShoppingCartItem (item do carrinho)
        Assertions.assertThat(cart.items()).doesNotContain(item);
        Assertions.assertThat(cart.totalItems()).isEqualTo(
                new Quantity(cart.items().stream().mapToInt(i -> i.quantity().value()).sum())
        );
    }

    @Test
    void givenCartWithItems_whenRemoveNonexistentItem_shouldThrowShoppingCartDoesNotContainItemException() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCartItemId randomId = new ShoppingCartItemId();

        //Tentar remover um item por um id randomico que nao estaa na lista
        //do carrinho de compras, ira lançar exception.
        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(() -> cart.removeItem(randomId));
    }

    @Test
    void givenCartWithItems_whenEmpty_shouldClearAllItemsAndResetTotals() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();

        cart.empty();
        //Testa se a lista de items do carrinho foi zerada, se totalamount é zero.
        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.isEmpty()).isTrue(),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(Quantity.ZERO),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(Money.ZERO)
        );
    }


    @Test
    void givenCartWithItems_whenChangeItemPrice_shouldRecalculateTotalAmount() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();


        Product product = ProductTestDataBuilder.aProduct()
                .build();

        cart.addItem(product, new Quantity(2));

        //Pega um produto com preço igual a 100 e chama
        //metodo refresh que atualiza dados do produto
        product = ProductTestDataBuilder.aProduct()
                .price(new Money("100"))
                .build();
        cart.refreshItem(product);

        //pega o produto de quantidade 2 e preço 100, sendo o total amount
        //igual a 200
        var item = cart.findItem(product.id());

        Assertions.assertThat(item.price()).isEqualTo(new Money("100"));
        Assertions.assertThat(cart.totalAmount()).isEqualTo(new Money("200"));
    }

    @Test
    void givenCartWithItems_whenDetectUnavailableItems_shouldReturnTrue() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().inStock(false).build();
        //Atualiza o produto como se estivesse indisponivel
        cart.refreshItem(product);
        //Veirifica se tem pelo menos um item indisponivel
        Assertions.assertThat(cart.containsUnavailableItems()).isTrue();
    }

    @Test
    void givenCartWithItems_whenChangeQuantityToZero_shouldThrowIllegalArgumentException() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        var item = cart.items().iterator().next();
        //Nao deve permitir mudar a quantidaade para zero, pois lança uma exceção
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> cart.changeItemQuantity(item.id(), Quantity.ZERO));
    }

    @Test
    void givenCartWithItems_whenChangeItemQuantity_shouldRecalculateTotalItems() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        var item = cart.items().iterator().next();

        cart.changeItemQuantity(item.id(), new Quantity(5));
        //Qaundo mudamos a quantidade de um item na lista de items do carrinho
        //entao a logica de somar todas as quantity do ShoppingCarItem é chamado, que é o
        //totalItems do carrinho.
        Assertions.assertThat(cart.totalItems()).isEqualTo(
                new Quantity(cart.items().stream().mapToInt(i -> i.quantity().value()).sum())
        );
    }


    @Test
    void givenCartWithItems_whenFindItemById_shouldReturnItem() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        var item = cart.items().iterator().next();

        var found = cart.findItem(item.id());
        //Testa se um item é achado pelo id
        Assertions.assertThat(found).isEqualTo(item);
    }

    @Test
    public void givenDifferentIds_whenCompareItems_shouldNotBeEqual() {
        ShoppingCart shoppingCart1 = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCart shoppingCart2 = ShoppingCartTestDataBuilder.aShoppingCart().build();
        //Criar dois carrinhos diferentes com items iguais, mas mesmo assim
        //são reconhecidos como carrinhos distintos.
        Assertions.assertThat(shoppingCart1).isNotEqualTo(shoppingCart2);
    }

}
