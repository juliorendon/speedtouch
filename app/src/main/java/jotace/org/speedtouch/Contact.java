package jotace.org.speedtouch;

public class Contact {

    private int id;

    private String name;

    private String number;

    private String image;

    public Contact() {

    }

    public Contact(String contactName, String contactNumber, String contactImg) {
        this.name = contactName;
        this.number = contactNumber;
        this.image = contactImg;
    }

    public Contact(int contactId, String contactName, String contactNumber, String contactImg) {
        this.id = contactId;
        this.name = contactName;
        this.number = contactNumber;
        this.image = contactImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

} // END
