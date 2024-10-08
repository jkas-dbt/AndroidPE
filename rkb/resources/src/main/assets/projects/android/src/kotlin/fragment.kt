package $PACKAGE_NAME$

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class $CLASS_NAME$ : Fragment() {

  companion object {
    fun newInstance(): $CLASS_NAME$ {
      return DogDetailsFragment()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.$LAYOUT_NAME$, container, false)
  }

}
