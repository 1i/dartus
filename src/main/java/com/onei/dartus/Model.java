package com.onei.dartus;

import com.onei.dartus.Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//A bird sighting
public class Model {

    //sighting position from the HTML page 1-50
    private String resultNumber;
    //reference is a unique increasing ID for every sighting e.g IB129006
    //it is NOT used in equals/compareTo
    private String reference;
    private String date;
    private String commonName;
    private String scientificName;
    private String count;
    private String location;
    private String county;
    private String photo;   // Boolean string "Yes" or ""

    // A sighting is the the same if the birdName, date and county are the same.
    // Even
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        if (date != null ? !date.equals(model.date) : model.date != null) return false;
        if (commonName != null ? !commonName.equals(model.commonName) : model.commonName != null) return false;
        return county != null ? county.equals(model.county) : model.county == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (commonName != null ? commonName.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        return result;
    }
}