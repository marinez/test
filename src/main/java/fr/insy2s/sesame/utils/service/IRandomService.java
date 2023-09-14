package fr.insy2s.sesame.utils.service;

/**
 * Interface for the random service
 *
 * @author Marine Zimmer
 */
public interface IRandomService {


    /**
     * generate a random uuid string of the given length. the length must be between 1 and 36
     *
     * @param length the length of the string
     * @return the generated string
     * @author Marine Zimmer
     */
    String generateRandomUUIDString(int length);

}
