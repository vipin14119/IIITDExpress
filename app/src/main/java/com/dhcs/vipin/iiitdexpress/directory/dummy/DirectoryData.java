package com.dhcs.vipin.iiitdexpress.directory.dummy;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by vipin on 26/03/18.
 */

public class DirectoryData {

    // public static final ArrayList<PersonData> PERSON_LIST =


    public class PersonData{
        public final String id;
        public final String name;
        public final String number;
        public final String desc;

        public PersonData(String id, String name, String number, String desc){
            this.id = id;
            this.name = name;
            this.number = number;
            this.desc = desc;
        }

    }
}
