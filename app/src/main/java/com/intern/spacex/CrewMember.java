package com.intern.spacex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "crew_member")
public class CrewMember {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("wikipedia")
    private String wikipedia;

    @SerializedName("status")
    private String status;

    @SerializedName("agency")
    private String agency;

    @SerializedName("image")
    private String image;

    public CrewMember(String id, String name, String status, String agency, String image, String wikipedia) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.agency = agency;
        this.image = image;
        this.wikipedia = wikipedia;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgency() {
        return agency;
    }

    public String getImage() {
        return image;
    }

    public String getWikipedia() {
        return wikipedia;
    }


    @Override
    public boolean equals(@Nullable Object newItem) {
        if(newItem instanceof CrewMember) {
            CrewMember temp = (CrewMember) newItem;
            return this.id.equals(temp.id) &&
                    this.status.equals(temp.status) &&
                    this.agency.equals(temp.agency) &&
                    this.name.equals(temp.name) &&
                    this.wikipedia.equals(temp.wikipedia) &&
                    this.image.equals(temp.image);
        } else {
            return false;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "name = " + this.name + " status = " + this.status + "\n";
    }
}
