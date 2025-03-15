package com.moroccanvviptrip.api.mvtapi.web.VM;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityResponseVm {
    private Long id;
    private String name;
    private String region;
}
