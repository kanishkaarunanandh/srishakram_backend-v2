package ecommerce.com.srishakram.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "home_products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String title;
    private String description;
    private Integer offer_price;
    private String img;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;

    // Category (plain, silk, pattu, etc.)
    private String category;
    private String subcategory;

    private Boolean instock;
    private Boolean newArrival;

    private Double price;
    private Double blouselength;
    private Double Sareelength;

    private String color;
    private Double weight;
    //private boolean active;
}
