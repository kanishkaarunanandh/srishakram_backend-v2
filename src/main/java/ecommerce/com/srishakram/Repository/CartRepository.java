package ecommerce.com.srishakram.Repository;

import ecommerce.com.srishakram.models.Cart;
import ecommerce.com.srishakram.models.Users;
import ecommerce.com.srishakram.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUsers(Users users);

    Optional<Cart> findByIdAndUsers(Long id, Users users);

    Optional<Cart> findByUsersAndProduct(Users users, Products product);
    void deleteByProductId(Long productId);
}
