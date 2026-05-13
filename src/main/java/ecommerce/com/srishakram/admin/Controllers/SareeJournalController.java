package ecommerce.com.srishakram.admin.Controllers;

import ecommerce.com.srishakram.admin.DTO.SareeJournalRequest;
import ecommerce.com.srishakram.admin.Repository.SareeJournalRepository;
import ecommerce.com.srishakram.admin.Service.SareeJournalService;
import ecommerce.com.srishakram.models.SareeJournal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saree-journal")
@RequiredArgsConstructor
public class SareeJournalController {

    private final SareeJournalService service;
    private final SareeJournalRepository sareeJournalRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SareeJournalRequest request) {
        return ResponseEntity.ok(service.save(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        SareeJournal journal = service.getById(id); // Implement this in service
        if (journal != null) {
            return ResponseEntity.ok(journal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recent/journal")
    public ResponseEntity<?> getRecent()
    {
        return new ResponseEntity<>(service.getRecent(), HttpStatus.OK);
    }
    @GetMapping("all/journal")
    public ResponseEntity<?> getAlljournal()
    {
        return new ResponseEntity<>(service.getAlljournal(), HttpStatus.OK);
    }

    // ✏️ UPDATE JOURNAL
    @PutMapping("/{id}")
    public ResponseEntity<SareeJournal> updateJournal(
            @PathVariable Long id,
            @RequestBody SareeJournal body
    ) {
        SareeJournal existing = sareeJournalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        existing.setProductName(body.getProductName());
        existing.setHeroImage(body.getHeroImage());
        existing.setZariCertificateImage(body.getZariCertificateImage());
        existing.setHeritageSteps(body.getHeritageSteps());
        existing.setCustomerReview(body.getCustomerReview());

        return ResponseEntity.ok(sareeJournalRepository.save(existing));
    }

    // 🗑 DELETE JOURNAL
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJournal(@PathVariable Long id) {
        sareeJournalRepository.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}