package co.edu.usbcali.ecommerceusb.repository;

import co.edu.usbcali.ecommerceusb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    List<User> findAll();
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByDocumentNumberAndDocumentTypeId(String documentNumber, Integer documentTypeId);
}