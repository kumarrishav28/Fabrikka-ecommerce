package com.userservice.user.controller;

import com.fabrikka.common.ProductDto;
import com.userservice.user.config.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ProductClient productClient;

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
}