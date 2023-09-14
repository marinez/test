package fr.insy2s.sesame.dto.request;

import lombok.*;


public record UserCriteria(
        String authority,
        String search
) {
}
