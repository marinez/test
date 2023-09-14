package fr.insy2s.sesame.dto.response;

import fr.insy2s.sesame.domain.enumeration.TypeContract;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TypeContractResponse {

    private String name;

    public TypeContractResponse(TypeContract typeContract) {
        this.name = typeContract.name();
    }
}
