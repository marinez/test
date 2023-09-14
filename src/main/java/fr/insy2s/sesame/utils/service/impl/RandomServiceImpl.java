package fr.insy2s.sesame.utils.service.impl;

import fr.insy2s.sesame.config.Constants;
import fr.insy2s.sesame.utils.service.IRandomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RandomServiceImpl implements IRandomService {


    /**
     * {@inheritDoc}
     */
    @Override
    public String generateRandomUUIDString(int length) {

       if(length <= 0) {
           length = 5;
       }
       if(length> Constants.MAX_LENGTH_UUID){
              length = Constants.MAX_LENGTH_UUID;
       }
          return UUID.randomUUID().toString().substring(0, length);
    }
}
