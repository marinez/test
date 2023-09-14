package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.error.exception.UserNotFoundException;
import fr.insy2s.sesame.repository.IAddressRepository;
import fr.insy2s.sesame.repository.IOrganizationRepository;
import fr.insy2s.sesame.repository.IUserRepository;
import fr.insy2s.sesame.service.IOrganizationService;
import fr.insy2s.sesame.service.ITestApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestApiServiceImpl implements ITestApiService {

    private final IUserRepository userRepository;
    private final IOrganizationRepository organizationRepository;
    private final IAddressRepository addressRepository;
    @Override
    public void unblockedUserByUuid(String uuid) {
        User user = userRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setFailedLoginAttempts(0);
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }

    @Override
    public void deleteUserByUuid(String uuid) {
        User userToDelete = userRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        List<User> users = userRepository.findAll();

        //supprimer les clés étrangères pour les subordonnées de l'utilisateur à supprimer
        users.stream().forEach(user -> {
            if(Objects.nonNull(user.getManager()) &&  user.getManager().equals(userToDelete)){
                user.setManager(null);
            }
        });
        userRepository.saveAll(users);


        userRepository.delete(userToDelete);

        // supprimer l'organisation et de l'adresse si non utilisé
        long organizationUse =  users.stream().filter(user -> Objects.nonNull(user.getOrganization())  && user.getOrganization().equals(userToDelete.getOrganization()) && !user.equals(userToDelete)).count();
        if(organizationUse==0) {
            int addressId = userToDelete.getOrganization().getAddress().getId();
            organizationRepository.delete(userToDelete.getOrganization());
            long addressUse = organizationRepository.findAll().stream().filter(organization -> organization.getAddress().getId() == addressId).count();
            if (addressUse == 0) {
                addressRepository.deleteById(addressId);
            }
        }





    }
}
