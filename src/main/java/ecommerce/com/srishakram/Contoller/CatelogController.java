package ecommerce.com.srishakram.Contoller;

import ecommerce.com.srishakram.admin.Repository.CatelogRepository;
import ecommerce.com.srishakram.admin.Service.CatelogService;
import ecommerce.com.srishakram.models.Catelog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CatelogController {

    @Autowired
    private CatelogService CatelogService;
    @Autowired
    private CatelogRepository catelogRepository;

    @PostMapping("/admin/auth/catelog")
    public ResponseEntity<Catelog> savecatelog(@RequestBody Catelog body)
    {
        return new ResponseEntity<>(CatelogService.save(body), HttpStatus.CREATED);
    }

    @PostMapping("/admin/auth/catelog/add-subcategory")
    public ResponseEntity<?> addSubcategory(@RequestBody Map<String,String> body) {

        Long id = Long.valueOf(body.get("catelogId"));
        String sub = body.get("subcategory");

        Catelog c = catelogRepository.findById(id).orElseThrow();

        c.getSubcategory().add(sub);

        catelogRepository.save(c);

        return ResponseEntity.ok().build();
    }
    @GetMapping("/catelog")
    public List<Catelog> getCatalog() {
        return catelogRepository.findAllByOrderByIdAsc();
    }

    @PutMapping("catelog/{id}")
    public ResponseEntity<Catelog> update(
            @PathVariable Long id,
            @RequestBody Catelog body
    ) {
        Catelog updated = CatelogService.update(id, body);
        return ResponseEntity.ok(updated);
    }

    // 🗑 DELETE CATEGORY
    @DeleteMapping("catelog/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        CatelogService.delete(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    // 🗑 DELETE SINGLE SUBCATEGORY
    @DeleteMapping("/{id}/subcategory")
    public ResponseEntity<Catelog> deleteSubcategory(
            @PathVariable Long id,
            @RequestParam String sub
    ) {
        Catelog updated = CatelogService.removeSubcategory(id, sub);
        return ResponseEntity.ok(updated);
    }


}
