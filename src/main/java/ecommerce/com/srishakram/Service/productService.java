package ecommerce.com.srishakram.Service;

import ecommerce.com.srishakram.Repository.productRepository;
import ecommerce.com.srishakram.models.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class productService {

    @Autowired
    private productRepository productrepo;

    /* =========================
       BASIC METHODS
       ========================= */

    public Products save(Products values) {
        return productrepo.save(values);
    }

    public List<Products> getRecentProducts() {
        return productrepo.findTop6ByOrderByIdDesc();
    }

    public Products getProducts(Long id) {
        return productrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /* =========================
       CATEGORY + FILTER LOGIC
       ========================= */

    public Page<Products> getProductscategory(
            String category,
            Double minPrice,
            Double maxPrice,
            Boolean inStock,
            Boolean newArrival,
            int page,
            List<String> color,
            String subcategory
    ) {

        PageRequest pageable =
                PageRequest.of(page, 15, Sort.by("id").descending());

        boolean isAllCategory =
                "All Kanchipuram Silk Saree".equalsIgnoreCase(category);

        boolean hasColor = color != null && !color.isEmpty();

    /* =========================
       ALL KANCHIPURAM SAREE
       ========================= */
        if (isAllCategory) {

            if (Boolean.TRUE.equals(inStock) && Boolean.TRUE.equals(newArrival)) {
                return hasColor
                        ? productrepo.findByPriceBetweenAndInstockTrueAndNewArrivalTrueAndColorIn(
                        minPrice, maxPrice, color, pageable)
                        : productrepo.findByPriceBetweenAndInstockTrueAndNewArrivalTrue(
                        minPrice, maxPrice, pageable);
            }

            if (Boolean.TRUE.equals(inStock)) {
                return hasColor
                        ? productrepo.findByPriceBetweenAndInstockTrueAndColorIn(
                        minPrice, maxPrice, color, pageable)
                        : productrepo.findByPriceBetweenAndInstockTrue(
                        minPrice, maxPrice, pageable);
            }

            if (Boolean.TRUE.equals(newArrival)) {
                return hasColor
                        ? productrepo.findByPriceBetweenAndNewArrivalTrueAndColorIn(
                        minPrice, maxPrice, color, pageable)
                        : productrepo.findByPriceBetweenAndNewArrivalTrue(
                        minPrice, maxPrice, pageable);
            }

            return hasColor
                    ? productrepo.findByPriceBetweenAndColorIn(
                    minPrice, maxPrice, color, pageable)
                    : productrepo.findByPriceBetween(
                    minPrice, maxPrice, pageable);
        }

    /* =========================
       CATEGORY + SUBCATEGORY + FLAGS
       ========================= */
        if (Boolean.TRUE.equals(inStock) && Boolean.TRUE.equals(newArrival)) {
            if (subcategory != null && !subcategory.isEmpty()) {
                return hasColor
                        ? productrepo.findByCategoryAndSubcategoryAndPriceBetweenAndInstockTrueAndNewArrivalTrueAndColorIn(
                        category, subcategory, minPrice, maxPrice, color, pageable)
                        : productrepo.findByCategoryAndSubcategoryAndPriceBetweenAndInstockTrueAndNewArrivalTrue(
                        category, subcategory, minPrice, maxPrice, pageable);
            }

            return hasColor
                    ? productrepo.findByCategoryAndPriceBetweenAndInstockTrueAndNewArrivalTrueAndColorIn(
                    category, minPrice, maxPrice, color, pageable)
                    : productrepo.findByCategoryAndPriceBetweenAndInstockTrueAndNewArrivalTrue(
                    category, minPrice, maxPrice, pageable);
        }

        if (Boolean.TRUE.equals(inStock)) {
            return hasColor
                    ? productrepo.findByCategoryAndPriceBetweenAndInstockTrueAndColorIn(
                    category, minPrice, maxPrice, color, pageable)
                    : productrepo.findByCategoryAndPriceBetweenAndInstockTrue(
                    category, minPrice, maxPrice, pageable);
        }

        if (Boolean.TRUE.equals(newArrival)) {
            return hasColor
                    ? productrepo.findByCategoryAndPriceBetweenAndNewArrivalTrueAndColorIn(
                    category, minPrice, maxPrice, color, pageable)
                    : productrepo.findByCategoryAndPriceBetweenAndNewArrivalTrue(
                    category, minPrice, maxPrice, pageable);
        }

    /* =========================
       CATEGORY + SUBCATEGORY (NO FLAGS)
       ========================= */
        if (subcategory != null && !subcategory.isEmpty()) {
            return hasColor
                    ? productrepo.findByCategoryAndSubcategoryAndPriceBetweenAndColorIn(
                    category, subcategory, minPrice, maxPrice, color, pageable)
                    : productrepo.findByCategoryAndSubcategoryAndPriceBetween(
                    category, subcategory, minPrice, maxPrice, pageable);
        }

    /* =========================
       CATEGORY ONLY (NO FLAGS)
       ========================= */
        return hasColor
                ? productrepo.findByCategoryAndPriceBetweenAndColorIn(
                category, minPrice, maxPrice, color, pageable)
                : productrepo.findByCategoryAndPriceBetween(
                category, minPrice, maxPrice, pageable);
    }


    /* =========================
       COUNT METHODS
       ========================= */

    public long countInStock(String category, Double min, Double max, List<String> color) {

        boolean isAllCategory =
                "All Kanchipuram Silk Saree".equalsIgnoreCase(category);

        boolean hasColor = color != null && !color.isEmpty();

        if (isAllCategory) {
            return hasColor
                    ? productrepo.countByPriceBetweenAndInstockTrueAndColorIn(min, max, color)
                    : productrepo.countByPriceBetweenAndInstockTrue(min, max);
        }

        return hasColor
                ? productrepo.countByCategoryAndPriceBetweenAndInstockTrueAndColorIn(
                category, min, max, color)
                : productrepo.countByCategoryAndPriceBetweenAndInstockTrue(
                category, min, max);
    }

    public long countNewArrivals(String category, Double min, Double max, List<String> color) {

        boolean isAllCategory =
                "All Kanchipuram Silk Saree".equalsIgnoreCase(category);

        boolean hasColor = color != null && !color.isEmpty();

        if (isAllCategory) {
            return hasColor
                    ? productrepo.countByPriceBetweenAndNewArrivalTrueAndColorIn(min, max, color)
                    : productrepo.countByPriceBetweenAndNewArrivalTrue(min, max);
        }

        return hasColor
                ? productrepo.countByCategoryAndPriceBetweenAndNewArrivalTrueAndColorIn(
                category, min, max, color)
                : productrepo.countByCategoryAndPriceBetweenAndNewArrivalTrue(
                category, min, max);
    }

    /* =========================
       OTHER METHODS
       ========================= */

    public List<Products> showallproduct() {
        return productrepo.findAll();
    }

    public List<Products> searchByTitle(String title) {
        return productrepo.findByTitleContainingIgnoreCase(title);
    }
    public Page<Products> searchByCategoryOrSubcategory(String value, int page) {
        PageRequest pageable = PageRequest.of(page, 15, Sort.by("id").descending());
        return productrepo.findByCategoryOrSubcategory(value, pageable);
    }

/* =========================
   CATEGORY OR SUBCATEGORY
   ========================= */



}
