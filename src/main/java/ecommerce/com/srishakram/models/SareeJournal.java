package ecommerce.com.srishakram.models;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "saree_journal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SareeJournal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    // Hero image of saree
    private String heroImage;

    // Dynamic heritage steps (JSON)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private JsonNode heritageSteps;

    // Zari certificate image
    private String zariCertificateImage;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private JsonNode customerReview;
}
