package idv.yst.music_list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class frag1 extends Fragment {
    private String Frag1Log="frag1Log";
    private View view;
    public String[] name={"極楽浄土","Eric周興哲 怎麼了","Eric周興哲 你，好不好？"};
    public static int[] icons={R.drawable.music0,R.drawable.music1,R.drawable.music2};
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        view=inflater.inflate(R.layout.music_list,null);
        ListView listView=view.findViewById(R.id.lv);
        MyBaseAdapter adapter=new MyBaseAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(frag1.this.getContext(),Music_Activity.class);
                intent.putExtra("name",name[position]);
                intent.putExtra("position",String.valueOf(position));
                startActivity(intent);
            }
        });
        return view;
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return name[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(frag1.this.getContext(),R.layout.item_layout,null);
            TextView tv_name=view.findViewById(R.id.item_name);
            ImageView iv=view.findViewById(R.id.iv);

            tv_name.setText(name[position]);
            iv.setImageResource(icons[position]);
            return view;
        }

        private ArrayList<HashMap<String,String>> getPlayList(String rootPath) {
            ArrayList<HashMap<String,String>> fileList = new ArrayList<>();
            try {
                File rootFolder = new File(rootPath);
                File[] files = rootFolder.listFiles();
                for (File file : files) {
                    if (file.isDirectory()) {
                        ArrayList<HashMap<String,String>> playList = getPlayList(file.getAbsolutePath());
                        fileList.addAll(playList);
                    } else if (file.getName().endsWith(".mp3")) {
                        HashMap<String, String> song = new HashMap<>();
                        song.put("file_path", file.getAbsolutePath());
                        song.put("file_name", file.getName());
                        fileList.add(song);
                    }
                }
            } catch (Exception e) {
                Log.w(Frag1Log,e.getMessage());
            }
            return fileList;
        }
    }
}
