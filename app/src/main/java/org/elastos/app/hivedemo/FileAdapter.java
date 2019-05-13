package org.elastos.app.hivedemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class FileAdapter extends BaseAdapter {

    Context context;

    File[] files;

    ArrayList<File> fileList;

    LayoutInflater layoutInflater;

    public FileAdapter(Context context, File[] files) {
        this.context = context;
        this.files = files;

        layoutInflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public File getItem(int i) {
        return files[i];
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(
            int i,
            View convertView,
            ViewGroup viewGroup) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.file_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.d("viewHolder", holder.id + " : " + i);

        holder.bindData(files[i]);

        holder.setOnclick(context, files[i]);

        return convertView;
    }

    static int counter = 1;

    static class ViewHolder {

        ImageView icon;
        TextView title;
        TextView info;
        ImageButton action;
        int id;

        public ViewHolder(View v) {
            icon = (ImageView) v.findViewById(R.id.imageView_icon);
            title = (TextView) v.findViewById(R.id.textView_name);
            info = (TextView) v.findViewById(R.id.textView_info);
            action = (ImageButton) v.findViewById(R.id.imageButton_action);
            id = counter++;
        }


        public void bindData(File file) {
            icon.setImageResource(
                    file.isDirectory()
                            ? R.drawable.ic_folder
                            : R.drawable.ic_more_vert_black_24dp);

            title.setText(file.getName());


            info.setText(
                    file.isFile()
                            ? String.format("大小：%,d 字节", file.length())
                            : String.format("目录：%d 个文件", file.list().length));
        }

        public void setOnclick(final Context context, final File file) {
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file.isDirectory()) {
                        Toast.makeText(context, "文件夹", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "文件", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}
