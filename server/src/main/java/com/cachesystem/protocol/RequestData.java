package com.cachesystem.protocol;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestData {
    private String key;
    private String value;
    private int keyLength;
    private int valueLength;
    // timestamp

}
