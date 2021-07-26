package idv.yst.music_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class frag2 extends Fragment {
    private View zj;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        zj=inflater.inflate(R.layout.frag2_layout, null);
        ListView listView= zj.findViewById(R.id.lv);
        return zj;
    }
}
