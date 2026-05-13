package ecommerce.com.srishakram.admin.Service;

import ecommerce.com.srishakram.admin.Repository.CatelogRepository;
import ecommerce.com.srishakram.models.Catelog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatelogService {

    @Autowired
    private CatelogRepository Catelogrepo;

    public Catelog save(Catelog body)
    {
        return Catelogrepo.save(body);
    }

    public Catelog update(Long id, Catelog body) {
        Catelog existing = Catelogrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existing.setCategory(body.getCategory());
        existing.setSubcategory(
                body.getSubcategory() != null ? body.getSubcategory() : existing.getSubcategory()
        );
        existing.setImage(body.getImage());

        return Catelogrepo.save(existing);
    }

    // DELETE CATEGORY
    public void delete(Long id) {
        Catelogrepo.deleteById(id);
    }

    // DELETE ONLY ONE SUBCATEGORY
    public Catelog removeSubcategory(Long id, String sub) {

        Catelog category = Catelogrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<String> subs = category.getSubcategory();

        subs.removeIf(s -> s.equalsIgnoreCase(sub));

        category.setSubcategory(subs);

        return Catelogrepo.save(category);
    }

}
