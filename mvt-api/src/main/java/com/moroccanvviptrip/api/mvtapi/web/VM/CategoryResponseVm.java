package com.moroccanvviptrip.api.mvtapi.web.VM;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseVm {
    private Long id;
    private String name;
    private String description;
    private String imageUri;
}
