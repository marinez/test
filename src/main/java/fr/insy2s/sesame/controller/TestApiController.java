package fr.insy2s.sesame.controller;

import fr.insy2s.sesame.service.ITestApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestApiController {

    private final ITestApiService testApiService;

    /**
     * GET  /unblocked/{uuid} : unblocked user by uuid.
     * @param uuid the uuid of the user to unblocked.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     * @throws fr.insy2s.sesame.error.exception.UserNotFoundException if the user not found.
     * @author Marine Zimmer
     */
    @GetMapping("unblocked-user/{uuid}")
    public ResponseEntity<Void> unblockedUserByUuid(@PathVariable String uuid) {
        log.debug("REST request to unblocked User : {}", uuid);
        testApiService.unblockedUserByUuid(uuid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("delete-user/{uuid}")
    public ResponseEntity<Void> deleteUserByUuid(@PathVariable String uuid) {
        log.debug("REST request to delete User : {}", uuid);
        testApiService.deleteUserByUuid(uuid);
        return ResponseEntity.ok().build();
    }


}
