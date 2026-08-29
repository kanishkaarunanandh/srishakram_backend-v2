package ecommerce.com.srishakram.Config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ecommerce.com.srishakram.Repository.CartRepository;
import ecommerce.com.srishakram.Repository.CustomerSelectionRepository;
import ecommerce.com.srishakram.Repository.ImageRepository;
import ecommerce.com.srishakram.Repository.adminRepository;
import ecommerce.com.srishakram.Repository.productRepository;
import ecommerce.com.srishakram.admin.Repository.AdminOrdersRepository;
import ecommerce.com.srishakram.admin.Repository.CatelogRepository;
import ecommerce.com.srishakram.admin.Repository.ColorPaletteRepository;
import ecommerce.com.srishakram.admin.Repository.CustomerOrderRepository;
import ecommerce.com.srishakram.admin.Repository.OrderStatusHistoryRepository;
import ecommerce.com.srishakram.admin.Repository.SareeJournalRepository;
import ecommerce.com.srishakram.admin.Repository.UsersRepository;
import ecommerce.com.srishakram.models.Admin;
import ecommerce.com.srishakram.models.Cart;
import ecommerce.com.srishakram.models.CartItem;
import ecommerce.com.srishakram.models.Catelog;
import ecommerce.com.srishakram.models.ColorPalette;
import ecommerce.com.srishakram.models.Contact;
import ecommerce.com.srishakram.models.CustomerOrder;
import ecommerce.com.srishakram.models.CustomerSelection;
import ecommerce.com.srishakram.models.Image;
import ecommerce.com.srishakram.models.OrderStatusHistory;
import ecommerce.com.srishakram.models.Products;
import ecommerce.com.srishakram.models.SareeJournal;
import ecommerce.com.srishakram.models.Users;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Configuration
public class DemoDataSeeder {

    private static final String BASE = "https://srishakram-backend-v2.onrender.com/demo-media/";

    @Bean
    CommandLineRunner seedDemoData(
            productRepository productRepository,
            ImageRepository imageRepository,
            UsersRepository usersRepository,
            adminRepository adminRepository,
            CartRepository cartRepository,
            CustomerSelectionRepository customerSelectionRepository,
            AdminOrdersRepository adminOrdersRepository,
            CatelogRepository catelogRepository,
            ColorPaletteRepository colorPaletteRepository,
            CustomerOrderRepository customerOrderRepository,
            OrderStatusHistoryRepository orderStatusHistoryRepository,
            SareeJournalRepository sareeJournalRepository,
            PasswordEncoder passwordEncoder,
            ObjectMapper objectMapper
    ) {
        return args -> {
            clearDemoData(
                    orderStatusHistoryRepository,
                    customerOrderRepository,
                    cartRepository,
                    customerSelectionRepository,
                    sareeJournalRepository,
                    catelogRepository,
                    colorPaletteRepository,
                    imageRepository,
                    productRepository,
                    usersRepository,
                    adminRepository,
                    adminOrdersRepository
            );

            List<Products> products = seedProducts(productRepository);
            Users customer = seedUsers(usersRepository, adminRepository, passwordEncoder);
            seedContacts(adminOrdersRepository);
            seedCatalog(catelogRepository);
            seedColorPalette(colorPaletteRepository);
            seedHomepageMedia(imageRepository);
            seedCustomerSelection(customerSelectionRepository, customer);
            seedCart(cartRepository, customer, products);
            seedOrders(customerOrderRepository, orderStatusHistoryRepository, customer, products);
            seedSareeJournals(sareeJournalRepository, objectMapper);

            System.out.println("Demo seed loaded: products=" + productRepository.count()
                    + ", catalog=" + catelogRepository.count()
                    + ", homepageMedia=" + imageRepository.count()
                    + ", journals=" + sareeJournalRepository.count());
        };
    }

    private void clearDemoData(
            OrderStatusHistoryRepository orderStatusHistoryRepository,
            CustomerOrderRepository customerOrderRepository,
            CartRepository cartRepository,
            CustomerSelectionRepository customerSelectionRepository,
            SareeJournalRepository sareeJournalRepository,
            CatelogRepository catelogRepository,
            ColorPaletteRepository colorPaletteRepository,
            ImageRepository imageRepository,
            productRepository productRepository,
            UsersRepository usersRepository,
            adminRepository adminRepository,
            AdminOrdersRepository adminOrdersRepository
    ) {
        orderStatusHistoryRepository.deleteAll();
        customerOrderRepository.deleteAll();
        cartRepository.deleteAll();
        customerSelectionRepository.deleteAll();
        sareeJournalRepository.deleteAll();
        catelogRepository.deleteAll();
        colorPaletteRepository.deleteAll();
        imageRepository.deleteAll();
        productRepository.deleteAll();
        usersRepository.deleteAll();
        adminRepository.deleteAll();
        adminOrdersRepository.deleteAll();
    }

    private List<Products> seedProducts(productRepository productRepository) {
        List<Products> products = List.of(
                product(1001L, "Royal Maroon Kanchipuram Silk Saree", "Traditional maroon silk saree with rich zari border for festive occasions.", 8999, 12499.0, "All Kanchipuram Silk Saree", "Contrast", "Maroon", BASE + "img1.jpg", true, true),
                product(1002L, "Peacock Blue Korvai Silk Saree", "Elegant blue korvai weave with temple border and classic pallu detailing.", 10499, 14999.0, "Korvai", "Ganga Jamuna Bordar", "Blue", BASE + "img2.jpg", true, false),
                product(1003L, "Golden Butta Bridal Silk Saree", "Premium bridal silk saree with golden butta motifs and soft sheen.", 12999, 17999.0, "Brocade", "Butta", "Gold", BASE + "img3.jpg", true, true),
                product(1004L, "Rose Pink Double Naada Saree", "Lightweight pink double naada silk saree with a refined woven border.", 7499, 9999.0, "Double Naada", "Stripes", "Pink", BASE + "img4.jpg", true, false),
                product(1005L, "Emerald Morning-Evening Silk Saree", "Dual-tone silk saree with refined color play and graceful drape.", 9499, 13999.0, "Morning-Evening", "Contrast", "Green", BASE + "img5.jpg", false, true),
                product(1006L, "Ivory Pure Silk Saree", "Subtle pure silk saree with understated zari highlights.", 6999, 9499.0, "Pure Silk", "Checks", "Ivory", BASE + "img6.jpg", true, false),
                product(1007L, "Crimson Extra Weft Silk Saree", "Statement crimson saree with extra weft motifs and grand traditional finish.", 11999, 15999.0, "All Kanchipuram Silk Saree", "Extra Weft", "Crimson", BASE + "img7.jpg", true, true)
        );

        return productRepository.saveAll(products);
    }

    private Products product(Long productId, String title, String description, Integer offerPrice, Double price,
                             String category, String subcategory, String color, String imageUrl,
                             boolean instock, boolean newArrival) {
        Products product = new Products();
        product.setProductId(productId);
        product.setTitle(title);
        product.setDescription(description);
        product.setOffer_price(offerPrice);
        product.setPrice(price);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setColor(color);
        product.setImg(imageUrl);
        product.setImages(List.of(imageUrl));
        product.setInstock(instock);
        product.setNewArrival(newArrival);
        product.setBlouselength(0.8);
        product.setSareelength(5.5);
        product.setWeight(0.85);
        return product;
    }

    private Users seedUsers(UsersRepository usersRepository, adminRepository adminRepository, PasswordEncoder passwordEncoder) {
        usersRepository.save(Users.builder()
                .email("admin@srishakram.com")
                .password(passwordEncoder.encode("admin123"))
                .role("ROLE_ADMIN")
                .customerId("ADMIN-DEMO")
                .build());

        Users customer = usersRepository.save(Users.builder()
                .email("customer@srishakram.com")
                .password(passwordEncoder.encode("customer123"))
                .role("ROLE_USER")
                .customerId("CUSTOMER-DEMO")
                .build());

        Admin admin = new Admin();
        admin.setRole("ROLE_ADMIN");
        admin.setPassword("admin123");
        adminRepository.save(admin);

        return customer;
    }

    private void seedContacts(AdminOrdersRepository adminOrdersRepository) {
        Contact contact = new Contact();
        contact.setName("Demo Customer");
        contact.setEmail("customer@srishakram.com");
        contact.setPhoneno(9876543210L);
        contact.setMsg("Interested in a custom Kanchipuram silk saree consultation.");
        contact.setCustom(true);
        adminOrdersRepository.save(contact);
    }

    private void seedCatalog(CatelogRepository catelogRepository) {
        catelogRepository.saveAllAndFlush(List.of(
                catalog("All Kanchipuram Silk Saree", "Extra Weft", "Contrast", "Butta", "Checks", "Stripes", BASE + "banner.webp"),
                catalog("Korvai", "Ganga Jamuna Bordar", "Contrast", "Butta", "Stripes", "Checks", BASE + "img2.jpg"),
                catalog("Double Naada", "Stripes", "Checks", "Contrast", "Butta", "Extra Weft", BASE + "img4.jpg"),
                catalog("Brocade", "Butta", "Extra Weft", "Contrast", "Checks", "Ganga Jamuna Bordar", BASE + "img3.jpg"),
                catalog("All Self", "Checks", "Stripes", "Butta", "Contrast", "Extra Weft", BASE + "img6.jpg"),
                catalog("Pure Silk", "Checks", "Stripes", "Contrast", "Butta", "Extra Weft", BASE + "img6.jpg"),
                catalog("Morning-Evening", "Contrast", "Ganga Jamuna Bordar", "Stripes", "Checks", "Butta", BASE + "img5.jpg")
        ));
    }

    private Catelog catalog(String category, String sub1, String sub2, String sub3, String sub4, String sub5, String image) {
        Catelog catelog = new Catelog();
        catelog.setCategory(category);
        catelog.setSubcategory(List.of(sub1, sub2, sub3, sub4, sub5));
        catelog.setImage(image);
        return catelog;
    }

    private void seedColorPalette(ColorPaletteRepository colorPaletteRepository) {
        colorPaletteRepository.saveAllAndFlush(List.of(
                palette("Maroon", Map.of("border", "#D8A53A", "pallu", "#6F0E18", "zari", "#C9A227")),
                palette("Blue", Map.of("border", "#C9A227", "pallu", "#0E3A66", "zari", "#E0B84C")),
                palette("Gold", Map.of("border", "#7A1E1E", "pallu", "#D4AF37", "zari", "#F3D36B")),
                palette("Green", Map.of("border", "#B88A2A", "pallu", "#0B5A3C", "zari", "#D9B44A")),
                palette("Ivory", Map.of("border", "#A07547", "pallu", "#F4E8D0", "zari", "#BF9B30"))
        ));
    }

    private ColorPalette palette(String main, Map<String, String> variants) {
        ColorPalette palette = new ColorPalette();
        palette.setMain(main);
        palette.setVariants(variants);
        return palette;
    }

    private void seedHomepageMedia(ImageRepository imageRepository) {
        Image image = new Image();
        image.setImg1(BASE + "hero-video.mp4");
        image.setImg2(BASE + "design.mp4");
        image.setImg3(BASE + "design2.mp4");
        image.setImg4(BASE + "design3.mp4");
        image.setImg5(BASE + "design4.mp4");
        image.setImg6(BASE + "design5.mp4");
        image.setImg7(BASE + "download%20(1).jpg");
        image.setImg8(BASE + "download%20(2).jpg");
        image.setImg9(BASE + "download%20(3).jpg");
        image.setImg10(BASE + "11.jpg");
        image.setImg11(BASE + "saree.jpg");
        image.setImg12(BASE + "10.jpg");
        image.setImg13(BASE + "11.jpg");

        imageRepository.saveAndFlush(image);
    }

    private void seedCustomerSelection(CustomerSelectionRepository customerSelectionRepository, Users customer) {
        CustomerSelection selection = new CustomerSelection();
        selection.setCustomerid(customer.getCustomerId());
        selection.setColors("{\"main\":\"Maroon\",\"border\":\"Gold\",\"occasion\":\"Wedding\"}");
        customerSelectionRepository.save(selection);
    }

    private void seedCart(CartRepository cartRepository, Users customer, List<Products> products) {
        Cart firstItem = new Cart();
        firstItem.setUsers(customer);
        firstItem.setProduct(products.get(0));
        firstItem.setQuantity(1L);

        Cart secondItem = new Cart();
        secondItem.setUsers(customer);
        secondItem.setProduct(products.get(2));
        secondItem.setQuantity(2L);

        cartRepository.saveAll(List.of(firstItem, secondItem));
    }

    private void seedOrders(CustomerOrderRepository customerOrderRepository,
                            OrderStatusHistoryRepository orderStatusHistoryRepository,
                            Users customer,
                            List<Products> products) {
        CustomerOrder order = new CustomerOrder();
        order.setUser(customer);
        order.setName("Demo Customer");
        order.setEmail("customer@srishakram.com");
        order.setPhoneNo("9876543210");
        order.setAddress("12 Silk Street");
        order.setCity("Kanchipuram");
        order.setState("Tamil Nadu");
        order.setPinCode("631501");
        order.setCountry("India");
        order.setItems(List.of(orderItem(products.get(0), 1L), orderItem(products.get(1), 1L)));
        order.setOrderStatus("Processing");
        order.setPaymentMethod("Demo UPI");
        order.setAmount(27498);
        order.setPaymentId("demo_payment_1001");

        CustomerOrder savedOrder = customerOrderRepository.save(order);

        OrderStatusHistory placed = orderStatus("Order Placed", savedOrder, LocalDateTime.now().minusDays(1));
        OrderStatusHistory processing = orderStatus("Processing", savedOrder, LocalDateTime.now());
        orderStatusHistoryRepository.saveAll(List.of(placed, processing));
    }

    private CartItem orderItem(Products product, Long quantity) {
        CartItem item = new CartItem();
        item.setProductId(product.getId());
        item.setProductName(product.getTitle());
        item.setQuantity(quantity);
        item.setImage(product.getImg());
        item.setPrice(product.getPrice());
        item.setOffer_price(product.getOffer_price());
        return item;
    }

    private OrderStatusHistory orderStatus(String status, CustomerOrder order, LocalDateTime updatedAt) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setStatus(status);
        history.setCustomerOrder(order);
        history.setUpdatedAt(updatedAt);
        return history;
    }

    private void seedSareeJournals(SareeJournalRepository sareeJournalRepository, ObjectMapper objectMapper) throws Exception {
        JsonNode heritageSteps = objectMapper.readTree("""
                [
                  {
                    "title":"Mulberry Silk Selection",
                    "description":"Fine silk threads are selected for a smooth, lustrous drape.",
                    "image":"https://srishakram-backend-v2.onrender.com/demo-media/10.jpg"
                  },
                  {
                    "title":"Zari Preparation",
                    "description":"Gold-toned zari is readied for borders, motifs, and pallu work.",
                    "image":"https://srishakram-backend-v2.onrender.com/demo-media/zari.webp"
                  },
                  {
                    "title":"Handloom Weaving",
                    "description":"Artisans weave the saree with traditional Kanchipuram structure.",
                    "image":"https://srishakram-backend-v2.onrender.com/demo-media/saree.jpg"
                  },
                  {
                    "title":"Finishing",
                    "description":"The saree is inspected, folded, and prepared for presentation.",
                    "image":"https://srishakram-backend-v2.onrender.com/demo-media/11.jpg"
                  }
                ]
                """);
        JsonNode customerReview = objectMapper.readTree("""
                [
                  {
                    "customerName":"Demo Customer",
                    "content":"Beautiful silk texture, rich zari, and perfect festive finish.",
                    "image":"https://srishakram-backend-v2.onrender.com/demo-media/img1.jpg",
                    "video":""
                  }
                ]
                """);

        sareeJournalRepository.saveAllAndFlush(List.of(
                sareeJournal("Royal Maroon Kanchipuram Silk Saree", BASE + "10.jpg", BASE + "zari.webp", heritageSteps, customerReview),
                sareeJournal("Peacock Blue Korvai Silk Saree", BASE + "11.jpg", BASE + "zari.webp", heritageSteps, customerReview),
                sareeJournal("Golden Butta Bridal Silk Saree", BASE + "saree.jpg", BASE + "zari.webp", heritageSteps, customerReview)
        ));
    }

    private SareeJournal sareeJournal(String productName, String heroImage, String certificate,
                                      JsonNode heritageSteps, JsonNode customerReview) {
        SareeJournal journal = new SareeJournal();
        journal.setProductName(productName);
        journal.setHeroImage(heroImage);
        journal.setZariCertificateImage(certificate);
        journal.setHeritageSteps(heritageSteps);
        journal.setCustomerReview(customerReview);
        return journal;
    }
}
