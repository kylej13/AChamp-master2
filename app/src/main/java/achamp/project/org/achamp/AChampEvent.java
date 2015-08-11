package achamp.project.org.achamp;

/**
 * Created by Nima on 8/4/2015.
 */
public class AChampEvent {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_ADDRRESS = "address";
    public static final String EXTRA_BEGININGDATE = "beginingDate";
    public static final String EXTRA_BEGININGTIME = "beginingTime";
    public static final String EXTRA_PICTURE = "picture";


    private String title;
    private String description;
    private String address;
    private String beginingDate;
    private String beginingTime;
    private String picture;

    public AChampEvent()
    {
        title = null;
        description = null;
        address = null;
        beginingDate = null;
        beginingTime = null;
        picture = null;
    }

    public AChampEvent(String title, String description, String address, String beginingDate, String beginingTime, String picture)
    {
        this.title = title;
        this.description = description;
        this.address = address;
        this.beginingDate = beginingDate;
        this.beginingTime = beginingTime;
        this.picture = picture;
    }


    public String getTitle() {return title;}

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getBeginingDate() {
        return beginingDate;
    }

    public String getBeginingTime() {
        return beginingTime;
    }

    public String getPicture(){return picture; }

}
