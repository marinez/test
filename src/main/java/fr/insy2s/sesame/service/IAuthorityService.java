package fr.insy2s.sesame.service;

import java.util.List;

public interface IAuthorityService {

    /**
     * get all authorities without ROLE_ADMIN.
     *
     * @return a list of authorities without ROLE_ADMIN.
     * @author Marine Zimmer
     */
    List<String> getAutoritiesWithoutAdmin();

    /**
     * Check if the authority exists in the database.
     *
     * @param authority: the authority to check.
     * @return true if the authority exists, false otherwise.
     */
    boolean authorityExist(String authority);
}
