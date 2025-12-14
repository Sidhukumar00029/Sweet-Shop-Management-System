package com.sweetshop.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweetshop.backend.dtos.PurchaseRequestDTO;
import com.sweetshop.backend.dtos.RestockRequestDTO;
import com.sweetshop.backend.dtos.SweetRequestDTO;
import com.sweetshop.backend.models.Sweet;
import com.sweetshop.backend.services.SweetService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SweetController.class)
class SweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SweetService sweetService;

    // ------------------------------------------------------------
    // 1. ADD SWEET
    // ------------------------------------------------------------
    @Test
    void testAddSweet_Success() throws Exception {

        SweetRequestDTO dto = new SweetRequestDTO();
        dto.setName("Milk Chocolate");
        dto.setCategory("Chocolate");
        dto.setPrice(BigDecimal.valueOf(100));
        dto.setQuantity(20);

        Sweet saved = new Sweet();
        saved.setId(1L);
        saved.setName(dto.getName());
        saved.setCategory(dto.getCategory());
        saved.setPrice(dto.getPrice());
        saved.setQuantity(dto.getQuantity());

        when(sweetService.addSweet(any(Sweet.class))).thenReturn(saved);

        mockMvc.perform(post("/api/sweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Milk Chocolate"));
    }

    // ------------------------------------------------------------
    // 2. GET ALL SWEETS
    // ------------------------------------------------------------
    @Test
    void testGetAllSweets_Success() throws Exception {

        Sweet s1 = new Sweet(1L, "Milk", "General", BigDecimal.valueOf(10), 5);
        Sweet s2 = new Sweet(2L, "Dark", "Chocolate", BigDecimal.valueOf(20), 10);

        List<Sweet> list = Arrays.asList(s1, s2);

        when(sweetService.getAllSweets()).thenReturn(list);

        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ------------------------------------------------------------
    // 3. SEARCH SWEETS
    // ------------------------------------------------------------
    @Test
    void testSearchSweets_Success() throws Exception {

        Sweet s1 = new Sweet(1L, "Choco Bar", "Chocolate", BigDecimal.valueOf(15), 10);
        List<Sweet> list = Arrays.asList(s1);

        when(sweetService.searchSweets(eq("choco"), eq("Chocolate"), eq(BigDecimal.valueOf(10)), eq(BigDecimal.valueOf(20))))
                .thenReturn(list);

        mockMvc.perform(get("/api/sweets/search")
                .param("name", "choco")
                .param("category", "Chocolate")
                .param("minPrice", "10")
                .param("maxPrice", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // ------------------------------------------------------------
    // 4. UPDATE SWEET
    // ------------------------------------------------------------
    @Test
    void testUpdateSweet_Success() throws Exception {

        SweetRequestDTO dto = new SweetRequestDTO();
        dto.setName("Updated");
        dto.setCategory("NewCat");
        dto.setPrice(BigDecimal.valueOf(200));
        dto.setQuantity(50);

        Sweet updated = new Sweet(1L, "Updated", "NewCat", BigDecimal.valueOf(200), 50);

        when(sweetService.updateSweet(eq(1L), anyString(), anyString(), any(BigDecimal.class), anyInt()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/sweets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.quantity").value(50));
    }

    // ------------------------------------------------------------
    // 5. DELETE SWEET
    // ------------------------------------------------------------
    @Test
    void testDeleteSweet_Success() throws Exception {

        Mockito.doNothing().when(sweetService).deleteSweet(1L);

        mockMvc.perform(delete("/api/sweets/1"))
                .andExpect(status().isNoContent());
    }

    // ------------------------------------------------------------
    // 6. PURCHASE SWEET
    // ------------------------------------------------------------
    @Test
    void testPurchaseSweet_Success() throws Exception {

        PurchaseRequestDTO dto = new PurchaseRequestDTO();
        dto.setQuantityToPurchase(3);

        Sweet updated = new Sweet(1L, "Milk", "General", BigDecimal.valueOf(10), 2);

        when(sweetService.purchaseSweet(eq(1L), eq(3))).thenReturn(updated);

        mockMvc.perform(post("/api/sweets/1/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(2));
    }

    // ------------------------------------------------------------
    // 7. RESTOCK SWEET
    // ------------------------------------------------------------
    @Test
    void testRestockSweet_Success() throws Exception {

        RestockRequestDTO dto = new RestockRequestDTO();
        dto.setQuantityToAdd(10);

        Sweet updated = new Sweet(1L, "Milk", "General", BigDecimal.valueOf(10), 15);

        when(sweetService.restockSweet(eq(1L), eq(10))).thenReturn(updated);

        mockMvc.perform(post("/api/sweets/1/restock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(15));
    }
}
