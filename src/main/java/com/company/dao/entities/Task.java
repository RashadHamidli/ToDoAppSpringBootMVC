package com.company.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Table(name = "tasks")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String taskName;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dedline;
    @Column(nullable = false)
    private String taskText;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", dedline=" + dedline +
                ", name='" + taskName + '\'' +
                ", taskText='" + taskText + '\'' +
                ", user=" + (user != null ? user.getId() : "null") + // Örnek: User nesnesinin id'sini al
                '}';
    }
}
