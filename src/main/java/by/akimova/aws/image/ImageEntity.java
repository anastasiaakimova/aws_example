package by.akimova.aws.image;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@Setter
@Builder
@Entity
@Table(name = "images")
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "size")
    private Long size;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt;

    @Column(name = "image")
    private byte[] image;

}
