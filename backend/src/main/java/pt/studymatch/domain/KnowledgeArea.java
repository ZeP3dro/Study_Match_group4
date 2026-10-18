package pt.studymatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A knowledge area groups course units and represents a competency of the student (UC12).
 */
@Entity
@Table(name = "knowledge_areas")
public class KnowledgeArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    protected KnowledgeArea() {
    }

    public KnowledgeArea(String name, String description) {
        update(name, description);
    }

    public void update(String newName, String newDescription) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("O nome da área é obrigatório.");
        }
        this.name = newName.strip();
        this.description = newDescription == null ? "" : newDescription.strip();
    }

    /**
     * UC12: area names are compared ignoring upper/lower case and leading or trailing spaces.
     */
    public boolean hasSameName(String otherName) {
        return otherName != null && name != null && name.equalsIgnoreCase(otherName.strip());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
