public class Address {
    private int houseNum;
    private String streetName;
    private String city;
    private String state;
    private int zip;

    public Address(){
        houseNum = 10;
        streetName = "Glenwood Lane";
        city = "Roslyn Heights";
        state = "NY";
        zip = 11577;
    }

    public int getHouseNum(){
        return houseNum;
    }

    public String getStreetName(){
        return streetName;
    }

    public String getCity(){
        return city;
    }

    public String getState(){
        return state;
    }

    public int getZip(){
        return zip;
    }

    public void setHouseNum(int newHouseNum){
        houseNum = newHouseNum;
    }

    public void setStreetName(String newStreetName){
        streetName = newStreetName;
    }
}
