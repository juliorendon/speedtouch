package jotace.org.speedtouch;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ContactsAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList<Contact> data;
    private static LayoutInflater inflater = null;
    public Resources res;

    public ContactsAdapter(Activity a, ArrayList<Contact> d, Resources resLocal) {
        activity = a;
        data = d;
        res = resLocal;
        inflater = a.getLayoutInflater();
    }


    @Override
    public int getCount() {
        if (data.size() <= 0) {
            return 1;
        }

        return data.size();
    }

    @Override
    public Contact getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;
        Contact tempContact = null;

        if(convertView == null) {

            // Inflating Contact Item
            vi = inflater.inflate(R.layout.contact_item, null);

            // Assigning values to Holder View
            holder = new ViewHolder();
            holder.setImage((ImageView) vi.findViewById(R.id.contact_image));
            holder.setName((TextView) vi.findViewById(R.id.name));
            holder.setNumber((TextView) vi.findViewById(R.id.number));


            // Set holder with LayoutInflater
            vi.setTag(holder);
        } else {
            holder = (ViewHolder)vi.getTag();
        }

        if(null != data && data.size() > 0) {

            tempContact = (Contact) data.get(position);

            /************  Set Model values in Holder elements ***********/
            holder.getName().setText(tempContact.getName());
            holder.getNumber().setText(tempContact.getNumber());
            /*holder.getImage().setImageResource(
                    res.getIdentifier(
                            "jotace.org.speedtouch:drawable/" + tempContact.getImage()
                            ,null,null));
            */
            /******** Set Item Click Listner for LayoutInflater for each row *******/

            //vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;

    }

    @Override
    public void onClick(View v) {

    }


    public class ViewHolder{

        private TextView name;
        private TextView number;
        private ImageView image;

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getNumber() {
            return number;
        }

        public void setNumber(TextView number) {
            this.number = number;
        }

        public ImageView getImage() {
            return image;
        }

        public void setImage(ImageView image) {
            this.image = image;
        }
    }

} // END
