package ecommerce.com.srishakram.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_selection")
@Data
public class CustomerSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String customerid;
    @Column(length = 4000)
    private String colors;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
