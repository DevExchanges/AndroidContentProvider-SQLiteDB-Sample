package info.devexchanges.contentproviderwithsqlitedb;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Friend> {

    private MainActivity activity;

    public ListViewAdapter(MainActivity context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        this.activity = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //set data to views
        holder.job.setText(getItem(position).getJob());
        holder.name.setText(getItem(position).getName());

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(getItem(position));
            }
        });

        return convertView;
    }

    public void showAlertDialog(final Friend friend) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete Confirm");
        builder.setCancelable(true);
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //deleting a record in database table based on "name"
                String selection = DBHelper.COL_NAME + " = \"" + friend.getName() + "\"";
                int rowsDeleted = activity.getContentResolver().delete(CustomContentProvider.CONTENT_URI, selection, null);

                if (rowsDeleted > 0) {
                    Toast.makeText(activity, "Deleted!", Toast.LENGTH_SHORT).show();

                    //reloading data
                    activity.showAllFriends();
                } else {
                    Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private class ViewHolder {
        private TextView name;
        private TextView job;
        private ImageView btnDel;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.name);
            job = (TextView) v.findViewById(R.id.job);
            btnDel = (ImageView) v.findViewById(R.id.btn_del);
        }
    }
}
