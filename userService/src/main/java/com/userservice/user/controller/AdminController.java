package com.userservice.user.controller;

import com.fabrikka.common.InventoryDto;
import com.fabrikka.common.ProductDto;
import com.userservice.user.config.InventoryClient;
import com.userservice.user.config.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ProductClient productClient;

    private final InventoryClient inventoryClient;

    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/add-product")
    public String addProductForm() {
        return "add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute ProductDto productDto, RedirectAttributes redirectAttributes) {
        productClient.addProduct(productDto);
        redirectAttributes.addFlashAttribute("successMessage", "Product has been added successfully!");
        return "redirect:/admin";
    }

    @GetMapping("/add-inventory")
    public String addInventoryForm() {
        return "add-inventory";
    }

    @GetMapping("/products")
    public String viewProducts(Model model) {
        List<ProductDto> productDtoList = productClient.getAllProducts().getBody();
        model.addAttribute("productDtoList", productDtoList);
        return "product-list";
    }

    @PostMapping("/add-inventory")
    public String addInventory(@ModelAttribute InventoryDto inventoryDto, RedirectAttributes redirectAttributes) {
        inventoryClient.addInventory(inventoryDto);
        redirectAttributes.addFlashAttribute("successMessage", "Product has been added successfully!");
        return "redirect:/admin";
    }

    @GetMapping("/inventory")
    public String viewInventory(@RequestParam("productId") UUID productId , Model model) {
        InventoryDto inventory = inventoryClient.getInventory(productId).getBody();
        model.addAttribute("inventory", inventory);
        return "inventory";
    }
}