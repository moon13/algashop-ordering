package com.algaworks.algashop.ordering.domain.valueobject;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoyaltyPointsTest {

    @Test
    void ShouldGenerateWithValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

    @Test
    void ShouldAddValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);

        Assertions.assertThat(loyaltyPoints.add(5).value()).isEqualTo(15);
    }

    @Test
    void ShouldNotAddValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
         Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                 .isThrownBy( () -> loyaltyPoints.add(-5));
        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

    @Test
    void ShouldNotAddZeroValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> loyaltyPoints.add(0));
        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);
    }



}