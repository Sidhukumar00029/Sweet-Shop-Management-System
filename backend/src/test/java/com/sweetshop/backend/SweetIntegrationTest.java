package com.sweetshop.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweetshop.backend.models.Sweet;
import com.sweetshop.backend.dtos.SweetRequestDTO;
import com.sweetshop.backend.dtos.PurchaseRequestDTO;
import com.sweetshop.backend.dtos.RestockRequestDTO;
import com.sweetshop.backend.repositories.SweetRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class SweetIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SweetRepository sweetRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        sweetRepository.deleteAll();
    }

    // ---------------------------------------------------------------
    // 1. ADD SWEET
    // ---------------------------------------------------------------
    @Test
    void testAddSweet_Success() throws Exception {

        SweetRequestDTO dto = new SweetRequestDTO();
        dto.setName("Milk Chocolate");
        dto.setCategory("Chocolate");
        dto.setPrice(BigDecimal.valueOf(100));
        dto.setQuantity(20);

        mockMvc.perform(post("/api/sweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Milk Chocolate"));
    }

    // ---------------------------------------------------------------
    // 2. GET ALL SWEETS
    // ---------------------------------------------------------------
    @Test
    void testGetAllSweets() throws Exception {

        Sweet s1 = new Sweet();
        s1.setName("Milk");
        s1.setCategory("General");
        s1.setPrice(BigDecimal.valueOf(10));
        s1.setQuantity(5);
        sweetRepository.save(s1);

        Sweet s2 = new Sweet();
        s2.setName("Dark");
        s2.setCategory("Chocolate");
        s2.setPrice(BigDecimal.valueOf(20));
        s2.setQuantity(10);
        sweetRepository.save(s2);

        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ---------------------------------------------------------------
    // 3. SEARCH SWEETS
    // ---------------------------------------------------------------
    @Test
    void testSearchSweets() throws Exception {

        Sweet s = new Sweet();
        s.setName("Choco Bar");
        s.setCategory("Chocolate");
        s.setPrice(BigDecimal.valueOf(15));
        s.setQuantity(10);
        sweetRepository.save(s);

        mockMvc.perform(get("/api/sweets/search")
                        .param("name", "choco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // ---------------------------------------------------------------
    // 4. UPDATE SWEET
    // ---------------------------------------------------------------
    @Test
    void testUpdateSweet() throws Exception {

        Sweet s = new Sweet();
        s.setName("Original");
        s.setCategory("General");
        s.setPrice(BigDecimal.valueOf(50));
        s.setQuantity(5);
        sweetRepository.save(s);

        SweetRequestDTO dto = new SweetRequestDTO();
        dto.setName("Updated");
        dto.setCategory("Premium");
        dto.setPrice(BigDecimal.valueOf(120));
        dto.setQuantity(30);

        mockMvc.perform(put("/api/sweets/" + s.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.quantity").value(30));
    }

    // ---------------------------------------------------------------
    // 5. DELETE SWEET
    // ---------------------------------------------------------------
    @Test
    void testDeleteSweet() throws Exception {

        Sweet s = new Sweet();
        s.setName("ToDelete");
        s.setCategory("General");
        s.setPrice(BigDecimal.valueOf(20));
        s.setQuantity(5);
        sweetRepository.save(s);

        mockMvc.perform(delete("/api/sweets/" + s.getId()))
                .andExpect(status().isNoContent());
    }

    // ---------------------------------------------------------------
    // 6. PURCHASE SWEET
    // ---------------------------------------------------------------
    @Test
    void testPurchaseSweet() throws Exception {

        Sweet s = new Sweet();
        s.setName("Milk");
        s.setCategory("General");
        s.setPrice(BigDecimal.valueOf(10));
        s.setQuantity(10);
        sweetRepository.save(s);

        PurchaseRequestDTO dto = new PurchaseRequestDTO();
        dto.setQuantityToPurchase(3);

        mockMvc.perform(post("/api/sweets/" + s.getId() + "/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(7));
    }

    // ---------------------------------------------------------------
    // 7. RESTOCK SWEET
    // ---------------------------------------------------------------
    @Test
    void testRestockSweet() throws Exception {

        Sweet s = new Sweet();
        s.setName("Milk");
        s.setCategory("General");
        s.setPrice(BigDecimal.valueOf(10));
        s.setQuantity(5);
        sweetRepository.save(s);

        RestockRequestDTO dto = new RestockRequestDTO();
        dto.setQuantityToAdd(10);

        mockMvc.perform(post("/api/sweets/" + s.getId() + "/restock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(15));
    }
}
