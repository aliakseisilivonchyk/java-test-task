package com.github.aliakseisilivonchyk.consumer.dto;

import java.util.Date;

public record EventDto(String producer, Date receivedDate, String type) {
}
