package ecommerce.com.srishakram.Repository;

import ecommerce.com.srishakram.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface adminRepository extends JpaRepository<Admin, Long> {

    @Query("SELECT a.role FROM Admin a WHERE a.password = :password")
    String rolecheck(@Param("password") String password);
}