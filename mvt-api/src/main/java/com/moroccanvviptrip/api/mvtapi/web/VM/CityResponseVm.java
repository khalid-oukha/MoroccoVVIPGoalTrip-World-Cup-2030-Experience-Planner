package com.moroccanvviptrip.api.mvtapi.web.VM;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CityResponseVm {
    private Long id;
    private String name;
    private String region;
}
