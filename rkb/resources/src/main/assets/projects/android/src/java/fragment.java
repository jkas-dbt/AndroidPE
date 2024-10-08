package $PACKAGE_NAME$;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;

public class $FRAGMENT_NAME$ extends Fragment {
    private Context C;
    private View _view;

    @Override
    public View onCreateView(
            LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        return _view;
    }

    @Override
    @MainThread
    @CallSuper
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        C = (Context) getActivity();
        _view = LayoutInflater.from(C).inflate(R.layout.$LAYOUT_NAME$, null);
        onCreateCalled = true;
        loadData();
    }

    public void loadData() {
        // all data
    }
}
