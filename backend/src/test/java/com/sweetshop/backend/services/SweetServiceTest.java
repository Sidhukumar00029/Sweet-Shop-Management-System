package com.sweetshop.backend.services;

import com.sweetshop.backend.exceptions.InvalidSweetException;
import com.sweetshop.backend.exceptions.SweetAlreadyExistsException;
import com.sweetshop.backend.exceptions.SweetNotFoundException;
import com.sweetshop.backend.models.Sweet;
import com.sweetshop.backend.repositories.SweetRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SweetServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private SweetService sweetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------------------------------------------------------
    // 1. ADD SWEET
    // ----------------------------------------------------------
    @Test
    void testAddSweet_Success() {
        Sweet sweet = new Sweet();
        sweet.setName("Milk Chocolate");
        sweet.setPrice(BigDecimal.valueOf(100));
        sweet.setQuantity(10);

        when(sweetRepository.existsByNameIgnoreCase("Milk Chocolate"))
                .thenReturn(false);

        when(sweetRepository.save(sweet)).thenReturn(sweet);

        Sweet result = sweetService.addSweet(sweet);

        assertEquals("Milk Chocolate", result.getName());
        verify(sweetRepository, times(1)).save(sweet);
    }


    @Test
    void testAddSweet_DuplicateName_ShouldThrow() {
        Sweet sweet = new Sweet();
        sweet.setName("Milk Chocolate");

        when(sweetRepository.existsByNameIgnoreCase("Milk Chocolate"))
                .thenReturn(true);

        assertThrows(SweetAlreadyExistsException.class,
                () -> sweetService.addSweet(sweet));

        verify(sweetRepository, never()).save(any());
    }

    @Test
    void testAddSweet_InvalidPrice_ShouldThrow() {
        Sweet sweet = new Sweet();
        sweet.setName("Milk Chocolate");
        sweet.setPrice(BigDecimal.valueOf(-10));

        assertThrows(InvalidSweetException.class,
                () -> sweetService.addSweet(sweet));
    }

    @Test
    void testAddSweet_InvalidQuantity_ShouldThrow() {

        Sweet sweet = new Sweet();
        sweet.setName("Milk Chocolate");
        sweet.setPrice(BigDecimal.valueOf(100));  // REQUIRED
        sweet.setQuantity(-5); // invalid

        assertThrows(InvalidSweetException.class,
                () -> sweetService.addSweet(sweet));
    }


    // ----------------------------------------------------------
    // 2. GET ALL SWEETS
    // ----------------------------------------------------------
    @Test
    void testGetAllSweets_ShouldReturnList() {
        Sweet s1 = new Sweet();
        Sweet s2 = new Sweet();
        when(sweetRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Sweet> result = sweetService.getAllSweets();

        assertEquals(2, result.size());
    }

    // ----------------------------------------------------------
    // 3. SEARCH SWEETS
    // ----------------------------------------------------------
    @Test
    void testSearchByName_ShouldReturnMatchingList() {

        Sweet s1 = new Sweet();
        s1.setName("Milk Chocolate");

        Sweet s2 = new Sweet();
        s2.setName("Dark Chocolate");

        Sweet s3 = new Sweet();
        s3.setName("Vanilla Candy");

        when(sweetRepository.findAll()).thenReturn(Arrays.asList(s1, s2, s3));

        List<Sweet> result = sweetService.searchSweets("choco", null, null, null);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getName().toLowerCase().contains("choco")));

        verify(sweetRepository, times(1)).findAll();
    }


    @Test
    void testSearchByCategory_ShouldReturnMatchingList() {

        Sweet s1 = new Sweet();
        s1.setCategory("Chocolate");

        Sweet s2 = new Sweet();
        s2.setCategory("Chocolate");

        Sweet s3 = new Sweet();
        s3.setCategory("Candy");

        when(sweetRepository.findAll()).thenReturn(Arrays.asList(s1, s2, s3));

        List<Sweet> result = sweetService.searchSweets(null, "Chocolate", null, null);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getCategory().equalsIgnoreCase("Chocolate")));

        verify(sweetRepository, times(1)).findAll();
    }


    // ----------------------------------------------------------
    // 4. UPDATE SWEET
    // ----------------------------------------------------------
    @Test
    void testUpdateSweet_Success() {
        Sweet existing = new Sweet();
        existing.setId(1L);
        existing.setName("Old Name");
        existing.setCategory("Old Cat");
        existing.setPrice(BigDecimal.valueOf(50));
        existing.setQuantity(10);

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(sweetRepository.save(existing)).thenReturn(existing);

        Sweet result = sweetService.updateSweet(
                1L,
                "New Name",
                "New Cat",
                BigDecimal.valueOf(100),
                20
        );

        assertEquals("New Name", result.getName());
        assertEquals("New Cat", result.getCategory());
        assertEquals(BigDecimal.valueOf(100), result.getPrice());
        assertEquals(20, result.getQuantity());

        verify(sweetRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateSweet_NotFound_ShouldThrow() {
        when(sweetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SweetNotFoundException.class,
                () -> sweetService.updateSweet(99L, "T", "T", BigDecimal.TEN, 5));
    }

    @Test
    void testUpdateSweet_InvalidQuantity_ShouldThrow() {
        Sweet existing = new Sweet();
        existing.setId(1L);

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(InvalidSweetException.class,
                () -> sweetService.updateSweet(
                        1L,
                        "Test",
                        "Chocolate",
                        BigDecimal.valueOf(100),
                        -10
                ));
    }

    @Test
    void testUpdateSweet_InvalidPrice_ShouldThrow() {
        Sweet existing = new Sweet();
        existing.setId(1L);

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(InvalidSweetException.class,
                () -> sweetService.updateSweet(
                        1L,
                        "Test",
                        "Chocolate",
                        BigDecimal.valueOf(-50),
                        10
                ));
    }

    // ----------------------------------------------------------
    // 5. DELETE SWEET
    // ----------------------------------------------------------
    @Test
    void testDeleteSweet_Success() {
        Sweet s = new Sweet();
        s.setId(1L);

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(s));

        sweetService.deleteSweet(1L);

        verify(sweetRepository, times(1)).delete(s);
    }

    @Test
    void testDeleteSweet_NotFound_ShouldThrow() {
        when(sweetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SweetNotFoundException.class,
                () -> sweetService.deleteSweet(99L));
    }

    // ----------------------------------------------------------
    // 6. PURCHASE SWEET
    // ----------------------------------------------------------
    @Test
    void testPurchaseSweet_Success() {
        Sweet s = new Sweet();
        s.setId(1L);
        s.setQuantity(5);

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(s));
        when(sweetRepository.save(s)).thenReturn(s);

        Sweet result = sweetService.purchaseSweet(1L, 3);

        assertEquals(2, result.getQuantity());
        verify(sweetRepository).save(s);
    }

    @Test
    void testPurchaseSweet_InsufficientStock_ShouldThrow() {
        Sweet s = new Sweet();
        s.setId(1L);
        s.setQuantity(2);

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(s));

        assertThrows(InvalidSweetException.class,
                () -> sweetService.purchaseSweet(1L, 5));
    }

    // ----------------------------------------------------------
    // 7. RESTOCK SWEET
    // ----------------------------------------------------------
    @Test
    void testRestockSweet_Success() {
        Sweet s = new Sweet();
        s.setId(1L);
        s.setQuantity(5);

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(s));
        when(sweetRepository.save(s)).thenReturn(s);

        Sweet result = sweetService.restockSweet(1L, 10);

        assertEquals(15, result.getQuantity());
        verify(sweetRepository).save(s);
    }
}
