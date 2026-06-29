package sco3.acc.dto;


import java.time.LocalDateTime;

public record UserDto(
        long userId,
        String serviceAccount,
        LocalDateTime createdAt
) {
}