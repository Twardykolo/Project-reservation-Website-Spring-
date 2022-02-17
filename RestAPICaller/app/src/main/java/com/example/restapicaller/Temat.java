package com.example.restapicaller;

public class Temat {
    private Long id;
    private String name;
    private String description;
    private Boolean isReserved;
    private Integer liczbaOsob;
    private String deadline;

    @Override
    public String toString() {
        return "Nazwa tematu: "+name+'\n'+
                "Opis tematu: "+description+'\n'+
                "Czy został zarezerwowany: "+(isReserved?"Tak":"Nie")+'\n'+
                "Termin ostateczny: "+deadline+'\n'+
                "\n";


    }
//    @Override
//    public String toString() {
//        return "Temat{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", isReserved=" + isReserved +
//                ", liczbaOsob=" + liczbaOsob +
//                ", deadline='" + deadline + '\'' +
//                '}'+'\n';
//    }

    public Temat(Long id, String name, String description, Boolean isReserved, Integer liczbaOsob, String deadline) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isReserved = isReserved;
        this.liczbaOsob = liczbaOsob;
        this.deadline = deadline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getReserved() {
        return isReserved;
    }

    public void setReserved(Boolean reserved) {
        isReserved = reserved;
    }

    public Integer getLiczbaOsob() {
        return liczbaOsob;
    }

    public void setLiczbaOsob(Integer liczbaOsob) {
        this.liczbaOsob = liczbaOsob;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
