package sco3.acc.model;

import java.time.LocalDateTime;

public record User(long userId, String serviceAccount, LocalDateTime createdAt) {
}