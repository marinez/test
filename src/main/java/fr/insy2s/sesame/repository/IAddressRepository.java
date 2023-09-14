package fr.insy2s.sesame.repository;

import fr.insy2s.sesame.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAddressRepository extends JpaRepository<Address, Integer> {
}