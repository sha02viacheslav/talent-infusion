package com.talent.infusion.entiry.talent;


import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("ti_talents")
public class Talent extends Model {

    public Integer getId() {
        return getInteger("id");
    }
}
