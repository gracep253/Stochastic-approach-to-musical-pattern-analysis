public class AddressTester {
    public static void main(String[] args){
        Address Grace = new Address();
        System.out.println(Grace.getHouseNum() + " " + Grace.getStreetName() + " " + Grace.getCity() + " " + Grace.getState() + " " + Grace.getZip());
        Grace.setHouseNum(500);
        System.out.println(Grace.getHouseNum());
    }
}
