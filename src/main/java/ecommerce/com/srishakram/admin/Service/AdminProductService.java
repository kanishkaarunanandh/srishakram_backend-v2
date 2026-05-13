package ecommerce.com.srishakram.admin.Service;

import ecommerce.com.srishakram.Repository.CartRepository;
import ecommerce.com.srishakram.admin.Repository.AdminProductRepository;
import ecommerce.com.srishakram.models.Products;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminProductService {
    @Autowired
    private AdminProductRepository productrepo;

    @Autowired
    private CartRepository cartRepository;

    public Products save(Products values) {
        return productrepo.save(values);
    }

    @Transactional
    public Products updateById(Long id, Products productData) {
        // 🔥 DEBUG: Log incoming data
        if (productData.getImages() != null) {
            System.out.println("✅ Received images count: " + productData.getImages().size());
            for (int i = 0; i < productData.getImages().size(); i++) {
                System.out.println("  Image " + (i + 1) + ": " + productData.getImages().get(i));
            }
        } else {
            System.out.println("❌ No images received in request");
        }

        return productrepo.findById(id)
                .map(existingProduct -> {
                    System.out.println("Found existing product, updating fields...");

                    // Update basic fields
                    if (productData.getTitle() != null && !productData.getTitle().isEmpty()) {
                        existingProduct.setTitle(productData.getTitle());
                    }
                    if (productData.getDescription() != null && !productData.getDescription().isEmpty()) {
                        existingProduct.setDescription(productData.getDescription());
                    }
                    if (productData.getOffer_price() != null) {
                        existingProduct.setOffer_price(productData.getOffer_price());
                    }
                    if (productData.getImg() != null && !productData.getImg().isEmpty()) {
                        existingProduct.setImg(productData.getImg());
                        System.out.println("Updated cover image: " + productData.getImg());
                    }

                    // 🔥 CRITICAL: Update images/thumbnails
                    if (productData.getImages() != null) {
                        existingProduct.setImages(productData.getImages());
                    }

                    if (productData.getCategory() != null) {
                        existingProduct.setCategory(productData.getCategory());
                    }

                    if (productData.getSubcategory() != null) {
                        existingProduct.setSubcategory(productData.getSubcategory());
                    }

                    if (productData.getPrice() != null) {
                        existingProduct.setPrice(productData.getPrice());
                    }

                    if (productData.getColor() != null) {
                        existingProduct.setColor(productData.getColor());
                    }

                    if (productData.getWeight() != null) {
                        existingProduct.setWeight(productData.getWeight());
                    }

                    if (productData.getBlouselength() != null) {
                        existingProduct.setBlouselength(productData.getBlouselength());
                    }

                    if (productData.getSareelength() != null) {
                        existingProduct.setSareelength(productData.getSareelength());
                    }

                    if (productData.getInstock() != null) {
                        existingProduct.setInstock(productData.getInstock());
                    }

                    if (productData.getNewArrival() != null) {
                        existingProduct.setNewArrival(productData.getNewArrival());
                    }
                    Products savedProduct = productrepo.save(existingProduct);

                    // 🔥 DEBUG: Log saved data
                    System.out.println("=== SAVE RESULT ===");
                    if (savedProduct.getImages() != null) {
                        System.out.println("✅ Saved product images count: " + savedProduct.getImages().size());
                        for (int i = 0; i < savedProduct.getImages().size(); i++) {
                            System.out.println("  Image " + (i + 1) + ": " + savedProduct.getImages().get(i));
                        }
                    } else {
                        System.out.println("❌ No images in saved product!");
                    }
                    return savedProduct;
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    @Transactional
    public void deleteById(Long id) {

        if (!productrepo.existsById(id)) {
            throw new RuntimeException("Product not found");
        }

        System.out.println("=== DELETING PRODUCT ===");
        System.out.println("Product ID: " + id);

        // 🔥 STEP 1: remove from cart
        cartRepository.deleteByProductId(id);
        System.out.println("✅ Deleted from cart");

        // 🔥 STEP 2: delete product
        productrepo.deleteById(id);
        System.out.println("✅ Deleted from products");

        System.out.println("========================");
    }
}