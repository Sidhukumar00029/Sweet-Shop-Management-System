package com.sweetshop.backend.services;

import com.sweetshop.backend.exceptions.InvalidSweetException;
import com.sweetshop.backend.exceptions.SweetAlreadyExistsException;
import com.sweetshop.backend.exceptions.SweetNotFoundException;
import com.sweetshop.backend.models.Sweet;
import com.sweetshop.backend.repositories.SweetRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SweetService {

    private final SweetRepository sweetRepository;

    public SweetService(SweetRepository sweetRepository) {
        this.sweetRepository = sweetRepository;
    }

    // ---------------------------------------------------------
    // ADD SWEET
    // ---------------------------------------------------------
    public Sweet addSweet(Sweet sweet) {

    	if (sweetRepository.existsByNameIgnoreCase(sweet.getName())) {

            throw new SweetAlreadyExistsException(
                    "Sweet with name '" + sweet.getName() + "' already exists");
        }

        if (sweet.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSweetException("Price cannot be negative");
        }

        if (sweet.getQuantity() < 0) {
            throw new InvalidSweetException("Quantity cannot be negative");
        }

        return sweetRepository.save(sweet);
    }

    // ---------------------------------------------------------
    // LIST ALL SWEETS
    // ---------------------------------------------------------
    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }

    // ---------------------------------------------------------
    // SEARCH SWEETS (name, category, minPrice, maxPrice)
    // ---------------------------------------------------------
    public List<Sweet> searchSweets(String name, String category,
                                    BigDecimal minPrice, BigDecimal maxPrice) {

        List<Sweet> all = sweetRepository.findAll();

        return all.stream()
                .filter(s -> name == null || s.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(s -> category == null || s.getCategory().equalsIgnoreCase(category))
                .filter(s -> minPrice == null || s.getPrice().compareTo(minPrice) >= 0)
                .filter(s -> maxPrice == null || s.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // UPDATE SWEET
    // ---------------------------------------------------------
    public Sweet updateSweet(Long id, String name, String category,
                             BigDecimal price, int quantity) {

        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found"));

        sweet.setName(name);
        sweet.setCategory(category);

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSweetException("Price cannot be negative");
        }
        sweet.setPrice(price);

        if (quantity < 0) {
            throw new InvalidSweetException("Quantity cannot be negative");
        }
        sweet.setQuantity(quantity);

        return sweetRepository.save(sweet);
    }

    // ---------------------------------------------------------
    // DELETE SWEET
    // ---------------------------------------------------------
    public void deleteSweet(Long id) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found"));

        sweetRepository.deleteById(id);
    }

    // ---------------------------------------------------------
    // PURCHASE (reduce quantity)
    // ---------------------------------------------------------
    public Sweet purchaseSweet(Long id, int qty) {

        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found"));

        if (qty <= 0) {
            throw new InvalidSweetException("Purchase quantity must be positive");
        }

        if (sweet.getQuantity() < qty) {
            throw new InvalidSweetException("Not enough stock");
        }

        sweet.setQuantity(sweet.getQuantity() - qty);

        return sweetRepository.save(sweet);
    }

    // ---------------------------------------------------------
    // RESTOCK (increase quantity)
    // ---------------------------------------------------------
    public Sweet restockSweet(Long id, int qty) {

        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found"));

        if (qty <= 0) {
            throw new InvalidSweetException("Restock quantity must be positive");
        }

        sweet.setQuantity(sweet.getQuantity() + qty);

        return sweetRepository.save(sweet);
    }
}
