package fr.insy2s.sesame.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    String password;
    private String email;
}
