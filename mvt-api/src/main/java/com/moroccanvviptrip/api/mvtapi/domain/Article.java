package com.moroccanvviptrip.api.mvtapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "articles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "content", length = 5000, nullable = false)
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @CreationTimestamp
    private LocalDateTime createdAt;
}