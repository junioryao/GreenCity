package greencity.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "web_pages")
public class WebPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String webPage;

    @ManyToMany
    private List<Place> places = new ArrayList<>();
}
