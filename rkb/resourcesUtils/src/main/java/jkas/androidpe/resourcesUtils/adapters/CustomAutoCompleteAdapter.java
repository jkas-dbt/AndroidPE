package jkas.androidpe.resourcesUtils.adapters;

import android.widget.ArrayAdapter;
import android.widget.Filterable;
import java.util.List;
import android.content.Context;
import android.widget.Filter;
import java.util.ArrayList;

/**
 * @author JKas
 */
public class CustomAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> originalItems = new ArrayList<>();
    private List<String> filteredItems = new ArrayList<>();

    public CustomAutoCompleteAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        filteredItems.clear();
        originalItems.clear();
        originalItems.addAll(items);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                filteredItems.clear();
                try {
                    if (constraint != null) {
                        for (String item : originalItems) {
                            if (item.matches(
                                    ".*" + constraint.toString().replace(" ", "_") + ".*")) {
                                filteredItems.add(item);
                            }
                        }
                    }
                } catch (Exception err) {
                    // cannot retrieve the list.
                }
                results.values = filteredItems;
                results.count = filteredItems.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    clear();
                    @SuppressWarnings("unchecked")
                    List<String> list = (List<String>) results.values;
                    addAll(list);
                    notifyDataSetChanged();
                }
            }
        };
    }
}
