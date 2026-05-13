package ecommerce.com.srishakram.Repository;

import ecommerce.com.srishakram.models.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface productRepository extends JpaRepository<Products, Long> {

    /* =========================
       SEARCH
       ========================= */

    List<Products> findByTitleContainingIgnoreCase(String title);

    @Query("""
       SELECT p FROM Products p
       WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%'))
          OR LOWER(p.category) LIKE LOWER(CONCAT('%', :q, '%'))
    """)
    List<Products> searchMainProducts(String q);

    @Query("""
       SELECT p FROM Products p
       WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%'))
          OR LOWER(p.category) LIKE LOWER(CONCAT('%', :q, '%'))
          OR LOWER(p.color) LIKE LOWER(CONCAT('%', :q, '%'))
    """)
    List<Products> searchEverywhere(String q);

    List<Products> findTop10ByColorIgnoreCase(String q);

    List<Products> findTop7ByOrderByIdDesc();

    Optional<Products> findById(Long id);

    /* =========================
       CATEGORY + FILTER QUERIES
       ========================= */

    Page<Products> findByCategoryAndPriceBetween(
            String category, Double min, Double max, Pageable pageable);

    Page<Products> findByCategoryAndPriceBetweenAndColorIn(
            String category, Double min, Double max, List<String> color, Pageable pageable);

    Page<Products> findByCategoryAndPriceBetweenAndInstockTrue(
            String category, Double min, Double max, Pageable pageable);

    Page<Products> findByCategoryAndPriceBetweenAndInstockTrueAndColorIn(
            String category, Double min, Double max, List<String> color, Pageable pageable);

    Page<Products> findByCategoryAndPriceBetweenAndNewArrivalTrue(
            String category, Double min, Double max, Pageable pageable);

    Page<Products> findByCategoryAndPriceBetweenAndNewArrivalTrueAndColorIn(
            String category, Double min, Double max, List<String> color, Pageable pageable);

    Page<Products> findByCategoryAndPriceBetweenAndInstockTrueAndNewArrivalTrue(
            String category, Double min, Double max, Pageable pageable);

    Page<Products> findByCategoryAndPriceBetweenAndInstockTrueAndNewArrivalTrueAndColorIn(
            String category, Double min, Double max, List<String> color, Pageable pageable);

    /* =========================
       ALL CATEGORY (NO CATEGORY FILTER)
       ========================= */

    Page<Products> findByPriceBetween(
            Double min, Double max, Pageable pageable);

    Page<Products> findByPriceBetweenAndColorIn(
            Double min, Double max, List<String> color, Pageable pageable);

    Page<Products> findByPriceBetweenAndInstockTrue(
            Double min, Double max, Pageable pageable);

    Page<Products> findByPriceBetweenAndInstockTrueAndColorIn(
            Double min, Double max, List<String> color, Pageable pageable);

    Page<Products> findByPriceBetweenAndNewArrivalTrue(
            Double min, Double max, Pageable pageable);

    Page<Products> findByPriceBetweenAndNewArrivalTrueAndColorIn(
            Double min, Double max, List<String> color, Pageable pageable);

    Page<Products> findByPriceBetweenAndInstockTrueAndNewArrivalTrue(
            Double min, Double max, Pageable pageable);

    Page<Products> findByPriceBetweenAndInstockTrueAndNewArrivalTrueAndColorIn(
            Double min, Double max, List<String> color, Pageable pageable);


    /* =========================
       COUNT — CATEGORY
       ========================= */

    long countByCategoryAndPriceBetweenAndInstockTrue(
            String category, Double min, Double max);

    long countByCategoryAndPriceBetweenAndInstockTrueAndColorIn(
            String category, Double min, Double max, List<String> color);

    long countByCategoryAndPriceBetweenAndNewArrivalTrue(
            String category, Double min, Double max);

    long countByCategoryAndPriceBetweenAndNewArrivalTrueAndColorIn(
            String category, Double min, Double max, List<String> color);

    /* =========================
       COUNT — ALL CATEGORY
       ========================= */

    long countByPriceBetweenAndInstockTrue(
            Double min, Double max);

    long countByPriceBetweenAndInstockTrueAndColorIn(
            Double min, Double max, List<String> color);

    long countByPriceBetweenAndNewArrivalTrue(
            Double min, Double max);

    long countByPriceBetweenAndNewArrivalTrueAndColorIn(
            Double min, Double max, List<String> color);

    @Query("SELECT DISTINCT p.color FROM Products p WHERE p.category = :category AND p.color IS NOT NULL")
    List<String> findDistinctColorsByCategory(String category);

    Page<Products> findBySubcategoryAndPriceBetween(
            String subcategory, Double min, Double max, Pageable pageable);

    @Query("""
SELECT p FROM Products p
WHERE LOWER(p.category) = LOWER(:value)
   OR LOWER(p.subcategory) = LOWER(:value)
""")
    Page<Products> findByCategoryOrSubcategory(String value, Pageable pageable);
    @Query("""
    SELECT p FROM Products p
    WHERE LOWER(p.category) = LOWER(:category)
      AND LOWER(p.subcategory) = LOWER(:subcategory)
      AND p.price BETWEEN :minPrice AND :maxPrice
      AND p.instock = true
      AND p.newArrival = true
""")
    Page<Products> findByCategoryAndSubcategoryAndPriceBetweenAndInstockTrueAndNewArrivalTrue(
            @Param("category") String category,
            @Param("subcategory") String subcategory,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );

    @Query("""
    SELECT p FROM Products p
    WHERE LOWER(p.category) = LOWER(:category)
      AND LOWER(p.subcategory) = LOWER(:subcategory)
      AND p.price BETWEEN :minPrice AND :maxPrice
      AND p.instock = true
      AND p.newArrival = true
      AND p.color IN (:color)
""")
    Page<Products> findByCategoryAndSubcategoryAndPriceBetweenAndInstockTrueAndNewArrivalTrueAndColorIn(
            @Param("category") String category,
            @Param("subcategory") String subcategory,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("color") List<String> color,
            Pageable pageable
    );

    @Query("""
    SELECT p FROM Products p
    WHERE LOWER(p.category) = LOWER(:category)
      AND LOWER(p.subcategory) = LOWER(:subcategory)
      AND p.price BETWEEN :minPrice AND :maxPrice
""")
    Page<Products> findByCategoryAndSubcategoryAndPriceBetween(
            @Param("category") String category,
            @Param("subcategory") String subcategory,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );

    @Query("""
    SELECT p FROM Products p
    WHERE LOWER(p.category) = LOWER(:category)
      AND LOWER(p.subcategory) = LOWER(:subcategory)
      AND p.price BETWEEN :minPrice AND :maxPrice
      AND p.color IN (:color)
""")
    Page<Products> findByCategoryAndSubcategoryAndPriceBetweenAndColorIn(
            @Param("category") String category,
            @Param("subcategory") String subcategory,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("color") List<String> color,
            Pageable pageable
    );


}
