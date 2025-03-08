package com.moroccanvviptrip.api.mvtapi.domain;

import com.moroccanvviptrip.api.mvtapi.domain.enums.Priority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "planned_activities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannedActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Priority priority;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
