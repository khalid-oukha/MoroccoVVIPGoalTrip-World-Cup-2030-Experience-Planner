package com.moroccanvviptrip.api.mvtapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    private String imageUri;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Activity> activities;
}
