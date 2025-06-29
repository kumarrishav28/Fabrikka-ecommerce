package com.userservice.user.controller;

import com.fabrikka.common.InventoryDto;
import com.fabrikka.common.ProductDto;
import com.userservice.user.config.InventoryClient;
import com.userservice.user.config.LoadProductClient;
import com.userservice.user.config.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ProductClient productClient;

    private final InventoryClient inventoryClient;

    private final LoadProductClient loadProductClient;

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
        inventoryClient.updateInventory(inventoryDto.getProductId(), inventoryDto.getAvailableStock());
        redirectAttributes.addFlashAttribute("successMessage", "Product has been added successfully!");
        return "redirect:/admin";
    }

    @GetMapping("/inventory")
    public String viewInventory(@RequestParam(value = "productId",required = false) String productId , Model model) {
        if (productId != null && !productId.isEmpty()) {
            InventoryDto inventory = inventoryClient.getInventory(UUID.fromString(productId)).getBody();
            model.addAttribute("inventory", inventory);
        } else {
            model.addAttribute("inventory", null);
        }
        return "inventory";
    }

    @PostMapping("/add-product-bulk")
    public String addProductInBulk(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "File is empty");
            return "redirect:/add-product-bulk";
        }
        try {
            loadProductClient.uploadProductFile(file);
        } catch (Exception e) {
            String encodedMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            redirectAttributes.addFlashAttribute("errorMessage", encodedMsg);
            return "redirect:/add-product-bulk";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Your file is being processed. You will receive a confirmation email once the process is complete.");
        return "redirect:/admin";
    }

    @GetMapping("/add-product-bulk")
    public String addProductInBulkForm() {
        return "add-product-bulk";
    }
}