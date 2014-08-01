package io.loli.sc.server.entity.pan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "category_type")
@Entity
public class CategoryType implements Serializable {
    private static final long serialVersionUID = 2751824724350366055L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String name;
    @Column(name = "name_en")
    private String nameEn;
    @Column
    private String type;
    @Column(name = "support_folder")
    private boolean supportFolder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSupportFolder() {
        return supportFolder;
    }

    public void setSupportFolder(boolean supportFolder) {
        this.supportFolder = supportFolder;
    }
}
