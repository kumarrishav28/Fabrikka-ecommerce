package com.userservice.user.controller;

import com.fabrikka.common.*;
import com.userservice.user.config.CartClient;
import com.userservice.user.config.NotificationClient;
import com.userservice.user.config.OrderClient;
import com.userservice.user.config.ProductClient;
import com.userservice.user.entity.User;
import com.userservice.user.service.userService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.userservice.user.security.CustomUserDetailsService.userCache;

@Controller
@RequiredArgsConstructor
public class ApplicationController {

    Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    private final userService userService;
    private final CartClient cartClient;
    private final ProductClient productClient;
    private final NotificationClient notificationClient;
    private final OrderClient orderClient;

    @GetMapping("index")
    public String home(Model model) {
        List<ProductDto> products = productClient.getAllProducts().getBody();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/shop")
    public String shop(Model model,
                       @RequestParam(value = "page", required = false) Optional<Integer> page,
                       @RequestParam(value = "size", required = false) Optional<Integer> size,
                       @RequestParam(value = "categories", required = false) Optional<List<String>> categories,
                       @RequestParam(value = "minPrice", required = false) Optional<Double> minPrice,
                       @RequestParam(value = "maxPrice", required = false) Optional<Double> maxPrice,
                       @RequestParam(value = "sort", required = false) Optional<String> sort) {
        // Define the current page and page size. Default to page 0 and size 9.
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(9);
        List<String> currentCategories = categories.orElse(Collections.emptyList());
        Double currentMinPrice = minPrice.orElse(null);
        Double currentMaxPrice = maxPrice.orElse(null);
        String sortOrder = sort.orElse(null);

        // Fetch the paginated data from your product service.
        // This now passes all filter and sort parameters to the product service.
        Page<ProductDto> productPage = productClient.getProductsPaginated(
                currentPage - 1,
                pageSize,
                currentCategories,
                currentMinPrice,
                currentMaxPrice,
                sortOrder).getBody();

        model.addAttribute("productPage", productPage);

        // Generate page numbers for the pagination control
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        // DESIGN SUGGESTION: Fetch all categories to dynamically build the filter UI.
        // This makes your frontend more maintainable as you add/remove categories.
        List<CategoryDto> allCategories = productClient.getAllCategory().getBody();
        model.addAttribute("categories", allCategories);
        model.addAttribute("currentCategories", currentCategories); // Used to re-check selected filters
        return "shop";
    }

    @GetMapping("/login")
    public String login(Model model) {
        // Providing an empty UserDto to bind the registration form fields
        model.addAttribute("user", new UserDto());
        return "login-register";
    }

    @GetMapping("/logout")
    public String logout() {
        removeCachedUser("user");
        return "redirect:/login";
    }

    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        // This endpoint can now redirect to the new login page,
        // which contains the registration form.
        return "redirect:/login";
    }

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @PostMapping("/register/createUser")
    public String registerUser(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult, Model model) {
        User existingUser = userService.findUserByEmail(user.getUserEmail());
        if (existingUser != null) {
            bindingResult.rejectValue("userEmail", null, "There is already an account registered with the email provided");
        }

        // Assuming UserDto has getPassword() and getConfirmPassword() for validation
        if (user.getPassword() != null && !user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            // If there are errors, return to the login page to display them.
            // The 'user' object with its errors is already in the model thanks to @ModelAttribute.
            return "login-register";
        }

        // --- Actions for successful registration ---
        User newUser = userService.createUser(user);
        CompletableFuture.runAsync(() -> sendEmail(List.of(newUser), List.of(), "welcome"));

        return "redirect:/login?register_success";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        User cachedUser = (User) getCachedUser("user");
        Long userId = cachedUser != null ? cachedUser.getId() : null;

        try {
            CartDto cartDto = fetchCart(userId);
            populateCartItemsWithProductDetails(cartDto);
            logger.warn("CartDto: {}", cartDto);
            model.addAttribute("cart", cartDto);

        } catch (Exception e) {
            model.addAttribute("cart", new CartDto());
        }

        return "cart";
    }

    private CartDto fetchCart(Long userId) {
        ResponseEntity<CartDto> response = cartClient.getCart(userId);
        if (response != null && response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        return new CartDto();
    }

    private void populateCartItemsWithProductDetails(CartDto cartDto) {
        for (CartItemDto itemDto : cartDto.getItems()) {
            ResponseEntity<ProductDto> productResponse = productClient.getProductById(itemDto.getProductId());
            logger.warn("ProductDto: {}", productResponse.getBody());
            itemDto.setProduct(productResponse.getBody());
        }
    }

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute("productId") UUID productId, @ModelAttribute("quantity") int quantity) {
        User cachedUser = (User) getCachedUser("user");
        Long userId = cachedUser != null ? cachedUser.getId() : null;
        AddItemRequest request = new AddItemRequest(productId, quantity);
        cartClient.addItemToCart(userId, request);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@ModelAttribute("itemId") UUID itemId) {
        User cachedUser = (User) getCachedUser("user");
        Long userId = cachedUser != null ? cachedUser.getId() : null;
        cartClient.removeItemFromCart(userId, itemId);
        orderClient.removeOrder(userId);
        return "redirect:/cart";
    }

    private void sendEmail(List<User> toUsers, List<User> ccUsers , String templateName) {
        NotificationDetailsDto notification = new NotificationDetailsDto();
        try {
            notification.setTemplateName(templateName);
            notification.setToUserDetails(createReceiversMap(toUsers));
            notification.setCcUserDetails(createReceiversMap(ccUsers));
            ResponseEntity<String> message = notificationClient.sendMail(notification);
            logger.info("Email sent successfully: {}", message.getBody());
        } catch (Exception e) {
            logger.warn("Error occurred while sending email: {}", e.getMessage());
        }
    }

    @GetMapping("order")
    public String showOrderPage() {
        return "order";
    }

    @PostMapping("/order")
    public String placeOrder() {
        User user = (User)getCachedUser("user");
        ResponseEntity<CartDto> cartDtos = cartClient.getCart(user.getId());
        populateCartItemsWithProductDetails(cartDtos.getBody());
        List<CartItemDto> items = cartDtos.getBody().getItems();
        CreateOrderRequest orderItem = new CreateOrderRequest();
        List<CreateOrderRequest.OrderItemRequest> orderItemRequestList = new ArrayList<>();
        orderItem.setUserId(user.getId());
        for (CartItemDto item : items) {
            orderItem.setUserId(user.getId());
            CreateOrderRequest.OrderItemRequest orderItemRequest = new CreateOrderRequest.OrderItemRequest();
            orderItemRequest.setProductId(item.getProductId());
            orderItemRequest.setQuantity(item.getQuantity());
            orderItemRequest.setPrice(item.getProduct().getPrice().doubleValue());
            orderItemRequestList.add(orderItemRequest);
        }
        orderItem.setItems(orderItemRequestList);

        ResponseEntity<OrderResponse> orderResponse = orderClient.createOrder(orderItem);

        return "redirect:/order";
    }

    private Map<String, String> createReceiversMap(List<User> users) {
        Map<String, String> receiversMap = new HashMap<>();
        if (users != null) {
            for (User user : users) {
                receiversMap.put(user.getUserEmail(), user.getUserName());
            }
        }
        return receiversMap;
    }

    public static Object getCachedUser(String username) {
        return userCache.get(username);
    }

    public static void removeCachedUser(String username) {
        userCache.remove(username);
    }
}