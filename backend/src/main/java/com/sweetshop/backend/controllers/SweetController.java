package com.sweetshop.backend.controllers;

import com.sweetshop.backend.services.SweetService;
import com.sweetshop.backend.dtos.PurchaseRequestDTO;
import com.sweetshop.backend.dtos.RestockRequestDTO;
import com.sweetshop.backend.dtos.SweetRequestDTO;
import com.sweetshop.backend.models.Sweet;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sweets")
public class SweetController {

    private final SweetService sweetService;

    public SweetController(SweetService sweetService) {
        this.sweetService = sweetService;
    }

    // -------------------- 1. ADD SWEET (POST) --------------------
    @PostMapping
    public ResponseEntity<Sweet> addSweet(@RequestBody SweetRequestDTO dto) {
        Sweet sweet = new Sweet();
        sweet.setName(dto.getName());
        sweet.setCategory(dto.getCategory());
        sweet.setPrice(dto.getPrice());
        sweet.setQuantity(dto.getQuantity());

        Sweet saved = sweetService.addSweet(sweet);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // -------------------- 2. LIST ALL SWEETS (GET) --------------------
    @GetMapping
    public ResponseEntity<List<Sweet>> getAllSweets() {
        List<Sweet> sweets = sweetService.getAllSweets();
        return ResponseEntity.ok(sweets);
    }

    // -------------------- 3. SEARCH SWEETS --------------------
    @GetMapping("/search")
    public ResponseEntity<List<Sweet>> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        List<Sweet> result = sweetService.searchSweets(name, category, minPrice, maxPrice);
        return ResponseEntity.ok(result);
    }

    // -------------------- 4. UPDATE SWEET (PUT) --------------------
    @PutMapping("/{id}")
    public ResponseEntity<Sweet> updateSweet(
            @PathVariable Long id,
            @RequestBody SweetRequestDTO dto
    ) {
        Sweet updated = sweetService.updateSweet(
                id,
                dto.getName(),
                dto.getCategory(),
                dto.getPrice(),
                dto.getQuantity()
        );
        return ResponseEntity.ok(updated);
    }

    // -------------------- 5. DELETE SWEET --------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSweet(@PathVariable Long id) {
        sweetService.deleteSweet(id);
        return ResponseEntity.noContent().build();  
    }


    // -------------------- 6. PURCHASE SWEET --------------------
    @PostMapping("/{id}/purchase")
    public ResponseEntity<Sweet> purchaseSweet(
            @PathVariable Long id,
            @RequestBody PurchaseRequestDTO dto
    ) {
        Sweet updated = sweetService.purchaseSweet(id, dto.getQuantityToPurchase());
        return ResponseEntity.ok(updated);
    }

    // -------------------- 7. RESTOCK SWEET --------------------
    @PostMapping("/{id}/restock")
    public ResponseEntity<Sweet> restockSweet(
            @PathVariable Long id,
            @RequestBody RestockRequestDTO dto
    ) {
        Sweet updated = sweetService.restockSweet(id, dto.getQuantityToAdd());
        return ResponseEntity.ok(updated);
    }
}
