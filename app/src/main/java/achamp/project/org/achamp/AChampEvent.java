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


    private String title;
    private String description;
    private String address;
    private String beginingDate;
    private String beginingTime;

    public AChampEvent()
    {
        title = null;
        description = null;
        address = null;
        beginingDate = null;
        beginingTime = null;
    }

    public AChampEvent(String title, String description, String address, String beginingDate, String beginingTime)
    {
        this.title = title;
        this.description = description;
        this.address = address;
        this.beginingDate = beginingDate;
        this.beginingTime = beginingTime;
    }


    public String getTitle() {
        return title;
    }

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

}
