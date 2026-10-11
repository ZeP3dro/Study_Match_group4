package pt.studymatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

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
        this.name = Objects.requireNonNull(name, "Name is required").strip();
        this.description = Objects.requireNonNull(description, "Description is required").strip();

        if (this.name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
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

    public boolean hasSameName(String otherName) {
        return otherName != null && name != null && name.equalsIgnoreCase(otherName.strip());
    }
}
