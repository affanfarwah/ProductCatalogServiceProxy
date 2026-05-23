package com.example.ProductCatalogServiceProxy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    @Test
    public void Test_AddTwoIntegers_ReturnsInteger() {
        //Arrange
        Calculator calculator = new Calculator();
        //Act
        int result = calculator.add(1,2);
        //Assert
        assert(3==result);
    }
    @Test
    public void Test_DivideByZero_ThrowsException() {
        Calculator calculator = new Calculator();
//        int result = calculator.divide(1,0);
        assertThrows(ArithmeticException.class, () -> calculator.divide(1,0));
    }
}