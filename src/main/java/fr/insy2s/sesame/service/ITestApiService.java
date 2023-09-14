package fr.insy2s.sesame.service;



public interface ITestApiService {
    void unblockedUserByUuid(String uuid);

    void deleteUserByUuid(String uuid);
}
