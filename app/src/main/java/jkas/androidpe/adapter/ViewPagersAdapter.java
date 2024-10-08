package jkas.androidpe.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JKas
 */
public class ViewPagersAdapter extends FragmentStateAdapter {
    private List<Fragment> listFrag = new ArrayList<>();

    public ViewPagersAdapter(
            @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        listFrag.clear();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return listFrag.get(position);
    }

    @NonNull
    public void addFragment(Fragment frag) {
        listFrag.add(frag);
    }

    @Override
    public int getItemCount() {
        return listFrag.size();
    }
}
