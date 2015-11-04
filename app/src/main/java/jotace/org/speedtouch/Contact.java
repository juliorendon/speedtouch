package jotace.org.speedtouch;

public class Contact {
    private String image;

    private String name;

    private String number;

    public Contact(String img, String contactName, String contactNumber) {
        image = img;
        name = contactName;
        number = contactNumber;
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
