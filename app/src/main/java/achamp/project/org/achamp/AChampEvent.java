package achamp.project.org.achamp;

import android.graphics.Bitmap;

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
    private String _id;
    private Bitmap picture;

    public AChampEvent()
    {
        title = null;
        description = null;
        address = null;
        beginingDate = null;
        beginingTime = null;
        picture = null;
        _id = null;
    }

    public AChampEvent(String title, String description, String address, String beginingDate, String beginingTime, Bitmap picture)
    {
        this.title = title;
        this.description = description;
        this.address = address;
        this.beginingDate = beginingDate;
        this.beginingTime = beginingTime;
        this.picture = picture;

    }

    public AChampEvent(String title, String description, String address, String beginingDate, String beginingTime, Bitmap picture, String _id)
    {
        this.title = title;
        this.description = description;
        this.address = address;
        this.beginingDate = beginingDate;
        this.beginingTime = beginingTime;
        this.picture = picture;
        this._id = _id;
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

    public Bitmap getPicture(){return picture; }

    public String get_id() {return _id;}

    @Override
    public boolean equals(Object o) {
        if(o == null)
        {
            return false;
        }
        else if(!o.getClass().equals(AChampEvent.class))
        {
            return false;
        }
        else if(this._id.equals(((AChampEvent)o)._id))
        {
            return true;
        }
        return false;
    }
}
