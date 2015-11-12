package jotace.org.speedtouch;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ContactsAdapter extends BaseAdapter implements View.OnClickListener, Filterable {

    private Activity activity;
    private ArrayList<Contact> data;
    private ArrayList<Contact> filteredData;
    private static LayoutInflater inflater;
    private ContactFilter mFilter = new ContactFilter();
    public Resources res;

    public ContactsAdapter(Activity a, ArrayList<Contact> d, Resources resLocal) {
        activity = a;
        data = d;
        filteredData = d;
        res = resLocal;
        inflater = a.getLayoutInflater();
    }


    @Override
    public int getCount() {
        if (filteredData.size() <= 0) {
            return 1;
        }

        return filteredData.size();
    }

    @Override
    public Contact getItem(int position) {
        return filteredData.get(position);
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


        if(null != filteredData && filteredData.size() > 0) {

            tempContact = (Contact) filteredData.get(position);

            // Setting values
            holder.getName().setText(tempContact.getName());
            holder.getNumber().setText(tempContact.getNumber());
            holder.getImage().setImageBitmap(ImageHelper.byteArrayToBitmap(tempContact.getImage()));
        }

        return vi;

    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    @Override
    public Filter getFilter() {
        return mFilter;
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

    private class ContactFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<Contact> list = data;

            int count = list.size();
            final ArrayList<Contact> nlist = new ArrayList<Contact>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                Contact filteredItem = list.get(i);
                filterableString = filteredItem.getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(new Contact(filteredItem.getName(), filteredItem.getNumber(), filteredItem.getImage()));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Contact>) results.values;
            notifyDataSetChanged();
        }

    }

} // END
