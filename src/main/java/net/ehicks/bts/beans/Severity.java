//package net.ehicks.bts.beans;
//
//import net.ehicks.bts.ISelectTagSupport;
//import net.ehicks.eoi.EOI;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.List;
//
//@Entity
//@Table(name = "severities")
//public class Severity implements Serializable, ISelectTagSupport
//{
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String name = "";
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (!(obj instanceof Severity)) return false;
//        Severity that = (Severity) obj;
//        return this.id.equals(that.getId());
//    }
//
//    @Override
//    public int hashCode()
//    {
//        return 17 * 37 * id.intValue();
//    }
//
//    public String toString()
//    {
//        return this.getClass().getSimpleName() + ":" + id.toString();
//    }
//
//    @Override
//    public String getValue()
//    {
//        return id.toString();
//    }
//
//    @Override
//    public String getText()
//    {
//        return name;
//    }
//
//    public static List<Severity> getAll()
//    {
//        return EOI.executeQuery("select * from severities");
//    }
//
//    public static Severity getById(Long id)
//    {
//        return EOI.executeQueryOneResult("select * from severities where id=?", Arrays.asList(id));
//    }
//
//    // -------- Getters / Setters ----------
//
//    public Long getId()
//    {
//        return id;
//    }
//
//    public void setId(Long id)
//    {
//        this.id = id;
//    }
//
//    public String getName()
//    {
//        return name;
//    }
//
//    public void setName(String name)
//    {
//        this.name = name;
//    }
//}
