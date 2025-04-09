package com.juyb99.dinorunboot.model;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity(name = "Score")
@Table(name = "score")
@NoArgsConstructor
@Getter
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private long scoreId;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "point")
    @Unsigned
    private Integer point;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    public Score(String nickname, Integer point) {
        this.nickname = nickname;
        this.point = point;
    }
}
