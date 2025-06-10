package com.cachesystem.cacheserver.protocol;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestData {
    private String key;
    private String value;
    // timestamp

}
