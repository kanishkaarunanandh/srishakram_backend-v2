package ecommerce.com.srishakram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "customer_order")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Users user;

    private String name;
    private String email;
    private String phoneNo;
    private String address;
    private String city;
    private String state;
    private String pinCode;
    private String country;

    @ElementCollection
    @CollectionTable(name = "customer_order_items", joinColumns = @JoinColumn(name = "order_id"))
    private List<CartItem> items;

    private String orderStatus;
    private String paymentMethod;
    private Integer amount;
    private String paymentId;
}
