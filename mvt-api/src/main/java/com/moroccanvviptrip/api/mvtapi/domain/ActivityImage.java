package com.moroccanvviptrip.api.mvtapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "activity_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;
}