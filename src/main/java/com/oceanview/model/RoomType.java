package com.oceanview.model;


public class RoomType {

    private String typeId;         // RT001, RT002, RT003
    private String typeName;       // Standard, Deluxe, Suite
    private double pricePerNight;  // 5000.00, 8000.00, 12000.00
    private String description;
    private int    maxOccupancy;   // max number of guests allowed


    public RoomType() {}


    public RoomType(String typeId, String typeName, double pricePerNight,
                    String description, int maxOccupancy) {
        this.typeId         = typeId;
        this.typeName       = typeName;
        this.pricePerNight  = pricePerNight;
        this.description    = description;
        this.maxOccupancy   = maxOccupancy;
    }


    public String getTypeId()        { return typeId; }
    public String getTypeName()      { return typeName; }
    public double getPricePerNight() { return pricePerNight; }
    public String getDescription()   { return description; }
    public int    getMaxOccupancy()  { return maxOccupancy; }


    public void setTypeId(String typeId)               { this.typeId = typeId; }
    public void setTypeName(String typeName)           { this.typeName = typeName; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    public void setDescription(String description)     { this.description = description; }
    public void setMaxOccupancy(int maxOccupancy)      { this.maxOccupancy = maxOccupancy; }

    @Override
    public String toString() {
        return "RoomType{id='" + typeId + "', name='" + typeName +
                "', price=" + pricePerNight + "}";
    }
}
